package com.mredrock.cyxbs.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.EditText;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Toolbar;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.util.Utils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import io.reactivex.Observer;
import kotlin.Unit;


public class EditNickNameActivity extends EditCommonActivity {

    @BindView(R.id.edit_common_toolbar)
    Toolbar editCommonToolbar;

    @BindView(R.id.edit_common_et)
    EditText editCommonEt;

    boolean isForceModify;


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    protected void provideData(Observer<Unit> observer, String stuNum, String idNum, String info) {
        RequestManager.getInstance().setPersonNickName(observer, stuNum, idNum, info);
    }

    private void initialize() {
        isForceModify = !BaseAPP.hasNickName();
        if (isForceModify) {
            editCommonToolbar.setLeftText("");
            editCommonToolbar.setLeftTextListener(null);
        }
        editCommonToolbar.setRightTextListener(v -> {
            String input = editCommonEt.getText().toString().replaceAll("\\s", "");
            if (input.length() == 0) {
                Utils.toast(EditNickNameActivity.this, "你还没有输入昵称哟！");
            } else {
                editCommonEt.setText(input);
                EditNickNameActivity.super.setPersonInfo();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Snackbar.make(editCommonEt, "要有昵称才能浏览哦~~~", Snackbar.LENGTH_SHORT).show();
        if (!isForceModify) {
            super.onBackPressed();
        }
    }

    @Override
    protected String getExtra() {
        return Const.Extras.EDIT_NICK_NAME;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, EditNickNameActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

}
