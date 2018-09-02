package com.mredrock.cyxbs.ui.fragment.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.ItemChangedEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.EndlessRecyclerOnScrollListener;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.social.FooterViewWrapper;
import com.mredrock.cyxbs.ui.activity.social.HeaderViewWrapper;
import com.mredrock.cyxbs.ui.activity.social.PostNewsActivity;
import com.mredrock.cyxbs.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseLazyFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;


/**
 * Created by mathiasluo on 16-4-26.
 */
public abstract class BaseNewsFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final int PER_PAGE_NUM = 10;
    public static final String TAG = "BaseNewsFragment";
    public static final int FIRST_PAGE_INDEX = 0;
    @BindView(R.id.fab_main)
    FloatingActionButton mFabMain;

    private boolean hasLoginStateChanged = false;

    @BindView(R.id.information_RecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.information_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public int currentIndex = 0;
    private List<HotNews> mListHotNews;
    private FooterViewWrapper mFooterViewWrapper;

    protected NewsAdapter mNewsAdapter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    abstract void provideData(Observer<List<HotNews>> observer, int size, int page);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        mFabMain.setOnClickListener(view1 -> {
            if (BaseAPP.getUser(getActivity()).id == null || BaseAPP.getUser(getActivity()).id.equals("0")) {
                RequestManager.getInstance().checkWithUserId("还没有完善信息，不能发动态哟！");
            } else
                PostNewsActivity.startActivity(getActivity());
        });
    }

    protected void init() {
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(BaseAPP.getContext(), R.color.colorAccent),
                ContextCompat.getColor(BaseAPP.getContext(), R.color.colorPrimary)
        );
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getParentFragment().getActivity());

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        addOnScrollListener();
        initAdapter(null);
    }

    @Override
    protected void onFirstUserVisible() {
        getCurrentData(PER_PAGE_NUM, FIRST_PAGE_INDEX);
    }

    private void addOnScrollListener() {
        if (endlessRecyclerOnScrollListener != null)
            mRecyclerView.removeOnScrollListener(endlessRecyclerOnScrollListener);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                currentIndex++;
                getNextPageData(PER_PAGE_NUM, currentIndex);
            }

            @Override
            public void onShow() {
                mFabMain.show();
            }

            @Override
            public void onHide() {
                mFabMain.hide();
            }
        };
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    public void onRefresh() {
        getCurrentData(PER_PAGE_NUM, FIRST_PAGE_INDEX);
        currentIndex = 0;
        addOnScrollListener();
    }

    private void showLoadingProgress() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    private void closeLoadingProgress() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void getCurrentData(int size, int page) {
        mSwipeRefreshLayout.post(this::showLoadingProgress);
        provideData(new SimpleObserver<>(getActivity(), new SubscriberListener<List<HotNews>>() {
            @Override
            public boolean onError(Throwable e) {
                super.onError(e);
                mFooterViewWrapper.showLoadingFailed();
                closeLoadingProgress();
                return false;
            }

            @Override
            public void onNext(List<HotNews> hotNewses) {
                super.onNext(hotNewses);
                if (mListHotNews == null) {
                    initAdapter(hotNewses);
                    if (hotNewses.size() == 0) mFooterViewWrapper.showLoadingNoData();
                } else mNewsAdapter.replaceDataList(hotNewses);
                Log.i("====>>>", "page===>>>" + page + "size==>>" + hotNewses.size());
                closeLoadingProgress();
            }
        }), size, page);
    }


    public void initAdapter(List<HotNews> listHotNews) {
        if (mRecyclerView == null) return;  // prevent it be called before lazy loading
        mListHotNews = listHotNews;
        mNewsAdapter = new NewsAdapter(mListHotNews) {
            @Override
            public void setDate(NewsViewHolder holder, HotNewsContent hotNewsContent) {
                BaseNewsFragment.this.setDate(holder, hotNewsContent);
            }
        };
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mNewsAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        addFooterView(mHeaderViewRecyclerAdapter);
        mFooterViewWrapper.getCircleProgressBar().setVisibility(View.INVISIBLE);
    }

    protected void setDate(NewsAdapter.NewsViewHolder holder, HotNewsContent mDataBean) {
    }

    private void addFooterView(HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter) {
        mFooterViewWrapper = new FooterViewWrapper(mRecyclerView);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterViewWrapper.getFooterView());
        mFooterViewWrapper.onFailedClick(view -> {
            if (currentIndex == 0) getCurrentData(PER_PAGE_NUM, currentIndex);
            getNextPageData(PER_PAGE_NUM, currentIndex);
        });
    }

    public void addHeaderView() {
        mHeaderViewRecyclerAdapter.addHeaderView(new HeaderViewWrapper(mRecyclerView, getContext()).getView());
    }

    private void getNextPageData(int size, int page) {
        mFooterViewWrapper.showLoading();
        provideData(new SimpleObserver<>(getContext(), new SubscriberListener<List<HotNews>>() {
            @Override
            public boolean onError(Throwable e) {
                super.onError(e);
                mFooterViewWrapper.showLoadingFailed();
                return true;
            }

            @Override
            public void onNext(List<HotNews> hotNewses) {
                super.onNext(hotNewses);
                if (hotNewses.size() == 0) {
                    mFooterViewWrapper.showLoadingNoMoreData();
                    return;
                }
                mNewsAdapter.addDataList(hotNewses);
                //Log.i("====>>>", "page===>>>" + page + "size==>>" + hotNewses.size());
            }
        }), size, page);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasLoginStateChanged) {
            getCurrentData(PER_PAGE_NUM, 0);
            hasLoginStateChanged = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        super.onLoginStateChangeEvent(event);
        hasLoginStateChanged = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemChangedEvent(ItemChangedEvent event) {
        if (mListHotNews == null)
            return;
        int index = -1;
        for (HotNews hotNews : mListHotNews) {
            index++;
            if (hotNews.data.articleId.equals(event.getArticleId())) {
                /*Log.i("onItemChangedEvent","index ="+ index
                        +"   "+event.getArticleId()+"hotNews.data.isMyLike"+ hotNews.data.isMyLike
                        +"  event.isMyLike()"+event.isMyLike());*/
                hotNews.data.isMyLike = event.isMyLike();
                hotNews.data.likeNum = event.getNum();
                mNewsAdapter.notifyItemChanged(index);
            }

        }
    }
}
