package com.mredrock.cyxbs.ui.adapter.me;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;



import java.util.List;

/**
 * Created by glossimar on 2017/10/1.
 */

public class VolunteerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> yearList;

    public VolunteerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> yearList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.yearList = yearList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return yearList.get(position).toString();
    }

    @Override
    public int getCount() {
        return yearList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
}
