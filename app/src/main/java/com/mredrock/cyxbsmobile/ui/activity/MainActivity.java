package com.mredrock.cyxbsmobile.ui.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.bottombar.BottomBar;
import com.mredrock.cyxbsmobile.ui.activity.social.AddNewsActivity;
import com.mredrock.cyxbsmobile.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbsmobile.ui.fragment.ExploreFragment;
import com.mredrock.cyxbsmobile.ui.fragment.UserFragment;
import com.mredrock.cyxbsmobile.ui.fragment.social.SocialContainerFragment;

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


    @Bind(R.id.bottom_bar)
    BottomBar mBottomBar;


    private Menu mMenu;


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
        // mImageView.setOnClickListener(this);
        mBottomBar.setOnBottomViewClickListener((view, position) -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = null;
            //  mImageView.setVisibility(View.GONE);
            hiddenMenu();

            switch (position) {
                case 0:
                    fragment = new SocialContainerFragment();
                    setTitle(mStringCommunity);
                    // mImageView.setVisibility(View.VISIBLE);
                    showMenu();

                    break;
                case 1:
                    fragment = new CourseContainerFragment();
                    break;
                case 2:
                    fragment = new ExploreFragment();
                    setTitle(mStringExplore);
                    break;
                case 3:
                    fragment = new UserFragment();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_news:
                AddNewsActivity.startActivity(this);
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
}