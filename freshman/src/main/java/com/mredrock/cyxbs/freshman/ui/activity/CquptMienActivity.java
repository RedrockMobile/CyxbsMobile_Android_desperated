package com.mredrock.cyxbs.freshman.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.ui.adapter.CustomVIewPager;
import com.mredrock.cyxbs.freshman.ui.adapter.MyFragmentPagerAdapter;
import com.mredrock.cyxbs.freshman.ui.fragment.CquptMienActFragment;
import com.mredrock.cyxbs.freshman.ui.fragment.CquptMienBaseFragment;
import com.mredrock.cyxbs.freshman.utils.DensityUtils;
import com.mredrock.cyxbs.freshman.utils.TabLayoutUtil;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 重邮风采主界面，由于没有数据加载，只有简单的控件初始化，故不使用mvp
 */

public class CquptMienActivity extends BaseActivity {

    private CustomVIewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findById();
        initVP();
    }

    private void findById() {
        viewPager = findViewById(R.id.freshman_CyMien_vp);
        tabLayout = findViewById(R.id.freshman_CyMien_tl);
    }

    private void initVP() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        CquptMienBaseFragment fragment = new CquptMienBaseFragment();
        fragment.setContext(this);
        fragments.add(fragment);
        fragments.add(new CquptMienActFragment());
        titles.add("学生组织");
        titles.add("大型活动");
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(viewPager.getChildCount());
        viewPager.setScanScroll(false);//设置不能滑动
        tabLayout.setupWithViewPager(viewPager);
        TabLayoutUtil.setIndicator(tabLayout, 50, 50);

        ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
        layoutParams.height = DensityUtils.getScreenHeight(this) / 16;
        tabLayout.setLayoutParams(layoutParams);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.freshman_activity_cqupt_mien;
    }

    @NotNull
    @Override
    public String getToolbarTitle() {
        return Const.INDEX_MIEN;
    }
}
