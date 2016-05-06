package com.mredrock.cyxbsmobile.ui.activity.me;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.me.ExamScheduleFragment;
import com.mredrock.cyxbsmobile.ui.fragment.me.GradeFragment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamAndGradeActivity extends BaseActivity {

    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.exam_grade_tab_layout) TabLayout examGradeTabLayout;
    @Bind(R.id.exam_grade_view_pager) ViewPager examGradeViewPager;

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    private TabPagerAdapter mTabPagerAdapter;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_and_grade);
        ButterKnife.bind(this);
        initToolbar();
        initViewPager();
    }

    private void initToolbar() {
        if(toolbar != null){
            toolbar.setTitle("");
            toolbarTitle.setText("考试与成绩");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> ExamAndGradeActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
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
                mFragmentList,mTitleList);
        examGradeViewPager.setAdapter(mTabPagerAdapter);
        examGradeViewPager.addOnPageChangeListener(new TabLayout
                .TabLayoutOnPageChangeListener(examGradeTabLayout));
        examGradeTabLayout.setupWithViewPager(examGradeViewPager);

        examGradeViewPager.setCurrentItem(0,true);
    }
}
