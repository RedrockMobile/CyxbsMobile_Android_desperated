package com.mredrock.cyxbs.ui.activity.me;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.VolunteerTimeSP;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class VolunteerTimeLoginActivity extends BaseActivity {
    private String account;
    private String password;

    private VolunteerTimeSP volunteerSP;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.account)
    EditText accountView;
    @Bind(R.id.password)
    EditText passwordView;
    @Bind(R.id.login_volunteer_button)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_time_login);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    @OnClick(R.id.login_volunteer_button)
    void loginButtonClick(View v) {
        initData();
        if (v.getId() == R.id.login_volunteer_button) login(account, password);;
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
                    v ->VolunteerTimeLoginActivity.this.finish());
        }
    }

    public void initData() {
        account =  accountView.getText().toString();
        password = passwordView.getText().toString();
        volunteerSP = new VolunteerTimeSP(this);
        if (!(volunteerSP.getVolunteerUid().equals("404")
            || volunteerSP.getVolunteerUid().equals("null")
                || volunteerSP.getVolunteerUid().equals(""))) {
            Intent intent = new Intent(this, VolunteerTimeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(String account, String password) {
        RequestManager.INSTANCE.getVolunteer(new Subscriber<VolunteerTime>() {


            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Toast.makeText(VolunteerTimeLoginActivity.this,
                        "亲，网络有问题哦", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onNext(VolunteerTime dataBean) {
                switch (dataBean.getStatus()) {
                    case 200:
                        volunteerSP.bindVolunteerInfo(account, password, dataBean.getData().getUid());
                        Intent intent = new Intent(VolunteerTimeLoginActivity.this, VolunteerTimeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 001:
                        showToast("亲，输入的账号或密码有误哦");
                        break;
                    case 002:
                        showToast("亲，输入的账号不存在哦");
                        break;
                    case 003:
                        showToast("亲，请填入正确的密码");
                        break;
                    case 004:
                        showToast("亲，请填入正确的账号");
                        break;
                }
            }
        }, account, password);
    }

    public void showToast(String content) {
        VolunteerTimeLoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VolunteerTimeLoginActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
