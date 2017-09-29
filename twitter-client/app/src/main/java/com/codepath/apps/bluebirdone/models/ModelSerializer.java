package com.codepath.apps.bluebirdone.models;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by jan_spidlen on 9/29/17.
 */

public class ModelSerializer {

    private final Gson gson;

    @Inject
    public ModelSerializer(Gson gson) {
        this.gson = gson;
    }

    public Tweet tweetFromJson(JSONObject jsonObject) {
        return Tweet.fromJson(jsonObject, gson);
    }

    public ArrayList<Tweet> tweetsFromJson(JSONArray jsonArray) {
        final ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tweets.add(tweetFromJson(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tweets;
    }
}
