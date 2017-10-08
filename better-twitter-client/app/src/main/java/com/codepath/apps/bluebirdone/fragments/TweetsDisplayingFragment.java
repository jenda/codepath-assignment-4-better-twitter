package com.codepath.apps.bluebirdone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.activities.TimelineActivity;
import com.codepath.apps.bluebirdone.adapters.TweetAdapter;
import com.codepath.apps.bluebirdone.data.DbController;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.twitter.CurrentUserMentionsDataConnector;
import com.codepath.apps.bluebirdone.twitter.DataConnector;
import com.codepath.apps.bluebirdone.twitter.HomeTimelineDataConnector;
import com.codepath.apps.bluebirdone.twitter.UserTimelineDataConnector;
import com.codepath.apps.bluebirdone.utils.SmartTweetArray;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 10/6/17.
 */

public class TweetsDisplayingFragment extends BaseFragment  implements DataConnector.OnApiFinishedListener{

    public enum Type {
        HOME_TIMELINE,
        USER_MENTIONS,
        USER_TWEETS
    }

    @Inject
    ModelSerializer modelSerializer;


    DataConnector dataConnector;

    @Inject
    HomeTimelineDataConnector homeTimelineDataConnector;
    @Inject
    UserTimelineDataConnector userTimelineDataConnector;
    @Inject
    CurrentUserMentionsDataConnector currentUserMentionsDataConnector;

    @Inject
    DbController dbController;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.tweets_recycler_view)
    RecyclerView tweetsRecyclerView;


    List<Tweet> tweets = new SmartTweetArray();
    TweetAdapter tweetAdapter;


    @BindView(R.id.outer_layout)
    View outerLayout;

    private static final String TYPE_ARG = "type";
    private static final String USER_ID_ARG = "user_id";
    private Type type;
    @Nullable
    private Long userId;

    public static TweetsDisplayingFragment newInstance(Type type) {
        return newInstance(type, null);
    }

    public static TweetsDisplayingFragment newInstance(Type type, @Nullable Long userId) {
        Bundle args = new Bundle();
        args.putSerializable(TYPE_ARG, type);
        if (userId != null) {
            args.putLong(USER_ID_ARG, userId);
        }
        TweetsDisplayingFragment fragment = new TweetsDisplayingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (Type) getArguments().getSerializable(TYPE_ARG);
        userId = getArguments().getLong(USER_ID_ARG);

        getComponent().inject(this);

        if (type == Type.HOME_TIMELINE) {
            dataConnector = homeTimelineDataConnector;
        } else if (type == Type.USER_MENTIONS) {
            dataConnector = currentUserMentionsDataConnector;
        } else {
            userTimelineDataConnector.setUserId(userId);
            dataConnector = userTimelineDataConnector;
        }

        Log.d("jenda", " onCreate " + (dataConnector == null));
        Log.d("jenda", " onCreate " + type);
    }

    private void setupSwipeRefreshContained() {
        swipeContainer.setOnRefreshListener(() -> {
            Log.d("jenda", "on refresh");
            dataConnector.fetchTweets();
        });

        // Colors.
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_timeline, container, false);

        ButterKnife.bind(this, view);


        tweetsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        tweetAdapter = new TweetAdapter(tweets, this.getActivity(),
                ((TimelineActivity)this.getActivity()).presenter);
        tweetsRecyclerView.setAdapter(tweetAdapter);


        // Load tweets from DB if there is a issue.
        List<Tweet> tweetsFromDb = dbController.loadTweets();
        if (tweetsFromDb != null && !tweetsFromDb.isEmpty()) {
            tweets.addAll(tweetsFromDb);
            tweetAdapter.notifyDataSetChanged();
        }

        dataConnector.addOnApiFinishedListener(this);
        dataConnector.fetchTweets();

        tweetsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (tweets.size() != 0
                        && lastVisibleItemPosition != RecyclerView.NO_POSITION
                        && lastVisibleItemPosition >= tweets.size() - 3) {
                    Log.d("jenda", "request more");
                    dataConnector.fetchMore();
                }
            }
        });

        setupSwipeRefreshContained();

        return view;
    }


    @Override
    public void onTweetPosted(Tweet tweet) {
        // TODO: Refactor.
        dbController.saveTweet(tweet);
        tweets.add(0, tweet);
        tweetAdapter.notifyDataSetChanged();

        LinearLayoutManager layoutManager = (LinearLayoutManager)tweetsRecyclerView.getLayoutManager();
        layoutManager.scrollToPosition(0);
    }

    @Override
    public void onFailure(@StringRes int messageRes) {
        final Snackbar snackbar = Snackbar.make(outerLayout,
                this.getActivity().getApplicationContext().getText(messageRes),
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onTimeLineFetched(int page, List<Tweet> tweets) {
        Log.d("jenda", "onTimeLineFetched " + tweets.size());
        if (page == 0) {
            // Clear all saved tweets since this is a fresh load.
            Log.d("jenda", "clearing tweets");
            tweetAdapter.clear();
        }

        tweetAdapter.addAll(tweets);
        tweetAdapter.notifyDataSetChanged();

        dbController.clearTweets();
        dbController.saveTweets(tweets);

        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
    }
}
