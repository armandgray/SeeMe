package com.armandgray.seeme.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.armandgray.seeme.models.User;
import com.armandgray.seeme.views.DiscoverFragment;
import com.armandgray.seeme.views.NetworkFragment;
import com.armandgray.seeme.views.NotesFragment;
import com.armandgray.seeme.views.ProfileFragment;
import com.armandgray.seeme.views.SeeMeFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 5;
    private final User activeUser;

    public ViewPagerAdapter(FragmentManager manager, User activeUser) {
        super(manager);
        this.activeUser = activeUser;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DiscoverFragment.newInstance(activeUser);
            case 1:
                return NetworkFragment.newInstance(activeUser);
            case 2:
                return SeeMeFragment.newInstance(activeUser);
            case 3:
                return NotesFragment.newInstance(activeUser);
            case 4:
                return ProfileFragment.newInstance(activeUser);
            default:
                return null;
        }
    }
}
