package com.mredrock.cyxbs.ui.activity.me;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.VolunteerTimeSP;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by glossimarsun on 2017/10/2.
 */


public class VolunteerTimeLoginActivity extends BaseActivity {
    private String account;
    private String password;

    private VolunteerTimeSP volunteerSP;
    private ProgressDialog dialog;

    @BindView(R.id.volunteer_toolbar)
    Toolbar toolbar;
    @BindView(R.id.volunteer_login_back)
    ImageView backImage;
    @BindView(R.id.volunteer_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.volunteer_account)
    EditText accountView;
    @BindView(R.id.volunteer_password)
    EditText passwordView;
    @BindView(R.id.volunteer_login)
    ImageView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_time_login);

        ButterKnife.bind(this);
        initToolbar();
        useSoftKeyboard();
        initData();
    }

    @OnClick(R.id.volunteer_login)
    void loginButtonClick(View v) {
        showProgressDialog();
        initData();
        if (v.getId() == R.id.volunteer_login) login(account, password);
    }

    @OnClick(R.id.volunteer_login_back)
    public void finishActivity(View v) {
        finish();
    }
    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
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

    private void useSoftKeyboard() {
        passwordView.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_GO) {
                showProgressDialog();
                initData();
                login(account,password);
                handled = true;

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(VolunteerTimeLoginActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
            return handled;
        });
    }

    private void login(String account, String password) {
        RequestManager.INSTANCE.getVolunteer(new Observer<VolunteerTime>() {

            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Throwable e) {
               showUnsuccessDialog("网络有问题哦");
                Log.d("RequestManager", "onError: ------------------------------------------------------------------------------");
                dialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(VolunteerTime dataBean) {
                switch (dataBean.getStatus()) {
                    case 200:
                        volunteerSP.bindVolunteerInfo(account, password, dataBean.getData().getUid());
                        Intent intent = new Intent(VolunteerTimeLoginActivity.this, VolunteerTimeActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                        break;
                    case 1:
                        showUnsuccessDialog("亲，输入的账号或密码有误哦");
                        break;
                    case 2:
                        showUnsuccessDialog("亲，输入的账号不存在哦");
                        break;
                    case 3:
                        showUnsuccessDialog("亲，请填入正确的密码");
                        break;
                    case 4:
                        showUnsuccessDialog("亲，请填入正确的账号");
                        break;
                }
            }
        }, account, password);
    }

    public void showProgressDialog() {
        dialog = new ProgressDialog(VolunteerTimeLoginActivity.this);
        dialog.setMessage("登录中...");
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showUnsuccessDialog(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> new MaterialDialog.Builder(VolunteerTimeLoginActivity.this)
                        .title("登录失败")
                        .content(text)
                        .positiveText("我知道啦")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                               // accountView.setText("");
                               // passwordView.setText("");
                            }
                        }).show());
            }
        });
    }
}
