package com.codepath.apps.bluebirdone.utils;

import android.util.Log;

import com.codepath.apps.bluebirdone.models.Tweet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jan_spidlen on 9/30/17.
 */

public class SmartTweetArray extends ArrayList<Tweet> {

    Map<String, Tweet> tweets = new HashMap<>();

    @Override
    public boolean addAll(Collection<? extends Tweet> c) {
        for(Tweet t: c) {
            this.add(t);
        }
        return true;
    }

    @Override
    public void add(int i, Tweet e) {
        if (!tweets.containsKey(e.idStr)) {
            tweets.put(e.idStr, e);
            super.add(i, e);
        }
    }

    public boolean add(Tweet e) {
        if (!tweets.containsKey(e.idStr)) {
            tweets.put(e.idStr, e);
            return super.add(e);
        }
        return true;
    }

    public void clear() {
        super.clear();
        tweets.clear();
    }

    public void updateOrInsertTweet(Tweet t) {
//        Log.d("jenda", "MAP " + tweets);
        if(tweets.containsKey(t.idStr)) {
            Tweet currentTweet = tweets.get(t.idStr);
            currentTweet.updateFrom(t);
//            Log.d("jenda", "updated tweet " + t.idStr);
        } else {
            this.add(t);
        }
    }
}
