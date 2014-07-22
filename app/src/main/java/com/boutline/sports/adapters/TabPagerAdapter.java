package com.boutline.sports.adapters;

import android.support.v4.app.FragmentPagerAdapter;
import com.boutline.sports.fragments.TweetsFragment;


public class TabPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    String mtId;

        public TabPagerAdapter(android.support.v4.app.FragmentManager fragmentManager,String mtId) {
            super(fragmentManager);
            this.mtId = mtId;
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
            	  return TweetsFragment.newInstance(mtId, "popular");
            case 1: // Fragment # 0 - This will show FirstFragment different title
            	  return TweetsFragment.newInstance(mtId, "media");
            case 2: // Fragment # 1 - This will show SecondFragment
            	  return TweetsFragment.newInstance(mtId, "normal");
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