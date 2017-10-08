package com.codepath.apps.bluebirdone.presenters;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.activities.BaseBlueBirdOneActivity;
import com.codepath.apps.bluebirdone.activities.TimelineActivity;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.User;
import com.codepath.apps.bluebirdone.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

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
        });
    }
}
