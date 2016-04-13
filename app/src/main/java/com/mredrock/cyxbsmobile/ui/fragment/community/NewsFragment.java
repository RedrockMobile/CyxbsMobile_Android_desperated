package com.mredrock.cyxbsmobile.ui.fragment.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.model.community.Student;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.EndlessRecyclerOnScrollListener;
import com.mredrock.cyxbsmobile.ui.activity.SpecificNewsActivity;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author MathiasLuo
 */
public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemOnClickLIstener {

    @Bind(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.information_refresh)
    SwipeRefreshLayout refreshLayout;

    private List<News> mDatas = null;
    private NewsAdapter mNewsAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    int currentPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        refreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getParentFragment().getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int Page) {
                getNextPageData(1, ++currentPage);
            }
        });
        getCurrentData(1, 1);
    }

    private void showLoadingProgress() {
        refreshLayout.setRefreshing(true);
    }

    private void closeLoadingProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onRefresh() {
        getCurrentData(1, 1);
    }

    private void getCurrentData(int size, int page) {
        RequestManager.getInstance()
                .getHotArticle(size, page, Student.STU_NUM, Student.ID_NUM)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .subscribe(newses -> {
                    if (mDatas == null) {
                        initAdapter(newses);
                    } else {
                        mDatas = newses;
                        mNewsAdapter.notifyDataSetChanged();
                    }
                    closeLoadingProgress();
                }, throwable -> {
                    closeLoadingProgress();
                    Toast.makeText(getContext(), getString(R.string.erro), Toast.LENGTH_SHORT).show();
                });

    }

    private void initAdapter(List<News> datas) {
        mDatas = datas;
        mNewsAdapter = new NewsAdapter(mDatas);
        mNewsAdapter.setOnItemOnClickLIstener(this);
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mNewsAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        addFooterView(mHeaderViewRecyclerAdapter);
    }

    private void addFooterView(HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_footer_item, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(view);
    }

    private void getNextPageData(int size, int page) {
        RequestManager.getInstance()
                .getHotArticle(size, page, Student.STU_NUM, Student.ID_NUM)
                .subscribe(newses -> {
                    mDatas.addAll(newses);
                    mNewsAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Toast.makeText(getContext(), getString(R.string.erro), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onItemClick(View itemView, int position, News.DataBean dataBean) {
        Intent intent = new Intent(getActivity(), SpecificNewsActivity.class);
        intent.putExtra("dataBean", dataBean);
        startActivity(intent);
    }
}
