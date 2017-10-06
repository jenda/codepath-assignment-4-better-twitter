package com.codepath.apps.bluebirdone.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by jan_spidlen on 10/1/17.
 */

public class ErrorResponse {

    public class Error {

        @SerializedName("code")
        public int code;

        @SerializedName("message")
        public String message;
    }


    @SerializedName("errors")
    public List<Error> errors;

    public static ErrorResponse fromJsonObject(JSONObject jsonObject, Gson gson) {
        return gson.fromJson(jsonObject.toString(), ErrorResponse.class);
    }
}
