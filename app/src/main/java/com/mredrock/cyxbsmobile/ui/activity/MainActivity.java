package com.mredrock.cyxbsmobile.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.fragment.community.CommunityContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.ExploreFragment;
import com.mredrock.cyxbsmobile.ui.fragment.MyPageFragment;
import com.mredrock.cyxbsmobile.util.DensityUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

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
    @BindString(R.string.community)
    String mStringCommunity;
    @BindString(R.string.course)
    String mStringCourse;
    @BindString(R.string.explore)
    String mStringExplore;
    @BindString(R.string.my_page)
    String mStringMyPage;
    @Bind(R.id.main_app_bar)
    AppBarLayout mAppBar;
    private BottomBar mBottomBar;
    private CourseContainerFragment courseContainerFragment;
    private CommunityContainerFragment communityContainerFragment;
    private ExploreFragment exploreFragment;
    private MyPageFragment myPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initBottomBar(savedInstanceState);
        mBottomBar.selectTabAtPosition(1, false);
    }

    private void initView() {
        initToolbar();
        courseContainerFragment = new CourseContainerFragment();
        communityContainerFragment = new CommunityContainerFragment();
        exploreFragment = new ExploreFragment();
        myPageFragment = new MyPageFragment();
    }

    private void initBottomBar(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(mCoordinatorLayout, savedInstanceState);
        mBottomBar.setItems(
                new BottomBarTab(R.drawable.ic_nearby, "社区"),
                new BottomBarTab(R.drawable.ic_favorites, "课表"),
                new BottomBarTab(R.drawable.ic_restaurants, "发现"),
                new BottomBarTab(R.drawable.ic_friends, "我的")
        );

        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mAppBar.setElevation(position == 1 ? 0 : DensityUtil.dp2px(MainActivity.this, 4));
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hideAllFragments(transaction);
                switch (position) {
                    case 0:
                        showFragment(transaction, communityContainerFragment, R.id.main_fragment_container);
                        setTitle(mStringCommunity);
                        break;
                    case 1:
                        showFragment(transaction, courseContainerFragment, R.id.main_fragment_container);
                        setTitle(mStringCourse);
                        break;
                    case 2:
                        showFragment(transaction, exploreFragment, R.id.main_fragment_container);
                        setTitle(mStringExplore);
                        break;
                    case 3:
                        showFragment(transaction, myPageFragment, R.id.main_fragment_container);
                        setTitle(mStringMyPage);
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabReSelected(int position) {
            }
        });

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.colorPrimary));

    }

    private void hideAllFragments(FragmentTransaction transaction) {
        hideFragment(transaction, courseContainerFragment);
        hideFragment(transaction, communityContainerFragment);
        hideFragment(transaction, exploreFragment);
        hideFragment(transaction, myPageFragment);
    }

    private void hideFragment(FragmentTransaction transaction, Fragment frag) {
        if (frag != null) transaction.hide(frag);
    }

    private void showFragment(FragmentTransaction transaction, Fragment frag, int resId) {
        hideAllFragments(transaction);
        if (!frag.isAdded()) {
            transaction.add(resId, frag);
        }
        transaction.show(frag);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
}
