package com.mredrock.cyxbs.ui.adapter.lost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mredrock.cyxbs.util.LogUtils;

import java.util.List;

/**
 * Created by wusui on 2017/2/10.
 */

public class LostTabPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String> mTitleList;
    private final Fragment fragment;
    Bundle bundle;
    String message;

    public LostTabPagerAdapter(FragmentManager fm, Fragment fragment, List<String> titleList, Bundle bundle) {
        super(fm);
        this.fragment = fragment;
        this.mTitleList = titleList;
        this.bundle = bundle;
        message = fragment.getArguments().getString("ARGUEMENT");
        if (message == null) {
            message = "全部";
        } else {
            return;
        }

        LogUtils.LOGE("TabAdapter", message);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return message;
    }

}
