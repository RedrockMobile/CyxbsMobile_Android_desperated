package com.mredrock.cyxbs.ui.activity.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.util.UpdateUtil;
import com.mredrock.cyxbs.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.about_1)
    TextView about1;
    @Bind(R.id.about_version)
    TextView aboutVersion;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar  toolbar;


    @OnClick(R.id.about_website)
    void clickToWebsite() {
       // WebViewUtils.showPortalWebView(this, Const.APP_HOME);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.APP_HOME));
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
        UpdateUtil.checkUpdate(this, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initializeToolbar();
        aboutVersion.setText(new StringBuilder("Version ").append(Utils.getAppVersionName(this)));
    }

    protected void initializeToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("关于");
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
