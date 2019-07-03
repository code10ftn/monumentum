package io.github.code10ftn.monumentum.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.code10ftn.monumentum.fragment.AllMonumentsFragment_;
import io.github.code10ftn.monumentum.fragment.FavouriteMonumentsFragment_;
import io.github.code10ftn.monumentum.fragment.VisitedMonumentsFragment_;

public class NavigationAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 3;

    public NavigationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AllMonumentsFragment_.builder().build();
            case 1:
                return FavouriteMonumentsFragment_.builder().build();
            default:
                return VisitedMonumentsFragment_.builder().build();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
