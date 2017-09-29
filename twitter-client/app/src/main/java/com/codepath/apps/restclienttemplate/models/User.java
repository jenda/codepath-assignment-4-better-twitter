package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jan_spidlen on 9/29/17.
 */

//@Table(database = MyDatabase.class)
public class User {
    @Column

    @SerializedName("name")
    public String name;
    @Column
    String id_str;

    public static User parseUser(JSONObject jsonObject) throws JSONException {

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonObject.toString(), User.class);
//        User user = new User();
//        user.name = jsonObject.getString("name");
//        return user;
    }
}
