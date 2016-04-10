package com.mredrock.cyxbsmobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.activity.MainActivity;
import com.mredrock.cyxbsmobile.ui.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author MOILING
 */
public class CourseContainerFragment extends BaseFragment {

    public static final String TAG = "CourseContainerFragment";

    @Bind(R.id.tab_course_tabs)
    TabLayout mTabs;
    @Bind(R.id.tab_course_viewpager)
    ViewPager mPager;

    private TabPagerAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        List<String> mTitles = new ArrayList<>();
        mTitles.addAll(Arrays.asList(getResources().getStringArray(R.array.titles_weeks)));
        if (mFragmentList.isEmpty()) {
            for (int i = 0; i < mTitles.size(); i++) {
                mFragmentList.add(SampleFragment.newInstance(Integer.toString(i)));
            }
        }
        mAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitles);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_container, container, false);
        Log.d(TAG, "onCreateView: ");
        ButterKnife.bind(this, view);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mTabs.setupWithViewPager(mPager);
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     /*   mTitle.setText(getString(R.string.community));
        ((MainActivity) getActivity()).setContainerToolBar(mToolbar);*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
