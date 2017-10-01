package com.codepath.apps.bluebirdone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.adapters.TweetAdapter;
import com.codepath.apps.bluebirdone.dialogs.PostTweetDialog;
import com.codepath.apps.bluebirdone.models.CurrentUser;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends BaseBlueBirdOneActivity {

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;

    @BindView(R.id.tweets_recycler_view)
    RecyclerView tweetsRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    List<Tweet> tweets = new ArrayList<>();
    TweetAdapter tweetAdapter;

    @Inject
    PostTweetDialog postTweetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getNetComponent().inject(this);
        tweetAdapter = new TweetAdapter(tweets, this);

        setUpToolbar();
        setupSwipeRefreshContained();

        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetsRecyclerView.setAdapter(tweetAdapter);

        fetchTimeLine();
//        twitterClient.getHomeTimeline(1, new JsonHttpResponseHandler() {
//            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
//                Log.d("DEBUG", "timeline: " + jsonArray.toString());
//                // Load json array into model classes
//                tweets.clear();
//                tweets.addAll(modelSerializer.tweetsFromJson(jsonArray));
//                tweetAdapter.notifyDataSetChanged();
//
//                if (swipeContainer.isRefreshing()) {
//                    swipeContainer.setRefreshing(false);
//                }
//
//            }
//        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
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
    }

    private void setupSwipeRefreshContained() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("jenda", "on refresh");
                fetchTimeLine();
            }
        });

        // Colors.
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    //////////////////////////
    ///// OnClick handlers
    //////////////////////////

    @OnClick(R.id.fab)
    protected void fabClicked() {
        Log.d("jenda", "fab clicked");
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

    private void fetchTimeLine() {
        twitterClient.getHomeTimeline(0, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "timeline: " + jsonArray.toString());
                // Load json array into model classes
                tweets.clear();
                tweets.addAll(modelSerializer.tweetsFromJson(jsonArray));
                tweetAdapter.notifyDataSetChanged();

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }

            }
        });
    }

}
