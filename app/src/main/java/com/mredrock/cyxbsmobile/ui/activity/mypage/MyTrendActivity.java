package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.activity.SpecificNewsActivity;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;
import com.mredrock.cyxbsmobile.util.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class MyTrendActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,NewsAdapter.OnItemOnClickListener {

    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.my_trend_recycler_view) RecyclerView myTrendRecyclerView;
    @Bind(R.id.my_trend_refresh_layout) SwipeRefreshLayout myTrendRefreshLayout;

    private List<News> mNewsList;
    private NewsAdapter mNewsAdapter;
    private User mUser;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trend);
        ButterKnife.bind(this);
        initToolbar();
        init();

        mUser = getIntent().getParcelableExtra(EditInfoActivity.EXTRA_USER);
        mUser.idNum = "26722X";

        getMyTrendData(false);
        showProgress();
    }


    @Override public void onRefresh() {
        getMyTrendData(true);
    }

    @Override
    public void onItemClick(View itemView, int position, News.DataBean dataBean) {
        Intent intent = new Intent(this, SpecificNewsActivity.class);
        intent.putExtra("dataBean",dataBean);
        startActivity(intent);
    }

    private void init() {
        myTrendRefreshLayout.setOnRefreshListener(this);
        myTrendRefreshLayout.setColorSchemeColors(ContextCompat.getColor
                (this,R.color.colorAccent),ContextCompat.getColor
                (this,R.color.colorPrimary));

        mNewsList = new ArrayList<>();
        myTrendRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewsAdapter = new NewsAdapter(mNewsList){
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                ImageLoader.getInstance().loadAvatar(mUser
                        .photo_thumbnail_src,holder.mImgAvatar);
                holder.mTextName.setText(mUser.nickname.equals("") ? mUser
                        .stunum : mUser.nickname);
            }
        };
        myTrendRecyclerView.setAdapter(mNewsAdapter);
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
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


    private void getMyTrendData(boolean update) {
        RequestManager.getInstance()
                      .getMyTrend(mUser.stunum, mUser.idNum, update)
                      .subscribe(newses -> {
                          dismissProgress();
                          mNewsList.clear();
                          mNewsList.addAll(newses);
                          mNewsAdapter.notifyDataSetChanged();
                      }, throwable -> {
                          dismissProgress();
                          getDataFailed(throwable.getMessage());
                      });
    }


    private void showProgress() {
        myTrendRefreshLayout.getViewTreeObserver()
                                  .addOnGlobalLayoutListener(
                                          new ViewTreeObserver.OnGlobalLayoutListener() {
                                              @Override
                                              public void onGlobalLayout() {
                                                  myTrendRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                                  myTrendRefreshLayout.setRefreshing(true);
                                                  getMyTrendData(true);
                                              }
                                          });
    }


    private void dismissProgress() {
        if (myTrendRefreshLayout.isRefreshing()) {
            myTrendRefreshLayout.setRefreshing(false);
        }
    }


    private void getDataFailed(String reason) {
        Toast.makeText(MyTrendActivity.this, "获取数据失败，原因:" + reason, Toast.LENGTH_SHORT).show();
    }
}
