package com.codepath.apps.bluebirdone;

import com.codepath.apps.bluebirdone.dagger.AppModule;
import com.codepath.apps.bluebirdone.dagger.DaggerAppComponent;
import com.codepath.apps.bluebirdone.dagger.AppComponent;
import com.codepath.apps.bluebirdone.dagger.NetModule;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import android.app.Application;
import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = BlueBirdOneApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class BlueBirdOneApplication extends Application {
	private static Context context;

    private AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		BlueBirdOneApplication.context = this;

		Stetho.initializeWithDefaults(this);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
	}

    public AppComponent getAppComponent() {
        return appComponent;
    }

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, BlueBirdOneApplication.context);
	}
}