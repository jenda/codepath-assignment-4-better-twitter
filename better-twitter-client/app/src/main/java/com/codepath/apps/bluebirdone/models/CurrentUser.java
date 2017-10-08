package com.codepath.apps.bluebirdone.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jan_spidlen on 9/29/17.
 */

@Deprecated
public class CurrentUser {

    @SerializedName("name")
    public String name;

    @SerializedName("screen_name")
    public String screen_name;

    @SerializedName("profile_image_url")
    public String profileImageUrl;

    @SerializedName("profile_banner_url")
    public String profileBackgroundImageUrl;

    @SerializedName("id")
    public long id;


    public static CurrentUser parseUser(JSONObject jsonObject, Gson gson) {
        return gson.fromJson(jsonObject.toString(), CurrentUser.class);
    }

    public String getHandle() {
        return "@" + screen_name;
    }
}
