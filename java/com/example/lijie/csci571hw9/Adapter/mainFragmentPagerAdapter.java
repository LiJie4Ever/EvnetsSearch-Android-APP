package com.example.lijie.csci571hw9.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lijie.csci571hw9.Fragment.FavoriteListFragment;
import com.example.lijie.csci571hw9.Fragment.SearchEventFragment;

import java.util.ArrayList;

public class mainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> tabTitles;
    private Context context;

    public mainFragmentPagerAdapter(FragmentManager fm, ArrayList<String> tabTitles, Context context) {
        super(fm);
        this.tabTitles = tabTitles;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (0 != position) {
            return FavoriteListFragment.getNewInstance();
        } else {
            return SearchEventFragment.getNewInstance();
        }
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
