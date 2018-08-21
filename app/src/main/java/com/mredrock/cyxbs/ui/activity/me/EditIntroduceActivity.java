package com.mredrock.cyxbs.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Toolbar;
import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.EditTextBottomSheetDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;

public class EditIntroduceActivity extends BaseActivity implements TextWatcher {

    public static final String EXTRA_EDIT_INTRODUCE = "extra_edit_introduce";

    public static final int MAX_SIZE_TEXT = 30;
    @BindView(R.id.edit_introduce_toolbar)
    Toolbar editIntroduceToolbar;
    @BindView(R.id.edit_introduce_et)
    EditText editIntroduceEt;
    @BindView(R.id.edit_introduce_count)
    TextView editIntroduceCount;

    private User mUser;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_introduce);
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        mUser = BaseAPP.getUser(this);

        editIntroduceEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_SIZE_TEXT)});
        editIntroduceEt.setText(mUser.introduction);
        editIntroduceEt.setSelection(editIntroduceEt.getText().toString().length());
        editIntroduceCount.setText(String.valueOf(
                MAX_SIZE_TEXT - (mUser.introduction == null ? 0 : mUser.introduction.length())));
        editIntroduceEt.addTextChangedListener(this);
        editIntroduceToolbar.setRightTextListener(v -> setPersonIntroduction());
        editIntroduceToolbar.setLeftTextListener(v -> {
            if (!editIntroduceEt.getText().toString().equals(mUser.introduction)) {
                showDialog(EditIntroduceActivity.this);
            } else {
                finish();
            }
        });
    }


    private void setPersonIntroduction() {
        if (EditTextBottomSheetDialog.emptyStr(editIntroduceEt.getText().toString())) {
            editIntroduceEt.setText("");
        }
        RequestManager.getInstance()
                .setPersonIntroduction(new SimpleObserver<>(
                                EditIntroduceActivity.this, true, new SubscriberListener<RedrockApiWrapper<Unit>>() {

                            @Override
                             public void onComplete() {
                                super.onComplete();
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_EDIT_INTRODUCE,
                                        editIntroduceEt.getText().toString());
                                setResult(RESULT_OK, intent);
                                finish();
                            }


                            @Override
                            public boolean onError(Throwable e) {
                                super.onError(e);
                                finish();
                                return false;
                            }
                        }), mUser.stuNum, mUser.idNum,
                        editIntroduceEt.getText().toString());
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


    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() < MAX_SIZE_TEXT) {
            int num = s.length();
            num = Math.max(MAX_SIZE_TEXT - num, 0);
            editIntroduceCount.setText(String.valueOf(num));
        } else {
            editIntroduceCount.setText(String.valueOf(0));
        }
    }
}
