package com.mredrock.cyxbs.ui.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.bottombar.BottomBar;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbs.ui.activity.me.NewsRemindActivity;
import com.mredrock.cyxbs.ui.activity.me.NoCourseActivity;
import com.mredrock.cyxbs.ui.activity.social.PostNewsActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbs.ui.fragment.UnLoginFragment;
import com.mredrock.cyxbs.ui.fragment.UserFragment;
import com.mredrock.cyxbs.ui.fragment.explore.ExploreFragment;
import com.mredrock.cyxbs.ui.fragment.social.SocialContainerFragment;
import com.mredrock.cyxbs.util.UpdateUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    @Bind(R.id.main_toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.bottom_bar)
    BottomBar mBottomBar;
    @Bind(R.id.main_view_pager)
    ViewPager mViewPager;

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
    BaseFragment unLoginFragment;

    private Menu mMenu;
    private ArrayList<Fragment> mFragments;
    private TabPagerAdapter mAdapter;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        UpdateUtil.checkUpdate(this, false);
        InterFilter();
    }

    /**
     * 适配魅族 3D TOUCH
     */
    private void InterFilter() {
        Uri data = getIntent().getData();
        if (data != null && TextUtils.equals("forcetouch", data.getScheme())) {
            Log.d(TAG, "InterFilter: ");
            if (TextUtils.equals("/schedule", data.getPath())) {
                Log.d(TAG, "InterFilter: 进入主页");
            }
            if (TextUtils.equals("/new", data.getPath())) {
                Intent intent = new Intent(this, NewsRemindActivity.class);
                startActivity(intent);
            }
            if (TextUtils.equals("/foods", data.getPath())) {
                Intent intent = new Intent(this, SurroundingFoodActivity.class);
                startActivity(intent);
            }
            if (TextUtils.equals("/date", data.getPath())) {
                Intent intent = new Intent(this, NoCourseActivity.class);
                startActivity(intent);
            }
        }
    }

    private void initView() {
        initToolbar();
        socialContainerFragment = new SocialContainerFragment();
        courseContainerFragment = new CourseContainerFragment();
        exploreFragment = new ExploreFragment();
        userFragment = new UserFragment();
        unLoginFragment = new UnLoginFragment();

        mFragments = new ArrayList<>();
        //判断是否登陆
        if (!APP.isLogin()) {
            mFragments.add(unLoginFragment);
        } else {
            mFragments.add(courseContainerFragment);
        }
        mFragments.add(socialContainerFragment);
        mFragments.add(exploreFragment);
        mFragments.add(userFragment);

        ArrayList<String> titles = new ArrayList<>();
        titles.add(mStringCourse);
        titles.add(mStringCommunity);
        titles.add(mStringExplore);
        titles.add(mStringMyPage);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, titles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mBottomBar.post(() -> hiddenMenu());
        mBottomBar.setOnBottomViewClickListener((view, position) -> {
            mViewPager.setCurrentItem(position, false);
            hiddenMenu();
            setTitle(mAdapter.getPageTitle(position));
            switch (position) {
                case 1:
                    showMenu();
                    Log.d(TAG, "initView: 1");
                    break;
                case 0:
                    Log.d(TAG, "initView: 0");
                    setTitle(((CourseContainerFragment) courseContainerFragment).getTitle());
                    break;
                case 3:
                    if (!APP.isLogin()) {
                        Log.d(TAG, "initView: 3");
//                        EventBus.getDefault().post(new LoginEvent());
                    }
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        super.onLoginStateChangeEvent(event);
        boolean isLogin = event.getNewState();
        Log.d(TAG, "onLoginStateChangeEvent: " + APP.isFresh());
        if (!isLogin) {
            mFragments.remove(0);
            mFragments.add(0, new UnLoginFragment());
            mAdapter.notifyDataSetChanged();
        } else {
            mFragments.remove(0);
            mFragments.add(0, new CourseContainerFragment());
            mBottomBar.setCurrentView(0);
            mAdapter.notifyDataSetChanged();
        }
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
                    if (APP.getUser(this).id == null || APP.getUser(this).id.equals("0")) {
                        RequestManager.getInstance().checkWithUserId("还没有完善信息，不能发动态哟！");
                        mViewPager.setCurrentItem(3);
                        mBottomBar.setCurrentView(3);
                        return super.onOptionsItemSelected(item);
                    } else
                        PostNewsActivity.startActivity(this);
                } else {
                    // Utils.toast(getApplicationContext(), "尚未登录");
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

    public int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

}