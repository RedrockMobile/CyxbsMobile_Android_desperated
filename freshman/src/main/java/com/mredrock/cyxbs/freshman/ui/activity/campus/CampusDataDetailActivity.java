package com.mredrock.cyxbs.freshman.ui.activity.campus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.ui.activity.BaseActivity;
import com.mredrock.cyxbs.freshman.ui.adapter.MyFragmentPagerAdapter;
import com.mredrock.cyxbs.freshman.ui.fragment.DataDetailSexFragment;
import com.mredrock.cyxbs.freshman.ui.fragment.DataDetailSubjectFragment;
import com.mredrock.cyxbs.freshman.utils.TabLayoutUtil;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CampusDataDetailActivity extends BaseActivity {

    public static void start(String name, Context context) {
        Intent intent = new Intent(context, CampusDataDetailActivity.class);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra("name");
        ViewPager mVp = findViewById(R.id.vp_data_detail);
        TabLayout mTl = findViewById(R.id.tl_data);

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        DataDetailSexFragment sex = new DataDetailSexFragment();
        sex.setData(name);
        fragments.add(sex);

        DataDetailSubjectFragment subject = new DataDetailSubjectFragment();
        subject.setName(name);
        fragments.add(subject);

        titles.add(getResources().getString(R.string.freshman_campus_data_detail_sex));
        titles.add(getResources().getString(R.string.freshman_campus_data_detail_difficult));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(mVp.getChildCount());
        mTl.setupWithViewPager(mVp);
        TabLayoutUtil.setIndicator(mTl, 40, 40);

    }


    @Override
    public int getLayoutResID() {
        return R.layout.freshman_activity_campus_data_datail;
    }

    @NotNull
    @Override
    public String getToolbarTitle() {
        return Const.INDEX_REVEAL;
    }
}
