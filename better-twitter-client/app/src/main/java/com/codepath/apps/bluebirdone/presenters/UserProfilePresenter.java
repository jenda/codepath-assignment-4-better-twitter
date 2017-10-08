package com.codepath.apps.bluebirdone.presenters;

import android.util.Log;

import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.fragments.UserProfileFragment;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import javax.inject.Inject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jan_spidlen on 10/8/17.
 */

public class UserProfilePresenter {

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;

    private UserProfileFragment userProfileFragment;

    @Inject
    public UserProfilePresenter() {}

    public void followOrUnfollowUser(User user) {
        Log.d("jenda", "followed button clicked");
        final JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("jenda", "follow/unfollow " + jsonObject.toString());
                User newUser = modelSerializer.userFromJson(jsonObject);
                // Twitter API: is so fucking dumb.
                newUser.following = !newUser.following;
                userProfileFragment.setUser(newUser);
                userProfileFragment.refreshTheFollowButton();
            }

            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        };
        if (user.following) {
            Log.d("jenda", "unfollow");
            twitterClient.unfollow(user.id, responseHandler);
        } else {
            Log.d("jenda", "follow");
            twitterClient.follow(user.id, responseHandler);
        }
    }

    public void setCurrentUserProfileFragment(UserProfileFragment userProfileFragment) {
        this.userProfileFragment = userProfileFragment;
    }
}
