package com.mredrock.cyxbsmobile.ui.fragment.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author MathiasLuo
 */
public class NewsFragment extends BaseFragment {

    @Bind(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        News news = new News();
        mRecyclerView.setAdapter(new NewsAdapter(news));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
