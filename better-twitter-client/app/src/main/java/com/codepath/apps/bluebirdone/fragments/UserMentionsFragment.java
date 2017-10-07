package com.codepath.apps.bluebirdone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.bluebirdone.R;

/**
 * Created by jan_spidlen on 10/6/17.
 */

@Deprecated
public class UserMentionsFragment extends BaseFragment {

    public static UserMentionsFragment newInstance() {
        Bundle args = new Bundle();
        UserMentionsFragment fragment = new UserMentionsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_mentions, container, false);

        return view;
    }
}
