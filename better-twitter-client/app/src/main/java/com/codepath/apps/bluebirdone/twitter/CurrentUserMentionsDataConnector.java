package com.codepath.apps.bluebirdone.twitter;

import com.loopj.android.http.AsyncHttpResponseHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jan_spidlen on 10/6/17.
 */

@Singleton
public class CurrentUserMentionsDataConnector extends DataConnector {

    @Inject
    public CurrentUserMentionsDataConnector() {}

    @Override
    protected void fetchConcreteData(int page, AsyncHttpResponseHandler handler) {
        twitterClient.getUserMentions(page, handler);
    }
}
