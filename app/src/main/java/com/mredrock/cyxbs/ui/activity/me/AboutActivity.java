package com.mredrock.cyxbs.ui.activity.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.UpdateUtil;
import com.mredrock.cyxbs.util.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.about_1)
    TextView about1;
    @BindView(R.id.about_version)
    TextView aboutVersion;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    RxPermissions rxPermissions;

    @OnClick(R.id.about_website)
    void clickToWebsite() {
        // WebViewUtils.showPortalWebView(this, Const.APP_HOME);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.APP_WEBSITE));
        startActivity(intent);
    }


    @OnClick(R.id.about_legal)
    void clickToSee() {
        new MaterialDialog.Builder(this).title("使用条款")
                .content("版权归红岩网校工作站所有,感谢您的使用")
                .positiveText("确定")
                .build()
                .show();
    }

    @OnClick(R.id.about_update)
    void clickToUpdate() {
        UpdateUtil.checkUpdate(this, true,rxPermissions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        rxPermissions = new RxPermissions(this);
        ButterKnife.bind(this);
        initializeToolbar();
        aboutVersion.setText(new StringBuilder("Version ").append(Utils.getAppVersionName(this)));
    }

    protected void initializeToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbarTitle.setText("关 于");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> AboutActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
