package com.codepath.apps.bluebirdone.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jan_spidlen on 9/29/17.
 */

//@Table(database = MyDatabase.class)
public class User {

    @SerializedName("name")
    public String name;

    @SerializedName("screen_name")
    public String screenName;

    @SerializedName("id_str")
    public String stringId;

    public static User parseUser(JSONObject jsonObject) throws JSONException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonObject.toString(), User.class);
    }
}
