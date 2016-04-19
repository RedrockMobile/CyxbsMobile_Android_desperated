package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;

public class SettingActivity extends BaseActivity
        implements View.OnClickListener {

    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.setting_remind_layout) RelativeLayout settingRemindLayout;
    @Bind(R.id.setting_feedback_layout) RelativeLayout settingFeedbackLayout;
    @Bind(R.id.setting_about_layout) RelativeLayout settingAboutLayout;
    @Bind(R.id.setting_exit_layout) RelativeLayout settingExitLayout;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
        settingRemindLayout.setOnClickListener(this);
        settingFeedbackLayout.setOnClickListener(this);
        settingAboutLayout.setOnClickListener(this);
        settingExitLayout.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_remind_layout:
                startActivity(new Intent(this, NewsRemindActivity.class));
                break;
            case R.id.setting_feedback_layout:
                break;
            case R.id.setting_about_layout:
                break;
            case R.id.setting_exit_layout:
                break;
        }
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("没课约");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> SettingActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
