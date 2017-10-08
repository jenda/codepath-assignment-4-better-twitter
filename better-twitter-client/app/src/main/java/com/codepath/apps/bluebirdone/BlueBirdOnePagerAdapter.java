package com.codepath.apps.bluebirdone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.bluebirdone.fragments.BaseFragment;
import com.codepath.apps.bluebirdone.fragments.TweetsDisplayingFragment;

/**
 * Created by jan_spidlen on 10/6/17.
 */

public class BlueBirdOnePagerAdapter extends FragmentPagerAdapter {
    private final String[] tabs;
    final int PAGE_COUNT = 2;
    private Context context;

    public BlueBirdOnePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabs = new String[] {
                context.getString(R.string.home_timeline_fragment_title),
                context.getString(R.string.user_mentions_fragment_title)
        };
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragmentBasedOnPosition(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabs[position];
    }

    private BaseFragment getFragmentBasedOnPosition(int position) {
        switch (position) {
            case 0:
                return TweetsDisplayingFragment.newInstance(TweetsDisplayingFragment.Type.HOME_TIMELINE);
            case 1:
                return TweetsDisplayingFragment.newInstance(TweetsDisplayingFragment.Type.USER_MENTIONS);
            default:
                throw new IllegalArgumentException("bad position: " + position);
        }
    }
}