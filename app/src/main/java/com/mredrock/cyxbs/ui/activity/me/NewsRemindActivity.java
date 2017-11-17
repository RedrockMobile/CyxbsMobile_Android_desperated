package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsRemindActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_remind);
        ButterKnife.bind(this);
        initToolbar();
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.back);
            toolbarTitle.setText("新消息提醒");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> NewsRemindActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
