package com.codepath.apps.bluebirdone.models;

import android.util.Log;

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
    @SerializedName("favorite_count")
    public int favouritesCount;

    @Column
    @SerializedName("id_str")
    public String idStr;

    @SerializedName("entities")
    Entity entity;

    @SerializedName("retweeted")
    public boolean retweeted;

    @SerializedName("favorited")
    public boolean favorited;

    @SerializedName("in_reply_to_status_id")
    public Long inReplyToStatusId;

    @SerializedName("retweeted_status")
    public Object retweetedStatus;

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

    public void updateFrom(Tweet t) {
        Log.d("jenda", "update " + t.id);
        this.favorited = t.favorited;
        this.retweeted = t.retweeted;
        this.retweetedStatus = t.retweetedStatus;
        this.user = t.user;
    }
}
