package com.codepath.apps.bluebirdone.presenters;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.activities.BaseBlueBirdOneActivity;
import com.codepath.apps.bluebirdone.activities.TimelineActivity;
import com.codepath.apps.bluebirdone.adapters.TweetAdapter;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.models.User;
import com.codepath.apps.bluebirdone.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jan_spidlen on 10/7/17.
 */

public class TweetPresenter {

//    @Inject
    TimelineActivity activity;

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;
    private TweetAdapter tweetAdapter;


    @Inject
    public TweetPresenter() {}

    public void onUserClicked(User user) {
        activity.showUserProfileFragment(user);
    }

    public void attachActivity(TimelineActivity activity) {
        this.activity = activity;
    }

    public void handleClicked(String handle) {
        activity.onDataLoadStarted();
        twitterClient.getUserInfo(Utils.toScreenName(handle), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                try {
                    Log.d("jenda", "users: " + jsonArray);
                    List<User> users = modelSerializer.usersFromJson(jsonArray);
                    if (users != null && !users.isEmpty()) {
                        activity.showUserProfileFragment(users.get(0));
                    }
                } finally {
                    activity.onDataLoadFinished();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                throwable.printStackTrace();
                activity.onDataLoadFinished();
            }
        });
    }

    public void onRetweetClicked(final Tweet tweet) {

        JsonHttpResponseHandler retweetHandler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                try {
                    Log.d("jenda", "jsonObject: " + jsonObject);
                    Tweet newTweet = modelSerializer.tweetFromJson(jsonObject);
                    Log.d("jenda", "tweet.retweetedStatus " + tweet.retweetedStatus);

//                    tweetAdapter.addFirst(newTweet);
                    tweet.retweeted = newTweet.retweetedStatus != null;
                    tweetAdapter.updateOrInsertTweet(tweet);
                } finally {
                    activity.onDataLoadFinished();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
                activity.onDataLoadFinished();
            }
        };
        if (!tweet.retweeted) {
            twitterClient.retweet(tweet.id, retweetHandler);
        } else {
            twitterClient.unretweet(tweet.id, retweetHandler);
        }

    }

    public void onFavClicked(Tweet tweet) {
        Log.d("jenda", "faving " + tweet.id);
        if (tweet.favorited) {
            twitterClient.unfav(tweet.id, favHandler);
        } else {
            twitterClient.fav(tweet.id, favHandler);
        }
    }

    public void attachAdapter(TweetAdapter tweetAdapter) {
        this.tweetAdapter = tweetAdapter;
    }

    JsonHttpResponseHandler favHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
            try {
                Log.d("jenda", "jsonObject: " + jsonObject);
                Tweet tweet = modelSerializer.tweetFromJson(jsonObject);
                Log.d("jenda", "tweet.retweetedStatus " + tweet.retweetedStatus);
                tweet.retweeted = tweet.retweetedStatus != null;
                Log.d("jenda", "tweet.retweeted " + tweet.retweeted);
                tweetAdapter.updateOrInsertTweet(tweet);
            } finally {
                activity.onDataLoadFinished();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            throwable.printStackTrace();
            activity.onDataLoadFinished();
        }
    };
}
