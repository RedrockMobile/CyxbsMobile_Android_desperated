package com.mredrock.cyxbsmobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.util.Util;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang3.StringUtils;

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
            Util.toast(this, "请输入有效的学号");
            return;
        }
        if (StringUtils.isBlank(idNum) || idNum.length() < 6) {
            Util.toast(this, "请输入有效的密码");
            return;
        }
        RequestManager.getInstance()
                      .verify(new SimpleSubscriber<>(this, true, false, new SubscriberListener<User>() {

                          @Override
                          public void onNext(User user) {
                              super.onNext(user);
                              APP.setUser(LoginActivity.this, user);
                              startActivity(new Intent(LoginActivity.this, MainActivity.class));
                              LoginActivity.this.finish();
                          }

                      }), stuNum, idNum);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
