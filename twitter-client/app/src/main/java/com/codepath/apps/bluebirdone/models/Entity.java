package com.codepath.apps.bluebirdone.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jan_spidlen on 9/30/17.
 */

public class Entity {

    @SerializedName("media")
    public List<Media> media;
}
