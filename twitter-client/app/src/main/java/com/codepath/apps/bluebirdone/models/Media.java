package com.codepath.apps.bluebirdone.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by jan_spidlen on 9/30/17.
 */

public class Media {

    @SerializedName("type")
    public String type;

    @SerializedName("media_url_https")
    public String mediaUrlHttps;
}
