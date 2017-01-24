package com.mredrock.cyxbs.ui.activity.lost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.lost.AllLostFragment;
import com.mredrock.cyxbs.ui.fragment.lost.CardFragment;
import com.mredrock.cyxbs.ui.fragment.lost.ClotheFragment;
import com.mredrock.cyxbs.ui.fragment.lost.ElectronicFragment;
import com.mredrock.cyxbs.ui.fragment.lost.KeyFragment;
import com.mredrock.cyxbs.ui.fragment.lost.OtherFragment;
import com.mredrock.cyxbs.ui.fragment.lost.UmbrellaFragment;
import com.mredrock.cyxbs.ui.fragment.lost.WalletFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mredrock.cyxbs.ui.adapter.lost.LostViewPagerAdapter;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostActivity extends BaseActivity {

    @Bind(R.id.toolbar) RelativeLayout toolbar;
    @Bind(R.id.rb_lost) RadioButton chooseModeLost;
    @Bind(R.id.rb_found) RadioButton chooseModeFound;

    @Bind(R.id.view_pager) ViewPager pager;
    @Bind(R.id.tab_layout) TabLayout tab;

    LostViewPagerAdapter adapter;
    String[] lostKindList;

    public static final int THEME_LOST = 0;
    public static final int THEME_FOUND = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        ButterKnife.bind(this);

        init();

        lostKindList = getResources().getStringArray(R.array.lost_category_list);
        StatusBarUtil.setTranslucent(this, 50);
        adapter = new LostViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        tab.setupWithViewPager(pager, true);
        chooseModeLost.setOnClickListener((v) -> refreshMode());
        chooseModeFound.setOnClickListener((v) -> refreshMode());
    }

    private void refreshMode() {
        adapter.setMode(getMode());
    }

    private int getMode() {
        if (chooseModeLost.isChecked()) {
            return THEME_LOST;
        } else {
            return THEME_FOUND;
        }
    }

    @OnClick(R.id.bt_navigation_up)
    public void onNavigationUpClick() {
        finish();
    }


    public void init(){
        List<Fragment> fragmentList = new ArrayList<>();
        AllLostFragment mAllLostFragment = new AllLostFragment();
        CardFragment mCardFragment = new CardFragment();
        ClotheFragment mClotheFragment = new ClotheFragment();
        ElectronicFragment mElectronicFragment = new ElectronicFragment();
        KeyFragment mKeyFragment = new KeyFragment();
        OtherFragment mOtherFragment = new OtherFragment();
        UmbrellaFragment mUmbrellaFragment = new UmbrellaFragment();
        WalletFragment mWalletFragment = new WalletFragment();

        fragmentList.add(mAllLostFragment);
        fragmentList.add(mCardFragment);
        fragmentList.add(mWalletFragment);
        fragmentList.add(mKeyFragment);
        fragmentList.add(mElectronicFragment);
        fragmentList.add(mUmbrellaFragment);
        fragmentList.add(mClotheFragment);
        fragmentList.add(mOtherFragment);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(),fragmentList, Arrays
        .asList(getResources().getStringArray(R.array.lost_tab_tiles)));
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragmentList.size());
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setupWithViewPager(pager);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LostActivity.class);
        context.startActivity(starter);
    }

}
