package com.boutline.sports.adapters;

import android.support.v4.app.FragmentPagerAdapter;

import com.boutline.sports.fragments.ExpertFragment;
import com.boutline.sports.fragments.FanVoiceFragment;
import com.boutline.sports.fragments.MediaFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

        public TabPagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
            	  return FanVoiceFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
            	  return ExpertFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
            	  return MediaFragment.newInstance(2, "Page # 3");
            default:
                return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }