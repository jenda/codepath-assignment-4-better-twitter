package com.codepath.apps.bluebirdone.models;

import com.codepath.apps.bluebirdone.MyDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by jan_spidlen on 9/29/17.
 */

@Table(database = MyDatabase.class)
@Parcel(analyze={User.class})
public class User extends BaseModel {

    @PrimaryKey
    @Column
    @SerializedName("id")
    public Long id;

    @Column
    @SerializedName("name")
    public String name;

    @Column
    @SerializedName("screen_name")
    public String screenName;

//    @Column
    @SerializedName("id_str")
    public String stringId;

    @Column
    @SerializedName("profile_image_url")
    public String profileImageUrl;

//    @Column
    @SerializedName("profile_banner_url")
    public String profileBackgroundImageUrl;

    public static User parseUser(JSONObject jsonObject, Gson gson) {
        return gson.fromJson(jsonObject.toString(), User.class);
    }

    public String getHandle() {
        return "@" + screenName;
    }
}
