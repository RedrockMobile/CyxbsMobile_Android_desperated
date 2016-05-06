package com.mredrock.cyxbsmobile.ui.activity.explore;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.fragment.AroundDishesFragment;
import com.mredrock.cyxbsmobile.util.ActivityUtils;
import com.mredrock.cyxbsmobile.util.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stormouble on 16/4/16.
 */
public class AroundDishesActivity extends BaseActivity {
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_dishes);
        ButterKnife.bind(this);

        initToolbar();

        onHandledSavedInstanceState(savedInstanceState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        View mainContent = findViewById(R.id.around_dishes_main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate()
                    .alpha(1)
                    .setDuration(MAIN_CONTENT_FADEIN_DURATION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            handleOnBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    private void onHandledSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
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
        } else {
            AroundDishesFragment fragment =
                    (AroundDishesFragment) getSupportFragmentManager().findFragmentById(R.id.around_dishes_contentFrame);
            if (fragment == null) {
                fragment = AroundDishesFragment.newInstance();
                ActivityUtils.addFragmentToActivity(
                        getSupportFragmentManager(), fragment, R.id.around_dishes_contentFrame);
            }
        }
    }

    private void handleOnBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }
}
