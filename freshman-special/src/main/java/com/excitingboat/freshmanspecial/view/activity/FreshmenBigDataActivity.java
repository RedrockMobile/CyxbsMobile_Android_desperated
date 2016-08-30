package com.excitingboat.freshmanspecial.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.view.adapter.BigDataAdapter_1;
import com.excitingboat.freshmanspecial.view.adapter.BigDataAdapter_2;
import com.excitingboat.freshmanspecial.view.adapter.FreshmanPagerAdapter;
import com.excitingboat.freshmanspecial.view.fragment.BigData.BigDataFragmentCake_1;
import com.excitingboat.freshmanspecial.view.fragment.BigData.BigDataFragmentCake_2;

import java.util.ArrayList;

public class FreshmenBigDataActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private String[] titles = {"男女比例", "最难科目"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_freshman_special__activity_freshmen_big_data);
        init();
    }

    private void init() {
        findViewById(R.id.bt_toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshmenBigDataActivity.this.finish();
            }
        });
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.freshman_big_data);

        fragments = new ArrayList<>();
        BigDataFragmentCake_1 fragment1 = new BigDataFragmentCake_1();
        BigDataFragmentCake_2 fragment2 = new BigDataFragmentCake_2();
        //BigDataFragmentCake_2 fragment3 = new BigDataFragmentCake_2();
        fragment1.setBigDataAdapter(new BigDataAdapter_1());
        fragment2.setBigDataAdapter(new BigDataAdapter_2());
        //fragment3.setBigDataAdapter(new BigDataAdapter_3());
        fragments.add(fragment1);
        fragments.add(fragment2);
        //fragments.add(fragment3);
        tabLayout = (TabLayout) findViewById(R.id.freshmen_big_data_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.freshmen_big_data_viewPager);
        tabLayout.setupWithViewPager(viewPager);
        FreshmanPagerAdapter adapter = new FreshmanPagerAdapter(getSupportFragmentManager());
        adapter.setTitles(titles);
        adapter.setFragments(fragments);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);
    }
}
