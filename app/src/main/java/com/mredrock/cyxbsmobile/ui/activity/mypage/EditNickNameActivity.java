package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.Toolbar;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;

public class EditNickNameActivity extends BaseActivity {

    public static final String EXTRA_NICK_NAME = "extra_nick_name";
    @Bind(R.id.edit_nick_toolbar) Toolbar editNickToolbar;
    @Bind(R.id.edit_nick_et) EditText editNickEt;
    @Bind(R.id.edit_nick_delete) ImageView editNickDelete;

    private User mUser;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nick_name);
        ButterKnife.bind(this);

        mUser = getIntent().getExtras()
                           .getParcelable(EditInfoActivity.EXTRA_USER);
        mUser.idNum = "26722X";
        editNickEt.setText(mUser.nickname);
        editNickDelete.setOnClickListener(v -> editNickEt.setText(""));
        editNickToolbar.setRightTextListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!editNickEt.getText().toString().equals("")) {
                    RequestManager.getInstance()
                                  .setPersonNickName(new SimpleSubscriber<>(
                                                  EditNickNameActivity.this, true,
                                                  new SubscriberListener<OkResponse>() {

                                                      @Override
                                                      public void onCompleted() {
                                                          super.onCompleted();
                                                          Intent intent = new Intent();
                                                          intent.putExtra(EXTRA_NICK_NAME,editNickEt.getText().toString());
                                                          setResult(RESULT_OK,intent);
                                                          finish();
                                                      }


                                                      @Override
                                                      public void onError(Throwable e) {
                                                          super.onError(e);
                                                          Toast.makeText(
                                                                  EditNickNameActivity.this,
                                                                  "修改昵称失败，原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                          finish();
                                                      }
                                                  }), mUser.stuNum, mUser.idNum, editNickEt.getText().toString());
                }
            }
        });
        editNickToolbar.setLeftTextListener(v -> {
            if(!editNickEt.getText().toString().equals(mUser.nickname)){
                showDialog(EditNickNameActivity.this);
            }
        });
    }

    private void showDialog(Context context) {
        new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .content("退出此次编辑？")
                .positiveText("退出")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        finish();
                    }


                    @Override public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
