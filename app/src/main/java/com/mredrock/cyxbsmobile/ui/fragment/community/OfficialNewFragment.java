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
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.ContentBean;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.EndlessRecyclerOnScrollListener;
import com.mredrock.cyxbsmobile.ui.activity.SpecificNewsActivity;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.OfficialNewAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class OfficialNewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OfficialNewAdapter.OnItemOnClickListener {
    @Bind(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.information_refresh)
    SwipeRefreshLayout refreshLayout;
    int currentPage = 1;
    private List<ContentBean> mDatas = null;
    private OfficialNewAdapter mNewsAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        refreshLayout.setColorSchemeColors(R.color.orange);
        refreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getParentFragment().getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int Page) {
                getNextPageData(1, ++currentPage);
            }
        });
        getCurrentData(1, 1, false);
        getCurrentData(1, 1, true);
    }

    protected void getCurrentData(int size, int page, boolean update) {
        getHotCurrentData(size, page, update);
    }

    private void doWithObser(Observable<List<ContentBean>> observable) {
        observable.doOnSubscribe(() -> showLoadingProgress())
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
                    getDataFailed(throwable.toString());
                    Log.e("=================>>>>>>>>>>", throwable.toString());
                });

    }

    private void getHotCurrentData(int size, int page, boolean update) {
        doWithObser(RequestManager.getInstance().getListNews(size, page, update));
    }


    private void initAdapter(List<ContentBean> datas) {
        mDatas = datas;
        mNewsAdapter = new OfficialNewAdapter(mDatas);
        mNewsAdapter.setOnItemOnClickListener(this);
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mNewsAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        addFooterView(mHeaderViewRecyclerAdapter);
    }

    private void addFooterView(HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_footer_item_news, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(view);
    }

    private void getNextPageData(int size, int page) {
        getNextHotArticle(size, page);
    }


    private void getNextHotArticle(int size, int page) {
        doNextWithObser(RequestManager.getInstance().getListNews(size, page));
    }

    private void doNextWithObser(Observable<List<ContentBean>> observable) {
        observable.subscribe(newses -> {
            mDatas.addAll(newses);
            mNewsAdapter.notifyDataSetChanged();
        }, throwable -> {
            getDataFailed(throwable.toString());
        });
    }


    private void showLoadingProgress() {
        refreshLayout.setRefreshing(true);
    }

    private void closeLoadingProgress() {
        refreshLayout.setRefreshing(false);
    }

    private void getDataFailed(String reason) {
        Toast.makeText(getContext(), getString(R.string.erro), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        getCurrentData(1, 1, true);
    }


    @Override
    public void onItemClick(View itemView, int position, ContentBean dataBean) {
        Intent intent = new Intent(getActivity(), SpecificNewsActivity.class);
        intent.putExtra("dataBean", new News.DataBean(dataBean));
        startActivity(intent);
    }
}
