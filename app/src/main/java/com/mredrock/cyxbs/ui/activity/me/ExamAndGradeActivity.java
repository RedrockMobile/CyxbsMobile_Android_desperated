package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.me.ExamScheduleFragment;
import com.mredrock.cyxbs.ui.fragment.me.GradeFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamAndGradeActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.exam_grade_tab_layout)
    TabLayout examGradeTabLayout;
    @BindView(R.id.exam_grade_view_pager)
    ViewPager examGradeViewPager;

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
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
        setContentView(R.layout.activity_exam_and_grade);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        ButterKnife.bind(this);
        initToolbar();
        initViewPager();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbarTitle.setText("考试与成绩");
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(
                    v -> ExamAndGradeActivity.this.finish());
        }
    }

    private void initViewPager() {
        mTitleList = Arrays.asList(getResources().getStringArray(R.array
                .exam_grade_tab_titles));
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new GradeFragment());
        mFragmentList.add(ExamScheduleFragment.newInstance(false));
        mFragmentList.add(ExamScheduleFragment.newInstance(true));

        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),
                mFragmentList, mTitleList);
        examGradeViewPager.setAdapter(mTabPagerAdapter);
        examGradeViewPager.addOnPageChangeListener(new TabLayout
                .TabLayoutOnPageChangeListener(examGradeTabLayout));
        examGradeTabLayout.setupWithViewPager(examGradeViewPager);
        examGradeViewPager.setOffscreenPageLimit(3);

        examGradeViewPager.setCurrentItem(0, true);
    }
}
