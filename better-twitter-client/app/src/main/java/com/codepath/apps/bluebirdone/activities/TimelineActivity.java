package com.codepath.apps.bluebirdone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.SampleFragmentPagerAdapter;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.adapters.TweetAdapter;
import com.codepath.apps.bluebirdone.data.DbController;
import com.codepath.apps.bluebirdone.dialogs.PostTweetDialog;
import com.codepath.apps.bluebirdone.models.CurrentUser;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.twitter.DataConnector;
import com.codepath.apps.bluebirdone.utils.SmartTweetArray;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.bluebirdone.R.id.swipeContainer;

public class TimelineActivity extends BaseBlueBirdOneActivity {

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;

//    @BindView(R.id.tweets_recycler_view)
//    RecyclerView tweetsRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
//    @BindView(R.id.swipeContainer)
//    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.outerLayout)
    View outerLayout;

//    List<Tweet> tweets = new SmartTweetArray();
//    TweetAdapter tweetAdapter;

    @Inject
    PostTweetDialog postTweetDialog;
    @Inject
    DataConnector dataConnector;
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
//        setupSwipeRefreshContained();

        getAppComponent().inject(this);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
//        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        tweetAdapter = new TweetAdapter(tweets, this);
//        tweetsRecyclerView.setAdapter(tweetAdapter);

        // Load tweets from DB if there is a issue.
//        List<Tweet> tweetsFromDb = dbController.loadTweets();
//        if (tweetsFromDb != null && !tweetsFromDb.isEmpty()) {
//            tweets.addAll(tweetsFromDb);
//            tweetAdapter.notifyDataSetChanged();
//        }
//
//        dataConnector.addOnApiFinishedListener(this);
//        dataConnector.fetchTimeLine();
//
//        tweetsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
//                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
//                if (tweets.size() != 0
//                        && lastVisibleItemPosition != RecyclerView.NO_POSITION
//                        && lastVisibleItemPosition >= tweets.size() - 3) {
//                    Log.d("jenda", "request more");
//                    dataConnector.fetchMore();
//                }
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
        getSupportActionBar().setTitle("");
    }

//    private void setupSwipeRefreshContained() {
//        swipeContainer.setOnRefreshListener(() -> {
//                Log.d("jenda", "on refresh");
//                dataConnector.fetchTimeLine();
//            });
//
//        // Colors.
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//    }

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

//    @Override
//    public void onTweetPosted(Tweet tweet) {
//        // TODO: Refactor.
//        dbController.saveTweet(tweet);
//        tweets.add(0, tweet);
//        tweetAdapter.notifyDataSetChanged();
//
//        LinearLayoutManager layoutManager = (LinearLayoutManager)tweetsRecyclerView.getLayoutManager();
//        layoutManager.scrollToPosition(0);
//    }
//
//    @Override
//    public void onFailure(@StringRes int messageRes) {
//        final Snackbar snackbar = Snackbar.make(outerLayout,
//                getApplicationContext().getText(messageRes),
//                Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction(R.string.ok, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackbar.dismiss();
//            }
//        });
//        snackbar.show();
//
//        if (swipeContainer.isRefreshing()) {
//            swipeContainer.setRefreshing(false);
//        }
//    }
//
//    @Override
//    public void onTimeLineFetched(int page, List<Tweet> tweets) {
//        Log.d("jenda", "onTimeLineFetched " + tweets.size());
//        if (page == 0) {
//            // Clear all saved tweets since this is a fresh load.
//            Log.d("jenda", "clearing tweets");
//            tweetAdapter.clear();
//        }
//
//        tweetAdapter.addAll(tweets);
//        tweetAdapter.notifyDataSetChanged();
//
//        dbController.clearTweets();
//        dbController.saveTweets(tweets);
//
//        if (swipeContainer.isRefreshing()) {
//            swipeContainer.setRefreshing(false);
//        }
//    }
}
