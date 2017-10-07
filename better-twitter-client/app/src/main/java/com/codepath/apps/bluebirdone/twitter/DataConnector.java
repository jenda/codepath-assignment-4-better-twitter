package com.codepath.apps.bluebirdone.twitter;

import android.app.Application;
import android.support.annotation.StringRes;
import android.util.Log;

import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.models.ErrorResponse;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jan_spidlen on 9/30/17.
 */

@Singleton
public abstract class DataConnector {

    public static final int TWITTER_RATE_LIMIT_CODE = 88;
    public static final int HTTP_TOO_MANY_REQUESTS = 429;

    public interface OnApiFinishedListener {
        void onTweetPosted(Tweet tweet);
        void onFailure(@StringRes int messageRes);
        void onTimeLineFetched(int page, List<Tweet> tweets);
    }

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;
    @Inject
    Application app;

    Set<OnApiFinishedListener> listeners = new HashSet<>();

    public void addOnApiFinishedListener(OnApiFinishedListener l) {
        listeners.add(l);
    }

    public void postTweet(String tweet) {
        twitterClient.postTweet(tweet, new JsonHttpResponseHandler() {

            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                throwable.printStackTrace();
                Log.d("jenda", errorResponse.toString());
                Log.d("jenda", "statusCode: " + statusCode);
                notifyFailure(R.string.posting_tweet_failed);
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("jenda", response.toString());
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    notifyFailure(R.string.posting_tweet_failed);
                    return;
                }

                Tweet tweet = modelSerializer.tweetFromJson(response);

                for(OnApiFinishedListener l: listeners) {
                    l.onTweetPosted(tweet);
                }
            }
        });
    }

    class State {
        int page;
        boolean isFetching = false;
        Date lastAttemptToFetch = null;
        boolean hasMore = true;
        boolean isRateLimited = false;
    }

    private final State state = new State();

    public void fetchMore() {
        fetchMoreInternal();
    }

    public void fetchTweets() {
        if (state.isFetching) {
            return;
        }
        state.page = 0;
        state.isRateLimited = false;
        fetchMoreInternal();
    }

    private void fetchMoreInternal() {
        if (state.isFetching) {
            return;
        }
        if (state.isRateLimited) {
            long nowInMs = new Date().getTime();
            long lastAttemptToFetchInMs = state.lastAttemptToFetch.getTime();
            if (nowInMs < (lastAttemptToFetchInMs + 60 * 1000)) {
                // Rate limit.
                Log.d("jenda", "rate-limiting");
                return;
            }

            Log.d("jenda", "ratelimit-passed-trying again");
            // Or just pass through
            state.isRateLimited = false;
        }
        state.isFetching = true;
        state.lastAttemptToFetch = new Date();

        final int currentPageToFetch = state.page;

        fetchConcreteData(currentPageToFetch, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "timeline: " + jsonArray.toString());

                try {
                    if (statusCode != HttpURLConnection.HTTP_OK) {

                        Log.d("jenda", "statusCode: " + statusCode);
                        if (statusCode == HTTP_TOO_MANY_REQUESTS) {
                            notifyRateLimitError();
                            return;
                        }
                        notifyFailure(R.string.fetching_timeline_failed);
                        return;
                    }
                    List<Tweet> tweets = modelSerializer.tweetsFromJson(jsonArray);


                    Log.d("DEBUG", "tweets: " + tweets.size());
                    for (OnApiFinishedListener l : listeners) {
                        l.onTimeLineFetched(currentPageToFetch, tweets);
                    }

                    state.hasMore = tweets.size() != 0;
                    state.page++;
                } finally {
                    state.isFetching = false;
                }

            }
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                try {
                    if (errorResponse == null) {
                        notifyFailure(R.string.fetching_failed_maybe_offline);
                        return;
                    }
                    throwable.printStackTrace();
                    Log.d("jenda", errorResponse.toString());

                    ErrorResponse errRes = modelSerializer.errorResponseFromJson(errorResponse);

                    if (errRes.errors != null) {
                        for(ErrorResponse.Error err: errRes.errors) {
                            if (err.code == TWITTER_RATE_LIMIT_CODE) {
                                notifyRateLimitError();
                                return;
                            }
                        }
                    }

                    notifyFailure(R.string.fetching_timeline_failed);
                } finally {
                    state.isFetching = false;
                }
            }
        });
    }

    protected abstract void fetchConcreteData(int page, AsyncHttpResponseHandler handler);

    private void notifyRateLimitError() {
        state.isRateLimited = true;
        notifyFailure(R.string.rate_limit_exceeded_please_wait);
    }

    private void notifyFailure(@StringRes int messageRes) {
        for(OnApiFinishedListener l: listeners) {
            l.onFailure(messageRes);
        }
    }
}
