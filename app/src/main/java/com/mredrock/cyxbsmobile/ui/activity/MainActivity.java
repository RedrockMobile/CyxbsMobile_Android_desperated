package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.bottombar.BottomBar;
import com.mredrock.cyxbsmobile.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.ExploreFragment;
import com.mredrock.cyxbsmobile.ui.fragment.MyPageFragment;
import com.mredrock.cyxbsmobile.ui.fragment.community.CommunityContainerFragment;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {

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

    @Bind(R.id.add_news_img)
    ImageView mImageView;
    @Bind(R.id.bottom_bar)
    BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    private void initView() {
        initToolbar();
        mImageView.setOnClickListener(this);

        mBottomBar.setOnBottomViewClickListener((view, position) -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = null;
            mImageView.setVisibility(View.GONE);
            switch (position) {
                case 0:
                    fragment = new CommunityContainerFragment();
                    setTitle(mStringCommunity);
                    mImageView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    fragment = new CourseContainerFragment();
                    break;
                case 2:
                    fragment = new ExploreFragment();
                    setTitle(mStringExplore);
                    break;
                case 3:
                    fragment = new MyPageFragment();
                    setTitle(mStringMyPage);
                    break;
            }
            transaction.replace(R.id.main_fragment_container, fragment);
            transaction.commit();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_news_img:
                startActivity(new Intent(MainActivity.this, AddNewsActivity.class));
                break;
        }

    }

    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }
}
