package com.codepath.apps.bluebirdone.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.bluebirdone.R;
import com.codepath.apps.bluebirdone.TwitterClient;
import com.codepath.apps.bluebirdone.dialogs.BaseBlueBirdOneDialog;
import com.codepath.apps.bluebirdone.models.CurrentUser;
import com.codepath.apps.bluebirdone.models.ModelSerializer;
import com.codepath.apps.bluebirdone.models.User;
import com.codepath.apps.bluebirdone.twitter.DataConnector;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 10/6/17.
 */

public class CurrentUserProfileFragment extends BaseBlueBirdOneDialog {

    public CurrentUser currentUser;

    @BindView(R.id.header_photo)
    ImageView headerPhotoImageView;
    @BindView(R.id.user_full_name)
    TextView userFullNameTextView;
    @BindView(R.id.user_handle)
    TextView userHandleTextView;

    @Inject
    public CurrentUserProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_user, container, false);

        getComponent().inject(this);
        ButterKnife.bind(this, view);


        Glide.with(this)
                .load(currentUser.profileBackgroundImageUrl)
                .into(headerPhotoImageView);

        userFullNameTextView.setText(currentUser.name);
        userHandleTextView.setText(currentUser.getHandle());

        Log.d("jenda", "user id" + currentUser.id);

        return view;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }
}