package com.codepath.apps.bluebirdone.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 9/29/17.
 */

public class PostTweetDialog extends BaseBlueBirdOneDialog {

    private static final String TITLE_ARG = "title";

    @Inject
    TwitterClient twitterClient;

    @Inject
    public PostTweetDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_tweet, container);

        getComponent().inject(this);
        ButterKnife.bind(this, view);


        Log.d("jenda", "twitterClient is null: " + (twitterClient == null));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(R.string.post_tweet_placeholder);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


}
