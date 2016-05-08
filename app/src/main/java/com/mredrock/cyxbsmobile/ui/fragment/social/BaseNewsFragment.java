package com.mredrock.cyxbsmobile.ui.fragment.social;

import android.content.Context;
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
import com.mredrock.cyxbsmobile.model.social.HotNews;
import com.mredrock.cyxbsmobile.model.social.HotNewsContent;
import com.mredrock.cyxbsmobile.subscriber.EndlessRecyclerOnScrollListener;
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
public abstract class BaseNewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    protected NewsAdapter mNewsAdapter;
    @Bind(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.information_refresh)
    SwipeRefreshLayout refreshLayout;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int currentIndex;
    private List<HotNews> mListHotNews = null;
    private FooterViewWrapper mFooterViewWrapper;

    public final static int PER_PAGE_NUM = 10;
    public static final String TAG = "BaseNewsFragment";
    public final static int FIRST_PAGE_INDEX = 0;


    abstract Observable<List<HotNews>> provideData(int size, int page, boolean update);

    abstract Observable<List<HotNews>> provideData(int size, int page);

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
                getNextPageData(PER_PAGE_NUM, currentIndex);
            }
        });

        getCurrentData(PER_PAGE_NUM, FIRST_PAGE_INDEX, false);
        getCurrentData(PER_PAGE_NUM, FIRST_PAGE_INDEX, true);

    }

    @Override
    public void onRefresh() {
        getCurrentData(PER_PAGE_NUM, FIRST_PAGE_INDEX, true);
    }

    private void getDataFailed(String reason) {
        Toast.makeText(getContext(), getString(R.string.erro), Toast.LENGTH_SHORT).show();
        Log.e(TAG, reason);
    }

    private void showLoadingProgress() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
    }

    private void closeLoadingProgress() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    public void getCurrentData(int size, int page, boolean update) {
        provideData(size, page, update)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .subscribe(newses -> {
                    if (mListHotNews == null) {
                        initAdapter(newses);
                        if (newses.size() == 0) mFooterViewWrapper.showLoadingNoData();
                    } else mNewsAdapter.addDatas(newses);
                    Log.i("====>>>", "page===>>>" + page + "size==>>" + newses.size());
                    closeLoadingProgress();
                }, throwable -> {
                    closeLoadingProgress();
                    getDataFailed(throwable.toString());
                });
    }


    private void initAdapter(List<HotNews> listHotNews) {
        mListHotNews = listHotNews;
        mNewsAdapter = new NewsAdapter(mListHotNews) {
            @Override
            public void setDate(ViewHolder holder, HotNewsContent hotNewsContent) {
                BaseNewsFragment.this.setDate(holder, hotNewsContent);
            }
        };
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mNewsAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        addFooterView(mHeaderViewRecyclerAdapter);
    }

    protected void setDate(NewsAdapter.ViewHolder holder, HotNewsContent mDataBean) {
    }

    private void addFooterView(HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter) {
        mFooterViewWrapper = new FooterViewWrapper(getContext(), mRecyclerView);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterViewWrapper.getFooterView());
        mFooterViewWrapper.onFailedClick(view -> getNextPageData(PER_PAGE_NUM, currentIndex));
    }

    private void getNextPageData(int size, int page) {
        provideData(size, page)
                .doOnSubscribe(() -> mFooterViewWrapper.showLoading())
                .subscribe(newses -> {
                            if (newses.size() == 0) {
                                mFooterViewWrapper.showLoadingNoMoreData();
                                return;
                            }
                            mNewsAdapter.addDatas(newses);
                            Log.i("====>>>", "page===>>>" + page + "size==>>" + newses.size());
                        },
                        throwable -> {
                            mFooterViewWrapper.showLoadingFailed();
                            getDataFailed(throwable.toString());
                        });
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
            mTextLoadingFailed.setText("加载失败，点击重新加载!");
        }

        public void showLoadingNoMoreData() {
            mCircleProgressBar.setVisibility(View.INVISIBLE);
            mTextLoadingFailed.setVisibility(View.VISIBLE);
            mTextLoadingFailed.setText("已经到底了,没有更多数据了哟!");
        }

        public void showLoadingNoData() {
            mCircleProgressBar.setVisibility(View.INVISIBLE);
            mTextLoadingFailed.setVisibility(View.VISIBLE);
            mTextLoadingFailed.setText("还没有数据哟,点击发送吧！");
        }

        public void onFailedClick(View.OnClickListener onClickListener) {
            mTextLoadingFailed.setOnClickListener(onClickListener::onClick);
        }

    }
}
