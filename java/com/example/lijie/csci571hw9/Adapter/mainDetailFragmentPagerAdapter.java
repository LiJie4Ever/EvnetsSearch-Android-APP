package com.example.lijie.csci571hw9.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class mainDetailFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentsList;
    private ArrayList<String> tabTitles;
    private Context context;

    public mainDetailFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList, ArrayList<String> tabTitles, Context context) {
        super(fm);
        this.fragmentsList = fragmentsList;
        this.tabTitles = tabTitles;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    @Override
    public int getCount() {
        if (null == fragmentsList) {
            return 0;
        } else {
            return fragmentsList.size();
        }
    }

}
