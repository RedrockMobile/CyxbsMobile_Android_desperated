package com.mredrock.cyxbs.ui.fragment.lost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.fragment.BaseLazyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wusui on 2017/1/20.
 */

public class BaseLostFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.fab_main)
    FloatingActionButton mFabMain;
    @BindView(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.information_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    protected void onFirstUserVisible() {

    }
}
