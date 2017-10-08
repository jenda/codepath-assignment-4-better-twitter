package com.codepath.apps.bluebirdone.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.codepath.apps.bluebirdone.dialogs.PostTweetDialog;
import com.codepath.apps.bluebirdone.fragments.UserProfileFragment;
import com.codepath.apps.bluebirdone.models.CurrentUser;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.models.User;
import com.codepath.apps.bluebirdone.presenters.TweetPresenter;
import com.codepath.apps.bluebirdone.twitter.CurrentUserMentionsDataConnector;
import com.codepath.apps.bluebirdone.twitter.DataConnector;
import com.codepath.apps.bluebirdone.twitter.HomeTimelineDataConnector;
import com.codepath.apps.bluebirdone.twitter.UserTimelineDataConnector;
import com.codepath.apps.bluebirdone.utils.Utils;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends BaseBlueBirdOneActivity implements DataConnector.LoaderListener {

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

    @Inject
    UserTimelineDataConnector userTimelineDataConnector;
    @Inject
    HomeTimelineDataConnector homeTimelineDataConnector;
    @Inject
    CurrentUserMentionsDataConnector currentUserMentionsDataConnector;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

//    @BindView(R.id.materialViewPager)
//    MaterialViewPager materialViewPager;

    ProgressDialog progressDialog;

    private void prepareProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_timeline);

        setUpToolbar();

        getAppComponent().inject(this);
        viewPager.setAdapter(new BlueBirdOnePagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
//        materialViewPager.getPagerTitleStrip().setViewPager(viewPager);

        prepareProgressDialog();
        userTimelineDataConnector.addLoaderListener(this);
        homeTimelineDataConnector.addLoaderListener(this);
        currentUserMentionsDataConnector.addLoaderListener(this);

        if (!Utils.isNetworkAvailable(this)) {
            showError(R.string.network_not_available);
        }
    }

    void showError(@StringRes int messageRes) {
        final Snackbar snackbar = Snackbar.make(outerLayout,
                this.getApplicationContext().getText(messageRes),
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
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
                Log.d("jenda", "timeline: " + jsonObject.toString());
                try {
                    Log.d("jenda", "following: " + jsonObject.get("following"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Load json array into model classes

                showUserProfileFragment(modelSerializer.userFromJson(jsonObject));
            }

            public void onFailure(int statusCode,
                                  Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();
            }
        });
    }

    public void showUserProfileFragment(User user) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.user = user;
        final FragmentManager fragmentManager = getSupportFragmentManager();


        userProfileFragment.show(fragmentManager, UserProfileFragment.class.getName());
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
        showPostTweetDialog(null);
    }

    public void showPostTweetDialog(Tweet replyTo) {

        final FragmentManager fm = getSupportFragmentManager();
        postTweetDialog.setReplyToTweet(replyTo);
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

    @Override
    public void onDataLoadStarted() {
        progressDialog.show();
    }

    @Override
    public void onDataLoadFinished() {
        progressDialog.dismiss();
    }
}
