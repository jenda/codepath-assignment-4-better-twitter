package com.codepath.apps.bluebirdone.twitter;

import com.loopj.android.http.AsyncHttpResponseHandler;

import javax.inject.Inject;

/**
 * Created by jan_spidlen on 10/6/17.
 */

public class UserTimelineDataConnector extends DataConnector {

    public Long userId;

    @Inject
    public UserTimelineDataConnector() {}

    @Override
    protected void fetchConcreteData(int page, AsyncHttpResponseHandler handler) {
        twitterClient.getUserTimeline(page, handler, userId);
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
