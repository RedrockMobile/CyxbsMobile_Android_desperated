package com.mredrock.cyxbsmobile.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.Toolbar;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;

public class EditIntroduceActivity extends BaseActivity implements TextWatcher {

    public static final String EXTRA_EDIT_INTRODUCE = "extra_edit_introduce";
    public static final int MAX_SIZE_TEXT = 30;
    @Bind(R.id.edit_introduce_toolbar) Toolbar editIntroduceToolbar;
    @Bind(R.id.edit_introduce_et) EditText editIntroduceEt;
    @Bind(R.id.edit_introduce_count) TextView editIntroduceCount;

    private User mUser;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_introduce);
        ButterKnife.bind(this);

        mUser = getIntent().getExtras()
                           .getParcelable(Const.Extras.EDIT_USER);
        init();
    }


    private void init() {
        editIntroduceEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_SIZE_TEXT)});
        editIntroduceEt.setText(mUser.introduction);
        editIntroduceEt.setSelection(mUser.introduction.length());
        editIntroduceCount.setText(String.valueOf(MAX_SIZE_TEXT - mUser.introduction.length()));
        editIntroduceEt.addTextChangedListener(this);
        editIntroduceToolbar.setRightTextListener(v -> setPersonIntroduction());
        editIntroduceToolbar.setLeftTextListener(v -> {
            if (!editIntroduceEt.getText().toString().equals(mUser.introduction)) {
                showDialog(EditIntroduceActivity.this);
            }else {
                finish();
            }
        });
    }


    private void setPersonIntroduction() {
        if (!editIntroduceEt.getText().toString().equals("")) {
            RequestManager.getInstance()
                          .setPersonIntroduction(new SimpleSubscriber<>(
                                          EditIntroduceActivity.this, true, new SubscriberListener<OkResponse>() {

                                      @Override public void onCompleted() {
                                          super.onCompleted();
                                          Intent intent = new Intent();
                                          intent.putExtra(EXTRA_EDIT_INTRODUCE,
                                                  editIntroduceEt.getText().toString());
                                          setResult(RESULT_OK, intent);
                                          finish();
                                      }


                                      @Override public void onError(Throwable e) {
                                          super.onError(e);
                                          Toast.makeText(EditIntroduceActivity.this, "修改简介失败，原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                          finish();
                                      }
                                  }), mUser.stunum, mUser.idNum,
                                  editIntroduceEt.getText().toString());
        }
    }


    private void showDialog(Context context) {
        new MaterialDialog.Builder(context).theme(Theme.LIGHT)
                                           .content("退出此次编辑？")
                                           .positiveText("退出")
                                           .negativeText("取消")
                                           .callback(new MaterialDialog.ButtonCallback() {
                                                       @Override
                                                       public void onPositive(MaterialDialog dialog) {
                                                           super.onPositive(dialog);
                                                           finish();
                                                       }


                                                       @Override
                                                       public void onNegative(MaterialDialog dialog) {
                                                           super.onNegative(dialog);
                                                           dialog.dismiss();
                                                       }
                                                   })
                                           .show();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        int num = s.length();
        editIntroduceCount.setText(String.valueOf(num));
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override public void afterTextChanged(Editable s) {
        if(s.length() < MAX_SIZE_TEXT) {
            int num = s.length();
            num = Math.max(MAX_SIZE_TEXT - num, 0);
            editIntroduceCount.setText(String.valueOf(num));
        }else {
            editIntroduceCount.setText(String.valueOf(0));
        }
    }
}
