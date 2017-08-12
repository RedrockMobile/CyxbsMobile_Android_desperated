package com.mredrock.cyxbs.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.me.EditNickNameActivity;
import com.mredrock.cyxbs.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_stu_num_edit)
    AppCompatEditText stuNumEdit;
    @Bind(R.id.login_id_num_edit)
    AppCompatEditText idNumEdit;
    @Bind(R.id.login_submit_button)
    Button submitButton;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    public static final String TAG = "LoginActivity";

    @OnClick(R.id.login_submit_button)
    void clickToLogin() {
        attemptLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initUser();
        autoSendFn();
        initToolbar();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("登 录");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(v -> LoginActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    /**
     * 软键盘登录
     */
    private void autoSendFn() {
        idNumEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                attemptLogin();
            }
            return false;
        });
    }

    private void initUser() {
        APP.setUser(this, null);
    }

    public void attemptLogin() {
        String stuNum = stuNumEdit.getText().toString();
        String idNum = idNumEdit.getText().toString();
        if ("".equals(idNum)) {
            Utils.toast(this, "请输入密码");
        }
        RequestManager.getInstance()
                .login(new SimpleSubscriber<>(this, true, false, new SubscriberListener<User>() {
                    @Override
                    public void onNext(User user) {
                        super.onNext(user);
                        if (user != null) {
                            APP.setUser(LoginActivity.this, user);
                            MobclickAgent.onProfileSignIn(stuNum);
                        } else {
                            Utils.toast(LoginActivity.this, "登录失败, 返回了信息为空");
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        EventBus.getDefault().post(new LoginStateChangeEvent(true));
                        finish();
                        if (!APP.hasNickName()) {
                            EditNickNameActivity.start(APP.getContext());
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
        /*if (EventBus.getDefault().hasSubscriberForEvent(ExitEvent.class)) {
            EventBus.getDefault().postSticky(new ExitEvent());
        }*/

        // this.finish();
    }
}
