package com.codepath.apps.bluebirdone.dagger;

import com.codepath.apps.bluebirdone.activities.TimelineActivity;
import com.codepath.apps.bluebirdone.dialogs.PostTweetDialog;
import com.codepath.apps.bluebirdone.fragments.HomeTimelineFragment;
import com.codepath.apps.bluebirdone.fragments.UserMentionsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jan_spidlen on 9/29/17.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(TimelineActivity activity);
    void inject(PostTweetDialog postTweetDialog);
    void inject(HomeTimelineFragment fragment);
    void inject(UserMentionsFragment fragment);
}
