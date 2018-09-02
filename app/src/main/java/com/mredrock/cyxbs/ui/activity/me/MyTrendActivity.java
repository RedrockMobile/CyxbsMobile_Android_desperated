package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;
import com.mredrock.cyxbs.util.ImageLoader;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTrendActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_trend_recycler_view)
    RecyclerView myTrendRecyclerView;
    @BindView(R.id.my_trend_refresh_layout)
    SwipeRefreshLayout myTrendRefreshLayout;

    private List<HotNews> mNewsList;
    private NewsAdapter mNewsAdapter;
    private User mUser;


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trend);
        ButterKnife.bind(this);
        initToolbar();
        init();
        showProgress();
    }

    @Override
    public void onRefresh() {
        getMyTrendData();
    }


    private void init() {
        mUser = BaseAPP.getUser(this);
        myTrendRefreshLayout.setOnRefreshListener(this);

        myTrendRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimary)
        );

        mNewsList = new ArrayList<>();
        myTrendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new NewsAdapter(mNewsList) {
            @Override
            public void onBindViewHolder(NewsViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ImageLoader.getInstance().loadAvatar(mUser.photo_thumbnail_src, holder.mImgAvatar);
                holder.mTextName.setText(mUser.getNickname());
                holder.mBtnFavor.setOnClickListener(null);
                holder.isFromMyTrend = true;
            }

            @Override
            public void setDate(NewsViewHolder holder, HotNewsContent mDataBean) {
                super.setDate(holder, mDataBean);
                holder.isFromMyTrend = true;
            }
        };
        myTrendRecyclerView.setAdapter(mNewsAdapter);
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbarTitle.setText("我的动态");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> MyTrendActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }


    private void getMyTrendData() {
        if (mUser != null) {
            Logger.d(mUser.toString());
            RequestManager.getInstance().getMyTrend(new SimpleObserver<>(this, new SubscriberListener<List<HotNews>>() {
                @Override
                public boolean onError(Throwable e) {
                    super.onError(e);
                    dismissProgress();
                    return false;
                }

                @Override
                public void onNext(List<HotNews> hotNewses) {
                    super.onNext(hotNewses);
                    dismissProgress();
                    mNewsList.clear();
                    mNewsList.addAll(hotNewses);
                    mNewsAdapter.notifyDataSetChanged();
                }
            }), mUser.stuNum, mUser.idNum);
            dismissProgress();
        }
    }


    private void showProgress() {
        if (mUser != null) {
            myTrendRefreshLayout.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            myTrendRefreshLayout.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                            myTrendRefreshLayout.setRefreshing(true);
                            getMyTrendData();
                        }
                    });
        }
    }

    private void dismissProgress() {
        if (myTrendRefreshLayout != null && myTrendRefreshLayout.isRefreshing()) {
            myTrendRefreshLayout.setRefreshing(false);
        }
    }
}
