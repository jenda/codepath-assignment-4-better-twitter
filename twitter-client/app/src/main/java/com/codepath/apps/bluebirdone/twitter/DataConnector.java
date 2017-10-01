package com.codepath.apps.bluebirdone.twitter;

import android.app.Application;
import android.support.annotation.StringRes;
import android.util.Log;

import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by jan_spidlen on 9/30/17.
 */

@Singleton
public class DataConnector {

    public interface OnApiFinishedListener {
        void onTweetPosted(Tweet tweet);
        void onFailure(@StringRes int messageRes);
    }

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;
    @Inject
    Application app;

    Set<OnApiFinishedListener> listeners = new HashSet<>();

    @Inject
    public DataConnector() {}

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

    private void notifyFailure(@StringRes int messageRes) {
        for(OnApiFinishedListener l: listeners) {
            l.onFailure(messageRes);
        }
    }
}
