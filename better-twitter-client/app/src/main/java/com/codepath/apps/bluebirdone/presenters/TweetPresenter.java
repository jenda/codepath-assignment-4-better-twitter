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

    public void onRetweetClicked(Tweet tweet) {
        twitterClient.retweet(tweet.id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                try {
                    Log.d("jenda", "jsonObject: " + jsonObject);
                    Tweet tweet = modelSerializer.tweetFromJson(jsonObject);
                    tweetAdapter.updateOrInsertTweet(tweet);
//                    tweet
                } finally {
                    activity.onDataLoadFinished();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
                activity.onDataLoadFinished();
            }
        });

    }

    public void onFavClicked(Tweet tweet) {
        Log.d("jenda", "faving " + tweet.id);
        if (tweet.favorited) {
            twitterClient.unfav(tweet.id, handler);
        } else {
            twitterClient.fav(tweet.id, handler);
        }
    }

    public void attachAdapter(TweetAdapter tweetAdapter) {
        this.tweetAdapter = tweetAdapter;
    }

    JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
            try {
                Log.d("jenda", "jsonObject: " + jsonObject);
                Tweet tweet = modelSerializer.tweetFromJson(jsonObject);
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
