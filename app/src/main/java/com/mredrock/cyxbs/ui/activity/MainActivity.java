package com.mredrock.cyxbs.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.bottombar.BottomBar;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.ui.activity.social.PostNewsActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbs.ui.fragment.UserFragment;
import com.mredrock.cyxbs.ui.fragment.explore.ExploreFragment;
import com.mredrock.cyxbs.ui.fragment.social.SocialContainerFragment;
import com.mredrock.cyxbs.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    @Bind(R.id.main_toolbar_title)
    TextView          mToolbarTitle;
    @Bind(R.id.main_toolbar)
    Toolbar           mToolbar;
    @Bind(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.bottom_bar)
    BottomBar         mBottomBar;
    @Bind(R.id.main_view_pager)
    ViewPager         mViewPager;

    @BindString(R.string.community)
    String mStringCommunity;
    @BindString(R.string.course)
    String mStringCourse;
    @BindString(R.string.explore)
    String mStringExplore;
    @BindString(R.string.my_page)
    String mStringMyPage;


    BaseFragment socialContainerFragment;
    BaseFragment courseContainerFragment;
    BaseFragment exploreFragment;
    BaseFragment userFragment;

    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar();
        socialContainerFragment = new SocialContainerFragment();
        courseContainerFragment = new CourseContainerFragment();
        exploreFragment = new ExploreFragment();
        userFragment = new UserFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(socialContainerFragment);
        fragments.add(courseContainerFragment);
        fragments.add(exploreFragment);
        fragments.add(userFragment);

        ArrayList<String> titles = new ArrayList<>();
        titles.add(mStringCommunity);
        titles.add(mStringCourse);
        titles.add(mStringExplore);
        titles.add(mStringMyPage);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);

        mBottomBar.setOnBottomViewClickListener((view, position) -> {
            mViewPager.setCurrentItem(position);
            hiddenMenu();
            setTitle(adapter.getPageTitle(position));
            if (position == 0) {
                showMenu();
            }
            if (position == 3) {
                if (!APP.isLogin()) {
                    EventBus.getDefault().post(new LoginEvent());
                }
            }
        });

    }

    private void initToolbar() {
        if (mToolbar != null) {
            setTitle(mStringCommunity);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbar != null) {
            mToolbarTitle.setText(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_news:
                if (APP.isLogin()) {
                    PostNewsActivity.startActivity(this);
                } else {
                    Util.toast(getApplicationContext(), "尚未登录");
                    EventBus.getDefault().post(new LoginEvent());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void hiddenMenu() {
        if (null != mMenu) {
            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(false);
            }
        }
    }

    private void showMenu() {
        if (null != mMenu) {
            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(true);
            }
        }

    }

    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}