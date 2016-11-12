package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.AboutMe;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.activity.social.SpecificNewsActivity;
import com.mredrock.cyxbs.ui.adapter.me.AboutMeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutMeActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener, AboutMeAdapter.OnItemClickListener {

    @Bind(R.id.relate_me_recycler_View)
    RecyclerView aboutMeRecyclerView;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.about_me_swipe_refresh)
    SwipeRefreshLayout aboutMeSwipeRefresh;

    private List<AboutMe> mAboutMeList;
    private AboutMeAdapter mAboutMeAdapter;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relate_me);
        ButterKnife.bind(this);
        initToolbar();
        init();
        mAboutMeAdapter.setOnItemClickListener(this);

        mUser = APP.getUser(this);

        getCurrentData(false);
        showProgress();
    }

    @Override
    public void onRefresh() {
        getCurrentData(true);
    }


    @Override
    public void onItemClick(View itemView, int position, AboutMe aboutMe) {
        SpecificNewsActivity.startActivityWithArticleId(this, aboutMe.article_id, false, true);
    }

    private void init() {
        aboutMeSwipeRefresh.setOnRefreshListener(this);
        aboutMeSwipeRefresh.setColorSchemeColors(ContextCompat.getColor
                (this, R.color.colorAccent), ContextCompat.getColor
                (this, R.color.colorPrimary));

        mAboutMeList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        aboutMeRecyclerView.setLayoutManager(linearLayoutManager);

        mAboutMeAdapter = new AboutMeAdapter(mAboutMeList, this);
        aboutMeRecyclerView.setAdapter(mAboutMeAdapter);
    }

    public void getCurrentData(boolean update) {
        RequestManager.getInstance().getAboutMeList(new SimpleSubscriber<>(this, new SubscriberListener<List<AboutMe>>() {
            @Override
            public boolean onError(Throwable e) {
                super.onError(e);
                dismissProgress();
                return false;
            }

            @Override
            public void onNext(List<AboutMe> aboutMes) {
                super.onNext(aboutMes);
                dismissProgress();
                mAboutMeList.clear();
                mAboutMeList.addAll(aboutMes);
                mAboutMeAdapter.notifyDataSetChanged();
            }
        }), mUser.stuNum, mUser.idNum);
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
        if (aboutMeSwipeRefresh.isRefreshing()) {
            aboutMeSwipeRefresh.setRefreshing(false);
        }
    }

}
