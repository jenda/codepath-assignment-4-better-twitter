package com.codepath.apps.restclienttemplate.dagger;

import com.codepath.apps.restclienttemplate.activities.TimelineActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jan_spidlen on 9/29/17.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(TimelineActivity activity);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}
