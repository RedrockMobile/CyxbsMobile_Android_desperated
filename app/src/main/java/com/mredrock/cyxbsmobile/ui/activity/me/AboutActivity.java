package com.mredrock.cyxbsmobile.ui.activity.me;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.config.Constants;
import com.mredrock.cyxbsmobile.util.Util;
import com.mredrock.cyxbsmobile.util.WebViewUtils;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.about_1) TextView about1;
    @Bind(R.id.about_version) TextView aboutVersion;
    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;


    @OnClick(R.id.about_website) void clickToWebsite() {
        //WebActivity.startWebActivity(Constants.REDROCK_PORTAL, getActivity());
        WebViewUtils.showPortalWebView(this, Constants.APP_HOME);
    }


    @OnClick(R.id.about_legal) void clickToSee() {
        new MaterialDialog.Builder(this).title("使用条款")
                                        .content("版权归红岩网校工作站所有,感谢您的使用")
                                        .positiveText("确定")
                                        .build()
                                        .show();
    }


    @OnClick(R.id.about_update) void clickToUpdate() {

    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initializeToolbar();
        aboutVersion.setText(new StringBuilder("Version ").append(
                Util.getAppVersionName(this)));
    }


    protected void initializeToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("与我相关");
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


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
