package com.mredrock.cyxbsmobile.ui.fragment.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.BBDD;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.EndlessRecyclerOnScrollListener;
import com.mredrock.cyxbsmobile.ui.activity.SpecificNewsActivity;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author MathiasLuo
 */
public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemOnClickListener {

    @Bind(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.information_refresh)
    SwipeRefreshLayout refreshLayout;

    private List<News> mDatas = null;
    private NewsAdapter mNewsAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    int currentPage = 1;
    private int newsType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        newsType = getArguments().getInt("type", 1);
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

    private void getCurrentData(int size, int page) {
        switch (newsType) {
            case BBDD.SHOTARTICLE:
                getHotCurrentData(size, page);
                break;
            case BBDD.LISTARTICLE:
                getTypeCurrentData(size, page, BBDD.BBDD);
                break;
            case BBDD.JWZXARTICLE:
                getTypeCurrentData(size, page, BBDD.JWZX);
                break;
        }
    }


    private void doWithObser(Observable<List<News>> observable) {
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
                });

    }

    private void getTypeCurrentData(int size, int page, int type) {
        doWithObser(RequestManager.getInstance().getListArticle(type, size, page));
    }

    private void getHotCurrentData(int size, int page) {
        doWithObser(RequestManager.getInstance().getHotArticle(size, page));
    }


    private void initAdapter(List<News> datas) {
        mDatas = datas;
        mNewsAdapter = new NewsAdapter(mDatas);
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
        RequestManager.getInstance()
                .getHotArticle(size, page)
                .subscribe(newses -> {
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
        Toast.makeText(getContext(), getString(R.string.erro) + "===>>>" + reason, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(View itemView, int position, News.DataBean dataBean) {
        Intent intent = new Intent(getActivity(), SpecificNewsActivity.class);
        intent.putExtra("dataBean", dataBean);
        startActivity(intent);
    }


}
