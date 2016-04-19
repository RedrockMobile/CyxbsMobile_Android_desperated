package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;

public class MyTrendActivity extends BaseActivity {

    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trend);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {
        if(toolbar != null){
            toolbar.setTitle("");
            toolbarTitle.setText("我的动态");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> MyTrendActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
