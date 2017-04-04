package com.mredrock.cyxbs.ui.activity.social;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.SearchView;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.social.TopicFragment;

import java.util.Arrays;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TopicActivity extends BaseActivity implements android.support.v7.widget.SearchView.OnQueryTextListener ,SearchView.SearchSwitchListener{

    public static final String TAG = TopicActivity.class.getSimpleName();

    @Bind(R.id.tb_topic)
    Toolbar mToolbar;
    @Bind(R.id.tl_topic)
    TabLayout mTlTopic;
    @Bind(R.id.vp_topic)
    ViewPager mVpTopic;
    @Bind(R.id.sv_topic)
    SearchView mSvTopic;

    private boolean mIsInSearch;
    TabPagerAdapter mSearchAdapter;
    TabPagerAdapter mTopicListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        SwipeBackHelper.getCurrentPage(this).setClosePercent(0.15f).setSwipeSensitivity(0.2f);
        initToolbar();
        mTlTopic.setupWithViewPager(mVpTopic);
        close();
        mSvTopic.addQueryListener(this);
        mSvTopic.addSearchSwitchListener(this);
    }

    private void initTopicListPage() {
        mTopicListAdapter = new TabPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(TopicFragment.newInstance(TopicFragment.TopicType.MY_TOPIC),
                        TopicFragment.newInstance(TopicFragment.TopicType.ALL_TOPIC)),
                Arrays.asList("我参与的", "全部话题"));
        mVpTopic.setAdapter(mTopicListAdapter);
    }

    private void initSearchPage(String query) {
        mSearchAdapter = new TabPagerAdapter(getSupportFragmentManager(),
                Collections.singletonList(TopicFragment.newInstance(query)),
                Collections.singletonList("搜索结果"));
        mVpTopic.setAdapter(mSearchAdapter);
    }

    private void initToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(
                    v -> this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        initSearchPage(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void close() {
        mIsInSearch = false;
        mTlTopic.setVisibility(View.VISIBLE);
        initTopicListPage();
    }

    @Override
    public void onSearch() {
        mIsInSearch = true;
        mTlTopic.setVisibility(View.GONE);
        mVpTopic.setAdapter(null);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mIsInSearch) {
//                close();
//            } else {
//                finish();
//            }
//        }
//        return true;
//    }
}
