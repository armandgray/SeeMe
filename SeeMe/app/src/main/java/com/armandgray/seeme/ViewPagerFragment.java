package com.armandgray.seeme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armandgray.seeme.views.DiscoverFragment;
import com.armandgray.seeme.views.SeeMeFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment {

    private FragmentManager fragmentManager;
    private ViewPagerAdapter adapterViewPager;

    public ViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_pager, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        // TODO Fix Error Here
        adapterViewPager = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);

        return rootView;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;

        public ViewPagerAdapter(FragmentManager manager) {
            super(fragmentManager);
            fragmentManager = manager;
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
                    return DiscoverFragment.newInstance();
                case 1:
                    return SeeMeFragment.newInstance();
                default:
                    return null;
            }
        }
    }
}
