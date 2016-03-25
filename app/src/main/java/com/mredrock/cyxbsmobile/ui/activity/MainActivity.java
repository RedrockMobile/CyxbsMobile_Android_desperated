package com.mredrock.cyxbsmobile.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.fragment.CommunityContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.ExploreFragment;
import com.mredrock.cyxbsmobile.ui.fragment.MyPageFragment;
import com.mredrock.cyxbsmobile.util.DensityUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.content)
    CoordinatorLayout mCoordinatorLayout;
    @BindString(R.string.app_name)
    String mAppName;
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
        mBottomBar = BottomBar.attach(mCoordinatorLayout, savedInstanceState);
        initView();
        mBottomBar.selectTabAtPosition(0, false);
    }

    private void initView() {
        initToolbar();
        courseContainerFragment = new CourseContainerFragment();
        communityContainerFragment = new CommunityContainerFragment();
        exploreFragment = new ExploreFragment();
        myPageFragment = new MyPageFragment();
        initBottomBar();
    }

    private void initBottomBar() {

        mBottomBar.setItems(
                new BottomBarTab(R.drawable.ic_nearby, "社区"),
                new BottomBarTab(R.drawable.ic_favorites, "课表"),
                new BottomBarTab(R.drawable.ic_restaurants, "发现"),
                new BottomBarTab(R.drawable.ic_friends, "我的")
        );

        mBottomBar.setOnItemSelectedListener(position -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBar.setElevation(position == 1 ? 0 : DensityUtil.dp2px(this, 4));
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragments(transaction);
            switch (position) {
                case 0:
                    showFragment(transaction, communityContainerFragment, R.id.fragment_container);
                    break;
                case 1:
                    showFragment(transaction, courseContainerFragment, R.id.fragment_container);
                    break;
                case 2:
                    showFragment(transaction, exploreFragment, R.id.fragment_container);
                    break;
                case 3:
                    showFragment(transaction, myPageFragment, R.id.fragment_container);
                    break;
            }
            transaction.commit();
        });

        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.orange));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.yellow));

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
        if (!frag.isAdded()) {
            transaction.add(resId, frag);
        }
        transaction.show(frag);
    }

    private void initToolbar() {
        mToolbar.setTitle(mAppName);
        setSupportActionBar(mToolbar);
    }
}
