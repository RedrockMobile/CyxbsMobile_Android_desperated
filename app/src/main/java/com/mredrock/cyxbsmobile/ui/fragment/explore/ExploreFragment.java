package com.mredrock.cyxbsmobile.ui.fragment.explore;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jude.rollviewpager.RollPagerView;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.RollViewPagerHint;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.ui.activity.explore.MapActivity;
import com.mredrock.cyxbsmobile.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbsmobile.ui.activity.explore.WhatToEatActivity;
import com.mredrock.cyxbsmobile.ui.adapter.ExploreRollViewPagerAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;
import com.mredrock.cyxbsmobile.util.LogUtils;
import com.mredrock.cyxbsmobile.util.WebViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Stormouble on 16/4/16.
 */
public class ExploreFragment extends BaseFragment {

    private static final String TAG = LogUtils.makeLogTag(ExploreFragment.class);

    @Bind(R.id.explore_roll_view_pager)
    RollPagerView mRollViewPager;
    @Bind(R.id.explore_what_to_eat_holder)
    LinearLayout mWhatToEatHolder;
    @Bind(R.id.explore_surrounding_food_holder)
    LinearLayout mSurroundingFoodHolder;

    @OnClick(R.id.explore_portal_holder)
    void clickToPortal() {
        if (isAdded()) {
            WebViewUtils.showPortalWebView(getActivity(), Const.REDROCK_PORTAL);
        }
    }

    @OnClick(R.id.explore_map_holder)
    void clickToMap() {
        if (isAdded()) {
            startActivity(new Intent(getActivity(), MapActivity.class));
        }
    }

    @OnClick(R.id.explore_what_to_eat_holder)
    void clickToEat() {
        if (isAdded()) {
            int[] startLocation = new int[2];
            mWhatToEatHolder.getLocationOnScreen(startLocation);
            WhatToEatActivity.startWhatToEatActivity(startLocation, getActivity());
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @OnClick(R.id.explore_surrounding_food_holder)
    void clickToAround() {
        if (isAdded()) {
            int[] startLocation = new int[2];
            mSurroundingFoodHolder.getLocationOnScreen(startLocation);
            startLocation[0] += mSurroundingFoodHolder.getWidth() / 2;
            SurroundingFoodActivity.startSurroundingFoodActivity(startLocation, getActivity());
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRollViewPager.setAdapter(new ExploreRollViewPagerAdapter(mRollViewPager));
        mRollViewPager.setHintView(new RollViewPagerHint(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
