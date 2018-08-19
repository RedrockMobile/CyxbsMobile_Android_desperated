package com.mredrock.cyxbs.freshman.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.ui.adapter.MyFragmentPagerAdapter;
import com.mredrock.cyxbs.freshman.ui.fragment.MilitaryShowFragment;
import com.mredrock.cyxbs.freshman.ui.fragment.MilitaryTipsFragment;
import com.mredrock.cyxbs.freshman.utils.TabLayoutUtil;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 军训特辑主界面，由于没有数据加载，只有简单的控件初始化，故不使用mvp
 */
public class MilitaryTrainingActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findById();
        initView();
    }

    private void findById() {
        tabLayout = findViewById(R.id.freshman_military_tl);
        viewPager = findViewById(R.id.freshman_military_vp);
        tabLayout.post(() -> TabLayoutUtil.setIndicator(tabLayout, 40, 40));
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        fragments.add(new MilitaryShowFragment());
        fragments.add(new MilitaryTipsFragment());
        titles.add("军训风采");
        titles.add("小贴士");
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(viewPager.getChildCount());
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.freshman_activity_military_training;
    }

    @NotNull
    @Override
    public String getToolbarTitle() {
        return Const.INDEX_MILITARY_TRAINING_TITLE;
    }
}
