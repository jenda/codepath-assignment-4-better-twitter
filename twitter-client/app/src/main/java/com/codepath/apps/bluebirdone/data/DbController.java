package com.codepath.apps.bluebirdone.data;

import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.models.User;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by jan_spidlen on 10/1/17.
 */

@Singleton
public class DbController {

    @Inject
    public DbController() {}

    public void saveTweet(Tweet tweet) {
        tweet.save();
    }

    public void saveTweets(List<Tweet> tweets) {
        for(Tweet t: tweets) {
            saveTweet(t);
        }
    }

    public List<Tweet> loadTweets() {
        List<Tweet> tweets = SQLite.select().from(Tweet.class).queryList();
        Collections.sort(tweets, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet o1, Tweet o2) {
                long time1 = new Date(o1.createdAt).getTime();
                long time2 = new Date(o2.createdAt).getTime();
                if (time1 == time2) {
                    return 0;
                } else if (time1 < time2) {
                    return 1;
                }
                return -1;
            }
        });

        return tweets;
    }

    public Map<Long, User> loadUsersAsMap() {
        Map<Long, User> map = new HashMap<>();
        for (User u: loadUsers() ) {
            map.put(u.id, u);
        }
        return map;
    }

    public List<User> loadUsers() {
        return select().from(User.class).queryList();
    }

    public void clearTweets() {
        Delete.table(Tweet.class);
        Delete.table(User.class);
    }
}
