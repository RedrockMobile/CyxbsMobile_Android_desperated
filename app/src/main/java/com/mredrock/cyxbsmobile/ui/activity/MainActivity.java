package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.BBDD;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.model.community.ReMarks;
import com.mredrock.cyxbsmobile.model.community.Student;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.fragment.community.CommunityContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.ExploreFragment;
import com.mredrock.cyxbsmobile.ui.fragment.MyPageFragment;
import com.mredrock.cyxbsmobile.util.DensityUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import rx.functions.Action1;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.content)
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

    @Bind(R.id.add_news_img)
    ImageView mImageView;
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
        mBottomBar.selectTabAtPosition(1, false);
        test();
    }

    private void test() {

    }


    private void initView() {
        initToolbar();
        mImageView.setOnClickListener(this);
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

        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mAppBar.setElevation(position == 1 ? 0 : DensityUtil.dp2px(MainActivity.this, 4));
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hideAllFragments(transaction);
                mImageView.setVisibility(View.GONE);
                switch (position) {
                    case 0:
                        showFragment(transaction, communityContainerFragment, R.id.fragment_container);
                        setTitle(mStringCommunity);
                        mImageView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        showFragment(transaction, courseContainerFragment, R.id.fragment_container);
                        setTitle(mStringCourse);
                        break;
                    case 2:
                        showFragment(transaction, exploreFragment, R.id.fragment_container);
                        setTitle(mStringExplore);
                        break;
                    case 3:
                        showFragment(transaction, myPageFragment, R.id.fragment_container);
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
                //actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
                //actionBar.setDisplayHomeAsUpEnabled(true);
                //actionBar.setHomeButtonEnabled(true);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_news_img:
                startActivity(new Intent(MainActivity.this, AddNewsActivity.class));
                break;
        }
    }
}
