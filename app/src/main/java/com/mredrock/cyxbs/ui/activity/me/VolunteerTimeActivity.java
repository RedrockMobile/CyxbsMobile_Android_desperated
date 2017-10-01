package com.mredrock.cyxbs.ui.activity.me;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.VolunteerTimeSP;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class VolunteerTimeActivity extends BaseActivity {
    private static final String TAG = "VolunteerTimeActivity";

    private String uid;
    private VolunteerTimeSP volunteerSP;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.unbind_info)
    TextView unbindInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_time);
        ButterKnife.bind(VolunteerTimeActivity.this);
        initToolbar();
        initData();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbarTitle.setText("服务记录");
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(
                    v ->VolunteerTimeActivity.this.finish());
        }
    }

    public void initData() {
        volunteerSP = new VolunteerTimeSP(this);
        uid = volunteerSP.getVolunteerUid();
        if (uid.equals("404") || volunteerSP.getVolunteerAccount().equals("404") ||
                    volunteerSP.getVolunteerPassword().equals("404")){
            Toast.makeText(this, "请先登录绑定账号哦", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, VolunteerTimeLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            loadVolunteerTime(uid);
        }
    }

    private void loadVolunteerTime(String uid) {
        RequestManager.INSTANCE.getVolunteerTime(new Subscriber<VolunteerTime.DataBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(VolunteerTime.DataBean dataBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VolunteerTimeActivity.this, dataBean.getRecord().get(0).getContent() + "", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, uid);
    }

    @OnClick(R.id.unbind_info)
    public void unbindClick(View v) {
        if (v.getId() == R.id.unbind_info) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> new MaterialDialog.Builder(this)
                    .title("解除账号绑定？")
                    .content("亲，真的要取消已绑定的账号咩？")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            volunteerSP.unBindVolunteerInfo();
                            Intent intent = new Intent(VolunteerTimeActivity.this, VolunteerTimeLoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    }));
        }
    }
}
