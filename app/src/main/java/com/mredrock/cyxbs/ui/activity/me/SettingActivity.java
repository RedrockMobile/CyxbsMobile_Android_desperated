package com.mredrock.cyxbs.ui.activity.me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.AffairShowModeEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.network.func.AppWidgetCacheAndUpdateFunc;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.SaveImageUtils;
import com.mredrock.cyxbs.util.permission.AfterPermissionGranted;
import com.mredrock.cyxbs.util.permission.EasyPermissions;
import com.suke.widget.SwitchButton;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SettingActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    public static final String SHOW_MODE = "showMode";


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.setting_remind)
    RelativeLayout settingRemindLayout;
    @BindView(R.id.setting_feedback)
    RelativeLayout settingFeedbackLayout;
    @BindView(R.id.setting_about)
    RelativeLayout settingAboutLayout;
    @BindView(R.id.setting_exit)
    Button settingExit;
    @BindView(R.id.setting_share)
    RelativeLayout mSettingShareLayout;
    @BindView(R.id.setting_switch_show)
    SwitchButton switchCompat;
    ImageView QRImageView;

    private SharedPreferences preferences;
    private boolean currentMode = true;
    private boolean initMode = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
        if (!BaseAPP.isLogin()) {
            settingExit.setVisibility(View.GONE);
        }

        initSwitchCompat();

    }

    private void initSwitchCompat() {
        preferences = getSharedPreferences(SHOW_MODE, MODE_PRIVATE);
        initMode = currentMode = preferences.getBoolean(SHOW_MODE, true);
        switchCompat.setChecked(currentMode);
        SharedPreferences.Editor editor = preferences.edit();
        switchCompat.setOnCheckedChangeListener((button, b) -> {
            Observable.create(subscriber -> {
                editor.putBoolean(SHOW_MODE, b);
                editor.apply();
                currentMode = b;
                subscriber.onNext("");
                subscriber.onComplete();
            }).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io()).subscribe();
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /*
        @OnClick(R.id.setting_remind)
        void clickToRemind() {
            startActivity(new Intent(this, NewsRemindActivity.class));
        }
    */
    @OnClick(R.id.setting_feedback)
    void clickToFeedback() {
        if (!joinQQGroup("DXvamN9Ox1Kthaab1N_0w7s5N3aUYVIf")) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("QQ Group", "570919844");
            clipboard.setPrimaryClip(data);
            Toast.makeText(this, "抱歉，由于您未安装手机QQ或版本不支持，无法跳转至掌邮bug反馈群。" +
                            "已将群号复制至您的手机剪贴板，请您手动添加",
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.setting_about)
    void clickToAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @OnClick(R.id.setting_exit)
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
                        BaseAPP.setUser(SettingActivity.this, null);
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

    /****************
     *
     * 发起添加群流程。群号：掌上重邮反馈群(570919844) 的 key 为： DXvamN9Ox1Kthaab1N_0w7s5N3aUYVIf
     * 调用 joinQQGroup(DXvamN9Ox1Kthaab1N_0w7s5N3aUYVIf) 即可发起手Q客户端申请加群 掌上重邮反馈群(570919844)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("设 置");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(v -> SettingActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (initMode != currentMode) {
            AffairShowModeEvent event = new AffairShowModeEvent();
            event.showMode = currentMode;
            EventBus.getDefault().post(event);
        }
        super.onDestroy();

    }

    @OnClick(R.id.setting_share)
    public void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_share, null, false);
        builder.setView(view).show();
        QRImageView = (ImageView) view.findViewById(R.id.imageView);
        QRImageView.setOnLongClickListener(v -> {
            Drawable drawable = QRImageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                if (!EasyPermissions.hasPermissions(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    EasyPermissions.requestPermissions(this, "要想保存二维码的话给个权限吧",0, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }else {
                    storeQRCode();
                }
            }
            return true;
        });
    }


    @AfterPermissionGranted(0)
    public void storeQRCode(){
        Bitmap bitmap = ((BitmapDrawable) QRImageView.getDrawable()).getBitmap();
        SaveImageUtils.imageSave(bitmap, String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this,"要想保存二维码必须给权限哟~",Toast.LENGTH_LONG).show();
    }

}