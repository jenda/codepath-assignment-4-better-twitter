package com.codepath.apps.bluebirdone.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.bluebirdone.BlueBirdOneApplication;
import com.codepath.apps.bluebirdone.dagger.AppComponent;

import butterknife.ButterKnife;

/**
 * Basic abstract activity class to contain the most common methods.
 */
public abstract class BaseBlueBirdOneActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public AppComponent getAppComponent() {
        return ((BlueBirdOneApplication) getApplication()).getAppComponent();
    }
}
