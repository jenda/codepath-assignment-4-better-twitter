package com.codepath.apps.bluebirdone.dialogs;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.models.CurrentUser;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.Tweet;
import com.codepath.apps.bluebirdone.twitter.DataConnector;
import com.codepath.apps.bluebirdone.twitter.HomeTimelineDataConnector;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jan_spidlen on 9/29/17.
 */

public class PostTweetDialog extends BaseBlueBirdOneDialog implements DataConnector.OnApiFinishedListener {

    private static final String TITLE_ARG = "title";
    public static final String SAVED_TWEET_PREF = "savedTweet";

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

    @BindView(R.id.post_tweet_button)
    Button postTweetButton;

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;
    @Inject
    HomeTimelineDataConnector dataConnector;
    @Inject
    SharedPreferences sharedPreferences;

    public CurrentUser currentUser;
    private Tweet replyToTweet;

    @Inject
    public PostTweetDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_tweet, container);

        ButterKnife.bind(this, view);

        userFullNameTextView.setText(currentUser.name);
        userHandleTextView.setText(currentUser.getHandle());
        updateReamingCharsCount(getResources().getInteger(R.integer.tweet_length));
        setupTweetEditText();
        maybeEnablePostTweetButton();

        dataConnector.addOnApiFinishedListener(this);

        Glide.with(this)
                .load(currentUser.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(currentUserImageView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTweet();
    }

    //////////////////////////
    ///// setups
    //////////////////////////

    private void populateTweet() {
        String savedTweet = sharedPreferences.getString(SAVED_TWEET_PREF, null);

        Log.d("jenda", "savedTweet: " + savedTweet);
        if (savedTweet != null) {
            tweetTextEditText.setText(savedTweet);
        } else if (replyToTweet != null) {
            tweetTextEditText.setText(replyToTweet.user.getHandle());
        }
        sharedPreferences.edit().clear().commit();
    }

    private void setupTweetEditText() {
        tweetTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("jenda", "text changed: " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    updateReamingCharsCount(
                            getResources().getInteger(R.integer.tweet_length) - s.length());
                    maybeEnablePostTweetButton();
                } catch (Exception e) {

                }
            }
        });
    }

    private void updateReamingCharsCount(int remainingChars) {
        reamingCharsCountTextView.setText(remainingChars + " chars remaining");
    }

    private void maybeEnablePostTweetButton() {
        postTweetButton.setEnabled(tweetTextEditText.getText().length() != 0);
    }

    //////////////////////////
    ///// OnClick handlers
    //////////////////////////

    @OnClick(R.id.post_tweet_button)
    protected void postTweet() {
        String tweet = tweetTextEditText.getText().toString();
        dataConnector.postTweet(tweet, replyToTweet != null ? replyToTweet.id : null);
    }

    @OnClick(R.id.close_dialog)
    protected void closeDialog() {
        closeDialog(true);
    }

    protected void closeDialog(boolean askToSave) {
        final String tweetToSave = tweetTextEditText.getText().toString();
        if (askToSave && tweetToSave != null && !tweetToSave.equals("")) {
            Resources res = this.getResources();
            AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
            alertDialog.setTitle(res.getString(R.string.unsaved_changes));
            alertDialog.setMessage(res.getString(R.string.save_progress));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, res.getString(R.string.save_changes),
                    (DialogInterface dialog, int which) -> {

                Log.d("jenda", "saving: " + tweetToSave);
                sharedPreferences.edit().putString(SAVED_TWEET_PREF, tweetToSave).commit();
                tweetTextEditText.setText("");
                dismiss();
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, res.getString(R.string.no_dont),
                    (DialogInterface dialog, int which) -> {
                tweetTextEditText.setText("");
                dismiss();
            });
            alertDialog.show();

        } else {
            tweetTextEditText.setText("");
            dismiss();
        }
    }

    @Override
    public void onTweetPosted(Tweet tweet) {
        closeDialog(false);
    }

    @Override
    public void onFailure(@StringRes int messageRes) {
        closeDialog(true);
    }

    @Override
    public void onTimeLineFetched(int page, List<Tweet> tweets) {

    }

    public void setReplyToTweet(Tweet replyToTweet) {
        this.replyToTweet = replyToTweet;
    }
}
