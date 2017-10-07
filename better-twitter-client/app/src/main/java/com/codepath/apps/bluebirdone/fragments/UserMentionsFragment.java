package com.codepath.apps.bluebirdone.fragments;

import android.os.Bundle;

import com.codepath.apps.bluebirdone.R;

/**
 * Created by jan_spidlen on 10/6/17.
 */

public class UserMentionsFragment extends BaseFragment {

    public static UserMentionsFragment newInstance() {
        Bundle args = new Bundle();
        UserMentionsFragment fragment = new UserMentionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getName() {
        return R.string.user_mentions_fragment_title;
    }
}
