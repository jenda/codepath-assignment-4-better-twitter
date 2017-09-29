package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;

import java.util.ArrayList;


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
    @Column
    public String userHandle;
    @Column
    public String timestamp;
    @Column
    @SerializedName("text")
    public String text;

    public Tweet() {}

//    public Tweet(JSONObject object){
//        super();
//
////        try {
////            this.user = User.parseUser(object.getJSONObject("user"));
//////            this.userHandle = object.getString("user_username");
//////            this.timestamp = object.getString("timestamp");
////            this.text = object.getString("text");
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//    }

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
