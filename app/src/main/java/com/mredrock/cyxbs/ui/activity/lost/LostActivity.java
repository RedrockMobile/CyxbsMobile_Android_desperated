package com.mredrock.cyxbs.ui.activity.lost;

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
    @Bind(R.id.lost_sliding_tabs)
    TabLayout mTabLayout;
    @Bind(R.id.lost_viewpager)
    ViewPager mViewPager;
    public static final int MODE_LOST = 0;
    public static final int MODE_FOUND = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        ButterKnife.bind(this);
        init();
        StatusBarUtil.setTranslucent(this, 50);
    }

    private void refreshMode() {

    }

    private int getMode() {
        if (chooseModeLost.isChecked()) {
            return MODE_LOST;
        } else {
            return MODE_FOUND;
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
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragmentList.size());
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
