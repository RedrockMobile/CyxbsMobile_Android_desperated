package com.mredrock.cyxbs.ui.fragment.explore;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.ui.activity.explore.MapActivity;
import com.mredrock.cyxbs.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbs.ui.activity.explore.WhatToEatActivity;
import com.mredrock.cyxbs.ui.activity.explore.electric.ElectricChargeActivity;
import com.mredrock.cyxbs.ui.activity.lost.LostActivity;
import com.mredrock.cyxbs.ui.adapter.ExploreRollerViewAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.ui.widget.RollerView;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.freshmanspecial.view.SpecialMainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by Stormouble on 16/4/16.
 */
public class ExploreFragment extends BaseFragment {

    private static final String TAG = LogUtils.makeLogTag(ExploreFragment.class);

    @Bind(R.id.rollerView)
    RollerView mRollerView;
    @Bind(R.id.explore_what_to_eat_holder)
    ViewGroup mWhatToEatHolder;
    @Bind(R.id.explore_surrounding_food_holder)
    ViewGroup mSurroundingFoodHolder;

    @OnClick(R.id.explore_portal_holder)
    void clickToPortal() {
        if (isAdded()) {
            // WebViewUtils.showPortalWebView(getActivity(), Const.REDROCK_PORTAL);
            Uri uri = Uri.parse(Const.REDROCK_PORTAL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getActivity().startActivity(intent);
        }
    }

    @OnClick(R.id.explore_map_holder)
    void clickToMap() {
        if (isAdded()) {
            MapActivity.startMapActivity(getActivity());
        }
    }

    @OnClick(R.id.explore_what_to_eat_holder)
    void clickToEat() {
        if (isAdded()) {
            int[] startLocation = new int[2];
            mWhatToEatHolder.getLocationOnScreen(startLocation);
            startLocation[0] += mWhatToEatHolder.getWidth() / 2;
            WhatToEatActivity.startWhatToEatActivity(startLocation, getActivity());
        }
    }

    @OnClick(R.id.explore_surrounding_food_holder)
    void clickToAround() {
        if (isAdded()) {
            int[] startLocation = new int[2];
            mSurroundingFoodHolder.getLocationOnScreen(startLocation);
            startLocation[0] += mSurroundingFoodHolder.getWidth() / 2;
            SurroundingFoodActivity.startSurroundingFoodActivity(startLocation, getActivity());
        }
    }

    @OnClick(R.id.explore_electric_query_holder)
    void clickToElectricQuery() {
        getActivity().startActivity(new Intent(getActivity(), ElectricChargeActivity.class));
    }

    @OnClick(R.id.explore_lost_and_found_holder)
    void clickToLostAndFound() {
        LostActivity.start(getActivity());
    }

    @OnClick(R.id.explore_smaile_holder)
    void clickToSmailFace() {
        Uri uri = Uri.parse(Const.SMAILE_FACE);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.explore_freshman_holder)
    void clickToFreshmanSpecial() {
        // TODO: 2017/8/15 新生专题跳转
        getActivity().startActivity(new Intent(getActivity(), SpecialMainActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);
        /*mViewPager.setAdapter(new ExploreViewPagerAdapter());
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.width = DensityUtils.getScreenWidth(APP.getContext())
                - DensityUtils.dp2px(APP.getContext(), 70);
        params.height = (int) (params.width * 0.65);
        mViewPager.setLayoutParams(params);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        mViewPager.setPageMargin(DensityUtils.dp2px(APP.getContext(), 12));*/

        mRollerView.setAdapter(new ExploreRollerViewAdapter(getContext(), new int[]{
                R.drawable.img_cqupt1,
                R.drawable.img_cqupt2,
                R.drawable.img_cqupt3,
                R.drawable.img_cqupt1,
                R.drawable.img_cqupt2,
                R.drawable.img_cqupt3}));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
