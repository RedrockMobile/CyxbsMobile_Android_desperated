package com.excitingboat.freshmanspecial.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.net.GetInformation;
import com.excitingboat.freshmanspecial.presenter.GetInformationPresenter;
import com.excitingboat.freshmanspecial.view.adapter.FreshmanPagerAdapter;
import com.excitingboat.freshmanspecial.view.fragment.Style.BeautifulCQUPTFragment;
import com.excitingboat.freshmanspecial.view.fragment.Style.OrganizationFragment;
import com.excitingboat.freshmanspecial.view.fragment.Style.VideoListFragment;
import com.excitingboat.freshmanspecial.view.fragment.Style.StudentGridFragment;
import com.excitingboat.freshmanspecial.view.fragment.Style.TeacherGridFragment;

import java.util.ArrayList;

public class FreshmenCQUPTStyleActivity extends AppCompatActivity {

    private static final String TAG = "CQUPTStyleActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private String[] titles = {"学生组织", "原创重邮", "美在重邮", "学生代表", "教师代表"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_freshman_special__activity_freshmen_cqupt_style);
        init();
    }

    private void init() {
        findViewById(R.id.bt_toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshmenCQUPTStyleActivity.this.finish();
            }
        });
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.freshman_cqupt_style);


        fragments = new ArrayList<>();
        OrganizationFragment fragment1 = new OrganizationFragment();
        VideoListFragment fragment2 = new VideoListFragment();
        fragment2.setPresenter(this, new GetInformationPresenter<>(fragment2, GetInformation.VIDEO));
        BeautifulCQUPTFragment fragment3 = new BeautifulCQUPTFragment();
        StudentGridFragment fragment4 = new StudentGridFragment();
        fragment4.setPresenter(this, new GetInformationPresenter<>(fragment4, GetInformation.STUDENT));
        TeacherGridFragment fragment5 = new TeacherGridFragment();
        fragment5.setPresenter(this, new GetInformationPresenter<>(fragment5, GetInformation.TEACHER));
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        tabLayout = (TabLayout) findViewById(R.id.freshmen_cqupt_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.freshmen_cqupt_viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        FreshmanPagerAdapter adapter = new FreshmanPagerAdapter(getSupportFragmentManager());
        adapter.setTitles(titles);
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);

    }
}
