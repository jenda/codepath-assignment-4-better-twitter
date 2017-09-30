package com.codepath.apps.bluebirdone.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
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
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

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

        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetsRecyclerView.setAdapter(tweetAdapter);

        twitterClient.getHomeTimeline(1, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", "timeline: " + jsonArray.toString());
                // Load json array into model classes
                tweets.clear();
                tweets.addAll(modelSerializer.tweetsFromJson(jsonArray));
                tweetAdapter.notifyDataSetChanged();

            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);

        return true;
    }

    @OnClick(R.id.fab)
    protected void fabClicked() {
        Log.d("jenda", "fab clicked");
        FragmentManager fm = getSupportFragmentManager();
//        PostTweetDialog postTweetDialog = PostTweetDialog.newInstance("Some Title");
        postTweetDialog.show(fm, PostTweetDialog.class.getName());
    }

}
