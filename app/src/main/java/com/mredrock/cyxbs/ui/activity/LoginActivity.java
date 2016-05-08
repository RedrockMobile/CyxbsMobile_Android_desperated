package com.mredrock.cyxbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.ExitEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.Utils;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar  toolbar;
    @Bind(R.id.login_stu_num_edit)
    EditText stuNumEdit;
    @Bind(R.id.login_id_num_edit)
    EditText idNumEdit;
    @Bind(R.id.login_submit_button)
    Button   submitButton;

    @OnClick(R.id.login_submit_button)
    void clickToLogin() {
        attemptLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initUser();
    }

    private void initUser() {
        APP.setUser(this, null);
    }

    private void initView() {
        toolbar.setTitle("");
        toolbarTitle.setText("登录");
        setSupportActionBar(toolbar);
    }

    public void attemptLogin() {
        String stuNum = stuNumEdit.getText().toString();
        String idNum = idNumEdit.getText().toString();
        if (StringUtils.isBlank(stuNum) || stuNum.length() < 10) {
            Utils.toast(this, "请输入有效的学号");
            return;
        }
        if (StringUtils.isBlank(idNum) || idNum.length() < 6) {
            Utils.toast(this, "请输入有效的密码");
            return;
        }
        RequestManager.getInstance()
                      .verify(new SimpleSubscriber<>(this, true, false, new SubscriberListener<User>() {

                          @Override
                          public void onNext(User user) {
                              super.onNext(user);
                              if (user != null) {
                                  APP.setUser(LoginActivity.this, user);
                                  EventBus.getDefault().removeStickyEvent(ExitEvent.class);
                                  startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                  LoginActivity.this.finish();
                              } else {
                                  Utils.toast(LoginActivity.this, "登录失败, 返回了信息为空");
                              }
                          }

                      }), stuNum, idNum);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (EventBus.getDefault().hasSubscriberForEvent(ExitEvent.class)) {
            EventBus.getDefault().postSticky(new ExitEvent());
        }

        this.finish();
    }
}
