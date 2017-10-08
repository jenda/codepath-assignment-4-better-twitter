package com.codepath.apps.bluebirdone.presenters;

import android.content.Context;

import com.codepath.apps.bluebirdone.activities.BaseBlueBirdOneActivity;
import com.codepath.apps.bluebirdone.activities.TimelineActivity;
import com.codepath.apps.bluebirdone.models.User;

import javax.inject.Inject;

/**
 * Created by jan_spidlen on 10/7/17.
 */

public class TweetPresenter {

//    @Inject
    TimelineActivity activity;


    @Inject
    public TweetPresenter() {}

    public void onUserClicked(User user) {
        activity.showUserProfileFragment(user);
    }

    public void attachActivity(TimelineActivity activity) {
        this.activity = activity;
    }
}
