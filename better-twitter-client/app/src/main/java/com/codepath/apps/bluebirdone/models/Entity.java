package com.codepath.apps.bluebirdone.models;

import com.codepath.apps.bluebirdone.MyDatabase;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by jan_spidlen on 9/30/17.
 */

public class Entity extends BaseModel {

    @SerializedName("media")
    public List<Media> media;
}
