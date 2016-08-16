package com.excitingboat.freshmanspecial.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.view.adapter.FreshmanPagerAdapter;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.AroundFoodFragment;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.AroundViewFragment;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.DailyLifeFragment;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.DormitorySituationFragment;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.EnrolInformationFragment;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.EnrolWayFragment;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.NecessaryList;
import com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide.QQGroup;

import java.util.ArrayList;

public class FreshmenGuideActivity extends AppCompatActivity {
    ImageButton back;
    TextView title;
    TabLayout tabLayout;
    ViewPager viewPager;
    FreshmanPagerAdapter freshmanPagerAdapter;
    //设置ViewPager的page_id
    int ViewPagerId[] = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    //储存fragment的数组
    private ArrayList<Fragment> mFragments;
    //tab条目中的标题
    private String[] titles = {"安全须知", "须知路线", "寝室概况", "必备清单", "QQ群", "日常生活", "周边美食", "周边美景"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_freshman_special__activity_freshmen_guide);
        initView();
    }

    private void initView() {
        initToolbar();

        initTab();
    }


    private void initToolbar() {
        back = (ImageButton) findViewById(R.id.bt_toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showBigPicture(view);
                finish();
            }
        });

        title = (TextView) findViewById(R.id.tv_toolbar_title);
        title.setText("新生攻略");

    }

    private void initTab() {
        tabLayout = (TabLayout) findViewById(R.id.freshmen_guide_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.freshmen_guide_viewPager);
        freshmanPagerAdapter = new FreshmanPagerAdapter(getSupportFragmentManager());
        freshmanPagerAdapter.setTitles(titles);
        mFragments = new ArrayList<>();

        mFragments.add(new EnrolInformationFragment());
        mFragments.add(new EnrolWayFragment());
        mFragments.add(new DormitorySituationFragment());
        mFragments.add(new NecessaryList());
        mFragments.add(new QQGroup());
        mFragments.add(new DailyLifeFragment());
        mFragments.add(new AroundFoodFragment());
        mFragments.add(new AroundViewFragment());

        freshmanPagerAdapter.setFragments(mFragments);

        //设置tablayout的模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //给viewPager设置适配器
        viewPager.setAdapter(freshmanPagerAdapter);
        //TabLayout绑定ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }


}
