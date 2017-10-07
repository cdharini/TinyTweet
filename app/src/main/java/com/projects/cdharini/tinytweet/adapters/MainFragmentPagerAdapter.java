package com.projects.cdharini.tinytweet.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.projects.cdharini.tinytweet.fragments.TimelineFragment;

/**
 * Created by dharinic on 10/3/17.
 */

public class MainFragmentPagerAdapter extends SmartFragmentStatePagerAdapter{

    private static final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home Timeline", "Mentions"};
    private Context mContext;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        if (position == 0) {
            frag = TimelineFragment.newInstance(0, -1);
        } else {
            frag = TimelineFragment.newInstance(1, -1);
        }
        return frag;
    }
}
