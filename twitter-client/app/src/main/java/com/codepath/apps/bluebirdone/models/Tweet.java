package com.codepath.apps.bluebirdone.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jan_spidlen on 9/27/17.
 */

//@Table(database = MyDatabase.class)
public class Tweet /* extends BaseModel */{
    // Define database columns and associated fields
    @PrimaryKey
    @Column
    public Long id;
    @Column
    @SerializedName("user")
    public User user;

    @SerializedName("text")
    public String text;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("retweet_count")
    public int retweetCount;

    @SerializedName("favourites_count")
    public int favouritesCount;


    @SerializedName("entities")
    Entity entity;

    public Tweet() {}


    public Media getPhoto() {
        if (entity != null && entity.media != null) {
            Log.d("jenda", "media: " + entity.media.size());
            for (Media m : entity.media) {
                if (m.type.equals("photo")) {
                    return m;
                }
            }
        }
        return null;
    }


    public static Tweet fromJson(JSONObject jsonObject, Gson gson) {
        return gson.fromJson(jsonObject.toString(), Tweet.class);
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Gson gson = new GsonBuilder().create();
            Tweet tweet = gson.fromJson(tweetJson.toString(), Tweet.class);
//            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }
}
