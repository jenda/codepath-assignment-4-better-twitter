package com.codepath.apps.bluebirdone.models;

import com.codepath.apps.bluebirdone.MyDatabase;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by jan_spidlen on 9/30/17.
 */

public class Media extends BaseModel {

    @SerializedName("type")
    public String type;

    @SerializedName("media_url_https")
    public String mediaUrlHttps;
}
