package com.mredrock.cyxbsmobile.ui.fragment.social;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.subscriber.EndlessRecyclerOnScrollListener;
import com.mredrock.cyxbsmobile.ui.activity.social.SpecificNewsActivity;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mathiasluo on 16-4-26.
 */
public abstract class BaseNewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemOnClickListener {


    protected NewsAdapter mNewsAdapter;
    @Bind(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.information_refresh)
    SwipeRefreshLayout refreshLayout;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int currentIndex;
    private List<News> mDatas = null;
    private FooterViewWrapper mFooterViewWrapper;

    private final static int PER_PAGE_NUM = 9;
    public static final String TAG = "BaseNewsFragment";

    abstract Observable<List<News>> provideData(int size, int page, boolean update);

    abstract Observable<List<News>> provideData(int size, int page);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    protected void init() {
        refreshLayout.setColorSchemeColors(R.color.orange);
        refreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getParentFragment().getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                currentIndex = page;
                getNextPageData(1, currentIndex);
            }
        });

        getCurrentData(PER_PAGE_NUM, 1, false);
        getCurrentData(PER_PAGE_NUM, 1, true);
    }

    @Override
    public void onRefresh() {
        getCurrentData(1, PER_PAGE_NUM, true);
    }

    private void getDataFailed(String reason) {
        Toast.makeText(getContext(), getString(R.string.erro), Toast.LENGTH_SHORT).show();
        Log.e(TAG, reason);
    }

    private void showLoadingProgress() {
        refreshLayout.setRefreshing(true);
    }

    private void closeLoadingProgress() {
        refreshLayout.setRefreshing(false);
    }

    public void getCurrentData(int size, int page, boolean update) {
        provideData(size, page, update)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .subscribe(newses -> {
                    if (mDatas == null)
                        initAdapter(newses);
                    else
                        mNewsAdapter.replaceDatas(newses);
                    Log.i("====>>>", "page===>>>" + page + "size==>>" + newses.size());
                    closeLoadingProgress();
                }, throwable -> {
                    closeLoadingProgress();
                    getDataFailed(throwable.toString());
                });
    }


    private void initAdapter(List<News> datas) {
        mDatas = datas;
        mNewsAdapter = new NewsAdapter(mDatas) {
            @Override
            public void setDate(ViewHolder holder, News.DataBean mDataBean) {
                BaseNewsFragment.this.setDate(holder, mDataBean);
            }
        };
        mNewsAdapter.setOnItemOnClickListener(this);
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mNewsAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        addFooterView(mHeaderViewRecyclerAdapter);
    }

    protected void setDate(NewsAdapter.ViewHolder holder, News.DataBean mDataBean) {
    }

    private void addFooterView(HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter) {
        mFooterViewWrapper = new FooterViewWrapper(getContext(), mRecyclerView);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterViewWrapper.getFooterView());
        mFooterViewWrapper.onFailedClick(view -> getNextPageData(1, currentIndex));
    }

    private void getNextPageData(int size, int page) {
        provideData(size, page)
                .doOnSubscribe(() -> mFooterViewWrapper.showLoading())
                .subscribe(newses -> {
                            mNewsAdapter.addDatas(newses);
                            Log.i("====>>>", "page===>>>" + page + "size==>>" + newses.size());
                        },
                        throwable -> {
                            mFooterViewWrapper.showLoadingFailed();
                            getDataFailed(throwable.toString());
                        });
    }

    @Override
    public void onItemClick(View itemView, int position, News.DataBean dataBean) {
        Intent intent = new Intent(getActivity(), SpecificNewsActivity.class);
        intent.putExtra("dataBean", dataBean);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static class FooterViewWrapper {

        @Bind(R.id.progressBar)
        CircleProgressBar mCircleProgressBar;
        @Bind(R.id.textLoadingFailed)
        TextView mTextLoadingFailed;
        private View footerView;

        public FooterViewWrapper(Context context, ViewGroup parent) {
            footerView = LayoutInflater.from(context).inflate(R.layout.list_footer_item_news, parent, false);
            ButterKnife.bind(this, footerView);
        }

        public View getFooterView() {
            return footerView;
        }

        public void showLoading() {
            mCircleProgressBar.setVisibility(View.VISIBLE);
            mTextLoadingFailed.setVisibility(View.GONE);
        }

        public void showLoadingFailed() {
            mCircleProgressBar.setVisibility(View.INVISIBLE);
            mTextLoadingFailed.setVisibility(View.VISIBLE);
        }

        public void onFailedClick(View.OnClickListener onClickListener) {
            mTextLoadingFailed.setOnClickListener(onClickListener::onClick);
        }

    }
}
