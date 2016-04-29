package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class EditIntroduceActivity extends BaseActivity {

    public static final String EXTRA_EDIT_INTRODUCE = "extra_edit_introduce";

    @Bind(R.id.edit_introduce_toolbar) Toolbar editIntroduceToolbar;
    @Bind(R.id.edit_introduce_et) EditText editIntroduceEt;

    private User mUser;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_introduce);
        ButterKnife.bind(this);

        mUser = getIntent().getExtras()
                           .getParcelable(EditInfoActivity.EXTRA_USER);
        editIntroduceEt.setText(mUser.introduction);
        editIntroduceToolbar.setRightTextListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!editIntroduceEt.getText().toString().equals("")) {
                    RequestManager.getInstance()
                                  .setPersonNickName(new SimpleSubscriber<>(
                                                  EditIntroduceActivity.this, true,
                                                  new SubscriberListener<OkResponse>() {

                                                      @Override
                                                      public void onCompleted() {
                                                          super.onCompleted();
                                                          Intent intent = new Intent();
                                                          intent.putExtra
                                                                  (EXTRA_EDIT_INTRODUCE,editIntroduceEt.getText().toString());
                                                          setResult(RESULT_OK, intent);
                                                          finish();
                                                      }


                                                      @Override
                                                      public void onError(Throwable e) {
                                                          super.onError(e);
                                                          Toast.makeText(
                                                                  EditIntroduceActivity.this,
                                                                  "修改简介失败，原因：" +
                                                                          e.getMessage(),
                                                                  Toast.LENGTH_SHORT)
                                                               .show();
                                                          finish();
                                                      }
                                                  }), mUser.stunum, mUser.idNum,
                                          editIntroduceEt.getText().toString());
                }
            }
        });
        editIntroduceToolbar.setLeftTextListener(v -> {
            if(!editIntroduceEt.getText().toString().equals(mUser.introduction)){
                showDialog(EditIntroduceActivity.this);
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
