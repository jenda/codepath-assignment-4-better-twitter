package com.codepath.apps.bluebirdone.utils;

import com.codepath.apps.bluebirdone.models.Tweet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jan_spidlen on 9/30/17.
 */

public class SmartTweetArray extends ArrayList<Tweet> {

    Set<String> tweetIds = new HashSet<>();

    @Override
    public boolean addAll(Collection<? extends Tweet> c) {
        for(Tweet t: c) {
            this.add(t);
        }
        return true;
    }

    @Override
    public void add(int i, Tweet e) {
        if (tweetIds.add(e.idStr)) {
            super.add(i, e);
        }
    }


    public boolean add(Tweet e) {
        if (tweetIds.add(e.idStr)) {
            return super.add(e);
        }
        return true;
    }
}
