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
import com.mredrock.cyxbs.model.RollerViewInfo;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.explore.ExploreSchoolCarActivity;
import com.mredrock.cyxbs.ui.activity.explore.MapActivity;
import com.mredrock.cyxbs.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbs.ui.activity.explore.WhatToEatActivity;
import com.mredrock.cyxbs.ui.activity.explore.electric.ElectricChargeActivity;
import com.mredrock.cyxbs.ui.activity.lost.LostActivity;
import com.mredrock.cyxbs.ui.adapter.ExploreRollerViewAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.ui.widget.RollerView;
import com.mredrock.cyxbs.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Stormouble on 16/4/16.
 */
public class ExploreFragment extends BaseFragment {

    private static final String TAG = LogUtils.makeLogTag(ExploreFragment.class);

    @BindView(R.id.rollerView)
    RollerView mRollerView;
    @BindView(R.id.explore_what_to_eat_holder)
    ViewGroup mWhatToEatHolder;
    @BindView(R.id.explore_surrounding_food_holder)
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

    @OnClick(R.id.explore_school_car)
    void clickToSchoolcar(){
        ExploreSchoolCarActivity.startSchoolCarActivity(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);
        setRollerView();
        return view;
    }

    private void setRollerView() {
        mRollerView.setAdapter(new ExploreRollerViewAdapter(getContext(), new int[]{
                R.drawable.img_cqupt1,
                R.drawable.img_cqupt2,
                R.drawable.img_cqupt3,
                R.drawable.img_cqupt1,
                R.drawable.img_cqupt2,
                R.drawable.img_cqupt3}));

        RequestManager.getInstance().getRollerViewInfo(new SimpleObserver<>(getActivity(), new SubscriberListener<List<RollerViewInfo>>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }

            @Override
            public boolean onError(Throwable e) {
                e.printStackTrace();
                return super.onError(e);
            }

            @Override
            public void onNext(List<RollerViewInfo> rollerViewInfoList) {
                mRollerView.setAdapter(new ExploreRollerViewAdapter(getContext(), rollerViewInfoList));
                super.onNext(rollerViewInfoList);
            }
        }), "4");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
