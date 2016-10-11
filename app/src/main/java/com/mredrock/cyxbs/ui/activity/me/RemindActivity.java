package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.fragment.me.RemindFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RemindActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.fragment_remind)
    FrameLayout mFragmentRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        ButterKnife.bind(this);
        initToolbar();
        initFragment();
    }

    private void initFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_remind, new RemindFragment())
                .commit();
    }

    private void initToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbarTitle.setText("课前提醒");
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(
                    v -> RemindActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
