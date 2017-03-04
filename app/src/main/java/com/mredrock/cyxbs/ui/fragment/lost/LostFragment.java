package com.mredrock.cyxbs.ui.fragment.lost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.lost.Lost;
import com.mredrock.cyxbs.model.lost.LostDetail;
import com.mredrock.cyxbs.model.lost.LostWrapper;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.EndlessRecyclerOnScrollListener;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.lost.LostActivity;
import com.mredrock.cyxbs.ui.activity.lost.LostDetailsActivity;
import com.mredrock.cyxbs.ui.activity.lost.ReleaseActivity;
import com.mredrock.cyxbs.ui.adapter.LostAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.ui.fragment.BaseLazyFragment;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.RxBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGUMENT_THEME = "theme";
    public static final String ARGUMENT_CATEGORY = "category";
    public static final String TAG = "LostFragment";
    public static final int FIRST_PAGE_INDEX = 1;


    int theme = LostActivity.THEME_LOST;
    String category;
    private boolean hasLoginStateChanged = false;
    private LinearLayoutManager mLinearLayoutManager;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    public int currentIndex = 0;
    public LostAdapter mAdapter;
    private FooterViewWrapper mFooterViewWrapper;
    private List<Lost>mLostlist = new ArrayList<>();
    private Subscription mSubscription;


    @Bind(R.id.information_RecyclerView) RecyclerView recycler;
    @Bind(R.id.fab_main) FloatingActionButton mFabMain;
    @Bind(R.id.information_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments;
        if ((arguments = getArguments()) != null) {
            theme = arguments.getInt(ARGUMENT_THEME);
            category = arguments.getString(ARGUMENT_CATEGORY);
        } else {
            category = getResources().getStringArray(R.array.lost_category_list)[0];
        }
        if (category.equals("全部")){
            category = "all";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "show me your floating ", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        init();
        mFabMain.setOnClickListener(view1 -> {
            if (APP.getUser(getActivity()).id == null || APP.getUser(getActivity()).id.equals("0")) {
                RequestManager.getInstance().checkWithUserId("还没有完善信息，不能发动态哟！");
            } else
                ReleaseActivity.startActivity(getActivity());
        });
    }
    public void init(){
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(APP.getContext(), R.color.colorAccent),
                ContextCompat.getColor(APP.getContext(), R.color.colorPrimary)
        );
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        recycler.setLayoutManager(mLinearLayoutManager);

        addOnScrollListener();
        mAdapter = new LostAdapter(mLostlist,getContext());
        initAdapter(null);
        registerObservable();
    }
    @Override
    protected void onFirstUserVisible() {
        getCurrentData(theme,category, FIRST_PAGE_INDEX);
    }

    public void provideData(Subscriber<LostWrapper<List<Lost>>>subscriber, int theme, String category, int page){
        RequestManager.getInstance().getLostList(subscriber,theme,category,page);
    }
    private void addOnScrollListener() {
        if (endlessRecyclerOnScrollListener != null)
            recycler.removeOnScrollListener(endlessRecyclerOnScrollListener);
            endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                currentIndex++;
                getNextPageData(theme,category, currentIndex);
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
        recycler.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    public void onRefresh() {
        getCurrentData(theme, category,FIRST_PAGE_INDEX);
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

    public void getCurrentData(int theme,String category,int page){
        mSwipeRefreshLayout.post(this::showLoadingProgress);
        provideData(new SimpleSubscriber<LostWrapper<List<Lost>>>(getActivity(), new SubscriberListener<LostWrapper<List<Lost>>>() {
            @Override
            public boolean onError(Throwable e) {
                super.onError(e);
                mFooterViewWrapper.showLoadingFailed();
                closeLoadingProgress();
                return false;
            }

            @Override
            public void onNext(LostWrapper<List<Lost>> losts) {
                super.onNext(losts);
                if (mLostlist == null) {
                    mAdapter = new LostAdapter(losts.data, getContext());
                    initAdapter(losts.data);
                    if (losts.data.size() == 0) mFooterViewWrapper.showLoadingNoData();
                }else mAdapter.replaceDataList(losts.data);
                LogUtils.LOGI("====>>>", "page===>>>" + page + "size==>>" + losts.data.size());
                closeLoadingProgress();
            }
        }),theme,category,page);
    }

    private void initAdapter(List<Lost> lostList){
        if (recycler == null)return;
        mLostlist = lostList;

        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LostAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Lost lost) {
                RequestManager.getInstance().getLostDetail(new SimpleSubscriber<LostDetail>(getContext(), new SubscriberListener<LostDetail>() {
                    @Override
                    public boolean onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(getContext(), "抱歉，加载数据失败，请检查网络再试", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onNext(LostDetail lostDetail) {
                        super.onNext(lostDetail);
                        Intent intent = new Intent(getActivity(),LostDetailsActivity.class);
                        intent.putExtra("LostDetail",lostDetail);
                        startActivity(intent);
                    }
                }),lost);
            }
        });
        mFooterViewWrapper = new FooterViewWrapper(getContext(),recycler);
        mFooterViewWrapper.onFailedClick(view -> {
            if (currentIndex == 0)getCurrentData(theme,category,currentIndex);
            getNextPageData(theme,category,currentIndex);
        });
        mFooterViewWrapper.mCircleProgressBar.setVisibility(View.INVISIBLE);
    }

    public void getNextPageData(int theme,String category,int page){
        mFooterViewWrapper.showLoading();
        provideData(new SimpleSubscriber<LostWrapper<List<Lost>>>(getContext(), new SubscriberListener<LostWrapper<List<Lost>>>() {
            @Override
            public boolean onError(Throwable e) {
                 super.onError(e);
                mFooterViewWrapper.showLoadingFailed();
                return true;
            }

            @Override
            public void onNext(LostWrapper<List<Lost>> lost) {
                super.onNext(lost);
                if (lost.data.size() == 0){
                    mFooterViewWrapper.showLoadingNoData();
                    return;
                }
                mAdapter.addDataList(lost.data);
            }
        }),theme,category,page);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (hasLoginStateChanged) {
            mLostlist.clear();
            getCurrentData(theme,category, 0);
            hasLoginStateChanged = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        unregisterObservable();
    }


    public static class FooterViewWrapper {

        @Bind(R.id.progressBar)
        CircleProgressBar mCircleProgressBar;
        @Bind(R.id.textLoadingFailed)
        TextView mTextLoadingFailed;

        private View footerView;

        public FooterViewWrapper(Context context, ViewGroup parent) {
            footerView = LayoutInflater.from(context)
                    .inflate(R.layout.list_footer_item_news, parent, false);
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
            mTextLoadingFailed.setText("没有更多内容啦，你来发布吧！");
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
    private void registerObservable() {
        mSubscription = RxBus.getDefault()
                .toObserverable(HotNews.class)
                .subscribe(s -> {
                    getCurrentData(theme,category,FIRST_PAGE_INDEX);
                    recycler.scrollToPosition(0);
                });
    }

    private void unregisterObservable() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
    @Override
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        super.onLoginStateChangeEvent(event);
        hasLoginStateChanged = true;
    }

}
