package com.codepath.apps.bluebirdone.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.models.CurrentUser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by jan_spidlen on 9/29/17.
 */

@Module
public class NetModule {

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }


    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        return client;
    }

    @Provides
    @Singleton
    TwitterClient provideTwitterClient(Application application) {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, application);
    }


//    @Provides
//    @Singleton
//    CurrentUser provideCurrentUser(TwitterClient twitterClient) {
//        return (CurrentUser) twitterClient.getHomeTimeline();
//    }
}
