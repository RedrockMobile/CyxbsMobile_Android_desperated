package com.mredrock.cyxbs.ui.adapter.lost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.lost.LostActivity;
import com.mredrock.cyxbs.ui.fragment.lost.LostFragment;
import com.mredrock.cyxbs.util.LogUtils;

import java.lang.ref.WeakReference;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private SparseArray<WeakReference<LostFragment>> lostFragments = new SparseArray<>();
    private SparseArray<WeakReference<LostFragment>> foundFragments = new SparseArray<>();
    private int mode;

    public LostViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        LostFragment fragment = findFragmentByModeAndPosition(mode, position);
        LogUtils.LOGI("LostViewAdapter", "getItem: mode" + mode + ", position=" + position);
        if (fragment == null) {
            LogUtils.LOGI("LostViewAdapter", "getItemInCreate: mode" + mode + ", position=" + position);
            fragment = new LostFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(LostFragment.ARGUMENT_THEME, mode);
            bundle.putString(LostFragment.ARGUMENT_CATEGORY, getTitles()[position]);
            fragment.setArguments(bundle);
            putFragmentByModeAndPosition(mode, position, fragment);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitles()[position];
    }

    @Override
    public int getCount() {
        return getTitles().length;
    }

//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return ((Fragment) object).getView() == view;
//    }

    private String[] getTitles() {
        if (titles == null) {
            titles = APP.getContext().getResources().getStringArray(R.array.lost_category_list);
        }
        return titles;
    }

    @Nullable
    private LostFragment findFragmentByModeAndPosition(int mode, int position) {
        SparseArray<WeakReference<LostFragment>> fragments;
        if (mode == LostActivity.THEME_LOST) {
            fragments = lostFragments;
        } else {    // FOUND
            fragments = foundFragments;
        }
        WeakReference<LostFragment> reference = fragments.get(position);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    private void putFragmentByModeAndPosition(int mode, int position, LostFragment fragment) {
        SparseArray<WeakReference<LostFragment>> fragments;
        if (mode == LostActivity.THEME_LOST) {
            fragments = lostFragments;
        } else {    // FOUND
            fragments = foundFragments;
        }
        fragments.put(position, new WeakReference<>(fragment));

    }

    public void setMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }
}
