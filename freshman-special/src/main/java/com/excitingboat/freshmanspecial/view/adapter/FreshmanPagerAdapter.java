package com.excitingboat.freshmanspecial.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by xushuzhan on 2016/8/4.
 */
public class FreshmanPagerAdapter extends FragmentStatePagerAdapter {
    String[] titles;
    ArrayList<Fragment> fragments;

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
    }

    public FreshmanPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }
}
