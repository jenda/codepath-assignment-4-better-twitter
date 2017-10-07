package com.codepath.apps.bluebirdone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.BlueBirdOnePagerAdapter;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.data.DbController;
import com.codepath.apps.bluebirdone.dialogs.PostTweetDialog;
import com.codepath.apps.bluebirdone.fragments.CurrentUserProfileFragment;
import com.codepath.apps.bluebirdone.models.CurrentUser;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.twitter.DataConnector;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends BaseBlueBirdOneActivity {

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.timeline_activity_outer_layout)
    View outerLayout;

    @Inject
    PostTweetDialog postTweetDialog;
//    @Inject
//    DataConnector dataConnector;
    @Inject
    DbController dbController;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_timeline);

        setUpToolbar();

        getAppComponent().inject(this);
        viewPager.setAdapter(new BlueBirdOnePagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                // Do whatever you want to do on logout click.
                Log.d("jenda", "logout ");
                twitterClient.clearAccessToken();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.show_profile:
                Log.d("jenda", "show_profile");
                showCurrentUserProfileFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCurrentUserProfileFragment() {

        twitterClient.getCurrentUser(new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("DEBUG", "timeline: " + jsonObject.toString());
                // Load json array into model classes

                CurrentUserProfileFragment currentUserProfileFragment = new CurrentUserProfileFragment();
                currentUserProfileFragment.currentUser = modelSerializer.currentUserFromJson(jsonObject);
                final FragmentManager fragmentManager = getSupportFragmentManager();


                currentUserProfileFragment.show(fragmentManager, CurrentUserProfileFragment.class.getName());
//                fragmentManager
//                        .beginTransaction()
//                        .add(R.id.fragmentContainerLayout, currentUserProfileFragment)
//                        .commit();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

    //////////////////////////
    ///// setups
    //////////////////////////

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("");
    }

    //////////////////////////
    ///// OnClick handlers
    //////////////////////////

    @OnClick(R.id.fab)
    protected void fabClicked() {
        final FragmentManager fm = getSupportFragmentManager();
        if (postTweetDialog.currentUser != null) {
            postTweetDialog.show(fm, PostTweetDialog.class.getName());
            return;
        }

        twitterClient.getCurrentUser(new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("DEBUG", "timeline: " + jsonObject.toString());
                // Load json array into model classes
                CurrentUser currentUser = modelSerializer.currentUserFromJson(jsonObject);
                postTweetDialog.currentUser = currentUser;
                postTweetDialog.show(fm, PostTweetDialog.class.getName());
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }
}
