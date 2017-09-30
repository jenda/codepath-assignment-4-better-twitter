package com.codepath.apps.bluebirdone.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.models.CurrentUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jan_spidlen on 9/29/17.
 */

public class PostTweetDialog extends BaseBlueBirdOneDialog {

    private static final String TITLE_ARG = "title";

    @BindView(R.id.user_full_name)
    TextView userFullNameTextView;
    @BindView(R.id.user_handle)
    TextView userHandleTextView;
    @BindView(R.id.tweet_text)
    EditText tweetTextEditText;
    @BindView(R.id.remaining_chars_count)
    TextView reamingCharsCountTextView;

    @BindView(R.id.user_profile_image)
    ImageView currentUserImageView;

    @Inject
    TwitterClient twitterClient;
//    @Inject
    public CurrentUser currentUser;

    @Inject
    public PostTweetDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_tweet, container);

        getComponent().inject(this);
        ButterKnife.bind(this, view);

        userFullNameTextView.setText(currentUser.name);
        userHandleTextView.setText(currentUser.getHandle());

        Glide.with(this)
                .load(currentUser.profileImageUrl)
                .into(currentUserImageView);

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


    @OnClick(R.id.close_dialog)
    protected void closeDialog() {
        this.dismiss();
    }


}
