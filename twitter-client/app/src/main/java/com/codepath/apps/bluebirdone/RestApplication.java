package com.codepath.apps.bluebirdone;

import com.codepath.apps.bluebirdone.dagger.AppModule;
import com.codepath.apps.bluebirdone.dagger.DaggerNetComponent;
import com.codepath.apps.bluebirdone.dagger.NetComponent;
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
 *     TwitterClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class RestApplication extends Application {
	private static Context context;

    private NetComponent netComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		RestApplication.context = this;

		Stetho.initializeWithDefaults(this);

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
	}

    public NetComponent getNetComponent() {
        return netComponent;
    }

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, RestApplication.context);
	}
}