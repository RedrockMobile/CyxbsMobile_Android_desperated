package com.mredrock.cyxbsmobile.ui.fragment.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 社区
 */
public class CommunityContainerFragment extends BaseFragment {

    @Bind(R.id.community_TabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.community_ViewPager)
    ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_container, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        List<Fragment> fragmentLIst = new ArrayList<>();

        HotNewsFragment mPopularNewFragment = new HotNewsFragment();
        BBDDNewsFragment mBBLLNewFragment = new BBDDNewsFragment();

        OfficialFragment mOfficialFragment = new OfficialFragment();
        fragmentLIst.add(mPopularNewFragment);
        fragmentLIst.add(mBBLLNewFragment);
        fragmentLIst.add(mOfficialFragment);

        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), fragmentLIst, Arrays.asList(getActivity().getResources().getStringArray(R.array.community_tab_tiles)));

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragmentLIst.size());

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void changeViewPagerIndex(int index) {
        mViewPager.setCurrentItem(index);
    }
}
