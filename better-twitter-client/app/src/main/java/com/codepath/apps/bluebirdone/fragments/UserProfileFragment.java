package com.codepath.apps.bluebirdone.fragments;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.dialogs.BaseBlueBirdOneDialog;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.User;
import com.codepath.apps.bluebirdone.presenters.UserProfilePresenter;
import com.codepath.apps.bluebirdone.twitter.UserTimelineDataConnector;
import com.codepath.apps.bluebirdone.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jan_spidlen on 10/6/17.
 */

public class UserProfileFragment extends BaseBlueBirdOneDialog {

    public User user;

    @BindView(R.id.header_photo)
    ImageView headerPhotoImageView;

    @BindView(R.id.profile_image_view)
    ImageView profileImage;

    @BindView(R.id.user_full_name)
    TextView userFullNameTextView;

    @BindView(R.id.user_handle)
    TextView userHandleTextView;

    @BindView(R.id.user_description)
    TextView userDescription;

    @BindView(R.id.following)
    TextView following;

    @BindView(R.id.followers)
    TextView followers;

    @BindView(R.id.tweets_container)
    FrameLayout frameLayout;

    @BindView(R.id.follow_button)
    Button followButton;

    @Inject
    TwitterClient twitterClient;
    @Inject
    ModelSerializer modelSerializer;

    @Inject
    UserProfilePresenter userProfilePresenter;

    @Inject
    public UserProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        userProfilePresenter.setCurrentUserProfileFragment(this);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        createAndAttachTweetsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_user, container, false);
        ButterKnife.bind(this, view);

        refreshUser();
        return view;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void refreshTheFollowButton() {
        Resources res = getContext().getResources();
        if (user.following) {
            followButton.setText(res.getText(R.string.unfollow));
            followButton.setBackgroundColor(res.getColor(R.color.negative_red));
        } else {
            followButton.setText(res.getText(R.string.follow));
            followButton.setBackgroundColor(res.getColor(R.color.logo_blue));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void createAndAttachTweetsFragment() {
        TweetsDisplayingFragment tweetsDisplayingFragment =
                TweetsDisplayingFragment.newInstance(
                        TweetsDisplayingFragment.Type.USER_TWEETS, user.id);
        this.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.tweets_container, tweetsDisplayingFragment)
                .commit();
    }

    @OnClick(R.id.follow_button)
    protected void followButtonClicked() {
        Log.d("jenda", "followed button clicked");
        userProfilePresenter.followOrUnfollowUser(user);
    }

    private void refreshUser() {
        Glide.with(this)
                .load(user.profileBackgroundImageUrl)
                .into(headerPhotoImageView);

        userFullNameTextView.setText(user.name);
        userHandleTextView.setText(user.getHandle());

        followers.setText(Utils.formatLargeNumber(user.followersCount));
        following.setText(Utils.formatLargeNumber(user.friendsCount));
        userDescription.setText(user.description);


        Glide.with(this)
                .load(user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);

        Log.d("jenda", "user id" + user.id);
        refreshTheFollowButton();
    }
}
