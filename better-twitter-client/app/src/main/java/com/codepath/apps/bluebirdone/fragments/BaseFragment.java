package com.codepath.apps.bluebirdone.fragments;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.codepath.apps.bluebirdone.activities.BaseBlueBirdOneActivity;
import com.codepath.apps.bluebirdone.dagger.AppComponent;

/**
 * Created by jan_spidlen on 10/6/17.
 */

public abstract class BaseFragment extends Fragment {
    protected AppComponent getComponent() {
        return ((BaseBlueBirdOneActivity)getActivity()).getAppComponent();
    }
}
