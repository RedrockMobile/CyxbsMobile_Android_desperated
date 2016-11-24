package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.NoCourse;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.me.NoCourseItemFragment;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoCourseContainerActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.no_course_tab_layout)
    TabLayout noCourseTabLayout;
    @Bind(R.id.no_course_view_pager)
    ViewPager noCourseViewPager;


    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private List<NoCourse> mNoCourseList;

    private TabPagerAdapter mTabPagerAdapter;

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
        setContentView(R.layout.activity_no_course_container);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this, 50);
        initToolbar();
        initViewPager();
        initView();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("没课约");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(v -> NoCourseContainerActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    private void initViewPager() {
        mTitleList = Arrays.asList(getResources().getStringArray(R.array.titles_weeks));
        int week = new SchoolCalendar().getWeekOfTerm();
        if (week >= 1 && week <= 23) {
            mTitleList.set(week, "本周");
        }
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTitleList.size(); i++) {
            NoCourseItemFragment noCourseItemFragment = NoCourseItemFragment
                    .newInstance(i);
            mFragmentList.add(noCourseItemFragment);
        }

        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),
                mFragmentList, mTitleList);
        noCourseViewPager.setAdapter(mTabPagerAdapter);
        noCourseViewPager.addOnPageChangeListener(new TabLayout
                .TabLayoutOnPageChangeListener(noCourseTabLayout));
        noCourseTabLayout.setupWithViewPager(noCourseViewPager);
        noCourseViewPager.setCurrentItem(week);
    }

    private void initView() {

    }
}
