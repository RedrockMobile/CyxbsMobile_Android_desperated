package com.mredrock.cyxbs.ui.activity.explore;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Stormouble on 16/5/3.
 */
public abstract class BaseExploreActivity extends BaseActivity {

    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.main_content)
    View mMainContent;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mMainContent = findViewById(R.id.main_content);
        if (mMainContent != null) {
            mMainContent.setAlpha(0.f);
            mMainContent.animate()
                    .alpha(1.f)
                    .setDuration(MAIN_CONTENT_FADEIN_DURATION)
                    .start();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initToolbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    protected void tryHandleMultiFragmentRestored(Bundle savedInstanceState) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (Utils.checkNotNullAndNotEmpty(fragments)) {
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            boolean isShow = true;
            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (Utils.checkNotNull(fragment)) {
                    if (isShow) {
                        transaction.show(fragment);
                        isShow = false;
                    } else {
                        transaction.hide(fragment);
                    }
                }
            }
            transaction.commit();
        }
    }

    protected void tryHandleMultiFragmentBack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
