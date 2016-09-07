package com.mredrock.cyxbs.ui.activity;

import android.accounts.Account;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
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

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_stu_num_edit)
    AppCompatEditText stuNumEdit;
    @Bind(R.id.login_id_num_edit)
    AppCompatEditText idNumEdit;
    @Bind(R.id.login_submit_button)
    Button submitButton;
    @Bind(R.id.iv_login_account)
    ImageView mIvLoginAccount;
    @Bind(R.id.iv_login_password)
    ImageView mIvLoginPassword;

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
        initView();
        initUser();
        iconColorChangerFn();
        autoSendFn();
        submitButton.setEnabled(false);
        submitButton.setBackgroundColor(getResources().getColor(R.color.gray_edit));
    }

    /***
     *软键盘回车登陆
     */
    private void autoSendFn() {
        idNumEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                attemptLogin();
            }
            return false;
        });
    }

    /**
     * 登录老锅，代码很乱
     */
    private void iconColorChangerFn() {
        stuNumEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mIvLoginAccount.setColorFilter(getResources().getColor(R.color.md_material_blue_600));
            } else {
                String stuNum = stuNumEdit.getText().toString();
                if (StringUtils.isBlank(stuNum) || stuNum.length() < 10) {
                    stuNumEdit.setError("请输入有效的学号");
                } else {
                    submitButton.setEnabled(true);
                    submitButton.setBackgroundColor(getResources().getColor(R.color.md_material_blue_600));
                }
                mIvLoginAccount.setColorFilter(getResources().getColor(R.color.gray_edit));
            }
        });
        idNumEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mIvLoginPassword.setColorFilter(getResources().getColor(R.color.md_material_blue_600));
            } else {
                String idNum = idNumEdit.getText().toString();
                mIvLoginPassword.setColorFilter(getResources().getColor(R.color.gray_edit));
                if (StringUtils.isBlank(idNum) || idNum.length() < 6) {
                    idNumEdit.setError("请输入身份证后六位");
                } else {
                    submitButton.setEnabled(true);
                    submitButton.setBackgroundColor(getResources().getColor(R.color.md_material_blue_600));
                }
            }
        });
    }

    private void initUser() {
        APP.setUser(this, null);
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setTitle("");
        toolbarTitle.setText("登录");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> LoginActivity.this.finish());
    }

    public void attemptLogin() {
        String stuNum = stuNumEdit.getText().toString();
        String idNum = idNumEdit.getText().toString();
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
