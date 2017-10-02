package com.codepath.apps.bluebirdone.models;

import com.codepath.apps.bluebirdone.MyDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcel;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;


/**
 * Created by jan_spidlen on 9/27/17.
 */

@Table(database = MyDatabase.class)
@Parcel(analyze={User.class})
public class Tweet extends BaseModel {
    // Define database columns and associated fields
    @PrimaryKey
    @Column
    @SerializedName("id")
    public Long id;

    @Column
    @ForeignKey(saveForeignKeyModel = true)
    @SerializedName("user")
    public User user;

    @Column
    @SerializedName("text")
    public String text;

    @Column
    @SerializedName("created_at")
    public String createdAt;

    @Column
    @SerializedName("retweet_count")
    public int retweetCount;

    @Column
    @SerializedName("favourites_count")
    public int favouritesCount;

    @Column
    @SerializedName("id_str")
    public String idStr;

//    @Column
    @SerializedName("entities")
    Entity entity;

    public Tweet() {}


    public Media getPhoto() {
        if (entity != null && entity.media != null) {
//            Log.d("jenda", "media: " + entity.media.size());
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

//    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
//        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
//
//        for (int i=0; i < jsonArray.length(); i++) {
//            JSONObject tweetJson = null;
//            try {
//                tweetJson = jsonArray.getJSONObject(i);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//
//            Gson gson = new GsonBuilder().create();
//            Tweet tweet = gson.fromJson(tweetJson.toString(), Tweet.class);
//            tweet.save();
//            tweets.add(tweet);
//        }
//
//        return tweets;
//    }
}
