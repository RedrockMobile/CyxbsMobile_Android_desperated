package com.mredrock.cyxbsmobile.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.AboutMe;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.activity.social.SpecificNewsActivity;
import com.mredrock.cyxbsmobile.ui.adapter.mypage.AboutMeAdapter;
import java.util.ArrayList;
import java.util.List;

public class AboutMeActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,AboutMeAdapter.OnItemClickListener{

    @Bind(R.id.relate_me_recycler_View) RecyclerView aboutMeRecyclerView;
    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.about_me_swipe_refresh) SwipeRefreshLayout aboutMeSwipeRefresh;

    private List<AboutMe> mAboutMeList;
    private AboutMeAdapter mAboutMeAdapter;

    private User mUser;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relate_me);
        ButterKnife.bind(this);
        initToolbar();
        init();
        mAboutMeAdapter.setOnItemClickListener(this);

        mUser = new User();
        mUser.stunum = "2014213983";
        mUser.idNum = "26722X";

        getCurrentData(false);
        showProgress();
    }

    @Override public void onRefresh() {
        getCurrentData(true);
    }


    @Override
    public void onItemClick(View itemView, int position, AboutMe aboutMe) {
        Intent intent = new Intent(this, SpecificNewsActivity.class);
        intent.putExtra("article_id",aboutMe.article_id);
        startActivity(intent);
    }

    private void init() {
        aboutMeSwipeRefresh.setOnRefreshListener(this);
        aboutMeSwipeRefresh.setColorSchemeColors(ContextCompat.getColor
                (this,R.color.colorAccent),ContextCompat.getColor
                (this,R.color.colorPrimary));

        mAboutMeList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        aboutMeRecyclerView.setLayoutManager(linearLayoutManager);

        mAboutMeAdapter = new AboutMeAdapter(mAboutMeList, this);
        aboutMeRecyclerView.setAdapter(mAboutMeAdapter);
    }

    public void getCurrentData(boolean update) {
        RequestManager.getInstance()
                      .getAboutMeList(mUser.stunum, mUser.idNum, update)
                      .subscribe(aboutMes -> {
                          dismissProgress();
                          mAboutMeList.clear();
                          mAboutMeList.addAll(aboutMes);
                          mAboutMeAdapter.notifyDataSetChanged();
                      }, throwable -> {
                          dismissProgress();
                          getDataFailed(throwable.getMessage());
                      });
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("与我相关");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> AboutMeActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    private void showProgress() {
        aboutMeSwipeRefresh.getViewTreeObserver()
                            .addOnGlobalLayoutListener(
                                    new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            aboutMeSwipeRefresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                            aboutMeSwipeRefresh.setRefreshing(true);
                                            getCurrentData(true);
                                        }
                                    });
    }


    private void dismissProgress() {
        if(aboutMeSwipeRefresh.isRefreshing()) {
            aboutMeSwipeRefresh.setRefreshing(false);
        }
    }

    private void getDataFailed(String reason){
        Toast.makeText(AboutMeActivity.this, "获取数据失败，原因:"+reason, Toast.LENGTH_SHORT).show();
    }
}
