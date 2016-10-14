package com.mredrock.cyxbs.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.network.func.AppWidgetCacheAndUpdateFunc;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.setting_remind_layout)
    RelativeLayout settingRemindLayout;
    @Bind(R.id.setting_feedback_layout)
    RelativeLayout settingFeedbackLayout;
    @Bind(R.id.setting_about_layout)
    RelativeLayout settingAboutLayout;
    @Bind(R.id.setting_exit_layout)
    RelativeLayout settingExitLayout;
    @Bind(R.id.setting_share_layout)
    RelativeLayout mSettingShareLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
        if (!APP.isLogin()) {
            settingExitLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.setting_remind_layout)
    void clickToRemind() {
        startActivity(new Intent(this, NewsRemindActivity.class));
    }

    @OnClick(R.id.setting_feedback_layout)
    void clickToFeedback() {

    }

    @OnClick(R.id.setting_about_layout)
    void clickToAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @OnClick(R.id.setting_exit_layout)
    void clickToExit() {
        Handler handler = new Handler(getMainLooper());
        handler.post(() -> new MaterialDialog.Builder(this)
                .title("退出登录?")
                .content("是否退出当前账号?")
                .positiveText("退出")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        finish();
                        APP.setUser(SettingActivity.this, null);
                        AppWidgetCacheAndUpdateFunc.deleteCache();
                        EventBus.getDefault().post(new LoginStateChangeEvent(false));
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                }).show());
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("设置");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> SettingActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    @OnClick(R.id.setting_share_layout)
    public void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_share).show();
    }
}