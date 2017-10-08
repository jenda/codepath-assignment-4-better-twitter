package com.codepath.apps.bluebirdone.utils;

import android.text.format.DateUtils;

import com.codepath.apps.bluebirdone.models.Tweet;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by jan_spidlen on 9/29/17.
 */

public class Utils {

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static String formatLargeNumber(long number) {
        final int K = 1000;
        final int M = 1000000;
        if (number < K) {
            return "" + number;
        }
        double relativeNum = number;
        String suffix = "";
        if (number >= M) {
            relativeNum = relativeNum/M;
            suffix = "M";
        } else if (number >= K) {
            relativeNum = relativeNum/K;
            suffix = "K";
        }
        DecimalFormat df = new DecimalFormat("#.#");

        return String.format("%s %s", df.format(relativeNum), suffix);
    }

    public static void sortTweets(List<Tweet> tweets) {
        Collections.sort(tweets,(Tweet o1, Tweet o2) -> {
                long time1 = new Date(o1.createdAt).getTime();
                long time2 = new Date(o2.createdAt).getTime();
                if (time1 == time2) {
                    return 0;
                } else if (time1 < time2) {
                    return 1;
                }
                return -1;
            });
    }
}
