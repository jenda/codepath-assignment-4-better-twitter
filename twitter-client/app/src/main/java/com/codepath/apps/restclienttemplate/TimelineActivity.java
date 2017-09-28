package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jan_spidlen on 9/27/17.
 */

class TimelineActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterClient client = RestApplication.getRestClient();
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "timeline: " + jsonArray.toString());
                // Load json array into model classes
                ArrayList<Tweet> tweets = Tweet.fromJson(jsonArray);

            }
        });

    }

}
