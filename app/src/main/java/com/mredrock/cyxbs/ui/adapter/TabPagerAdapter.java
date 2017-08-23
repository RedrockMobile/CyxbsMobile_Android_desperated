package com.mredrock.cyxbs.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> mTitleList;
    private final List<Fragment> mFragmentsList;

    public TabPagerAdapter(final FragmentManager fm, List<Fragment> fragmentsList, List<String> titleList) {
        super(fm);
        this.mFragmentsList = fragmentsList;
        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return (mFragmentsList == null || mFragmentsList.size() == 0) ? null : mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList == null ? 0 : mFragmentsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (mTitleList.size() > position) ? mTitleList.get(position) : "";
    }
}
