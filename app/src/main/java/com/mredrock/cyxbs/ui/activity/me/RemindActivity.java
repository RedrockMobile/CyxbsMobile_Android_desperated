package com.mredrock.cyxbs.ui.activity.me;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.fragment.me.RemindFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemindActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.fragment_remind)
    FrameLayout mFragmentRemind;
    public static final String TAG = RemindActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        ButterKnife.bind(this);
        initToolbar();
        initFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) askForPermission();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermission() {
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(this, "抱歉，由于您的手机系统原因此功能无法使用", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode != -1){
            Toast.makeText(this, "请加入电量忽略，否则无法使用该功能", Toast.LENGTH_SHORT).show();
            finish();
        }
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
            mToolbar.setNavigationIcon(R.drawable.ic_back);
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
