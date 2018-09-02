package com.mredrock.cyxbs.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Toolbar;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import kotlin.Unit;


/**
 * Created by skylineTan on 2016/5/5 19:03.
 */
public abstract class EditCommonActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.edit_common_toolbar)
    Toolbar editCommonToolbar;
    @BindView(R.id.edit_common_et)
    EditText editCommonEt;
    @BindView(R.id.edit_common_delete)
    ImageView editCommonDelete;
    @BindView(R.id.edit_common_count)
    TextView editCommonCount;

    protected User mUser;
    private String editTextContent;

    protected abstract void provideData(Observer<Unit> observer, String
            stuNum, String idNum, String info);

    protected abstract String getExtra();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_common);
        ButterKnife.bind(this);
        initToolbar();
        init();
    }

    private void initToolbar() {
        if (editCommonToolbar != null) {
            setSupportActionBar(editCommonToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    private void init() {
        mUser = BaseAPP.getUser(this);
        switch (getExtra()) {
            case Const.Extras.EDIT_QQ:
                editCommonEt.setHint("客官，留个QQ呗～");
                editTextContent = mUser.qq;
                editCommonEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                editCommonToolbar.setTitle("QQ");
                break;
            case Const.Extras.EDIT_PHONE:
                editCommonEt.setHint("客官，留个电话呗～");
                editCommonEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextContent = mUser.phone;
                editCommonToolbar.setTitle("电话");
                break;
            case Const.Extras.EDIT_NICK_NAME:
                editCommonEt.setHint("怎么可以没有昵称");
                editCommonEt.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextContent = mUser.nickname;
                editCommonToolbar.setTitle("昵称");
                break;
        }

        editCommonEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(getExtra()))});
        editCommonEt.setText(editTextContent);
        editCommonEt.setSelection(editCommonEt.getText().toString().length());
        editCommonCount.setText(String.valueOf(
                Integer.parseInt(getExtra()) - (editTextContent == null ? 0 : editTextContent.length())));
        editCommonEt.addTextChangedListener(this);
        editCommonDelete.setOnClickListener(v -> editCommonEt.setText(""));
        editCommonToolbar.setRightTextListener(v -> setPersonInfo());
        editCommonToolbar.setLeftTextListener(v -> {
            if (!editCommonEt.getText().toString().equals(editTextContent)) {
                showDialog(EditCommonActivity.this);
            } else {
                finish();
            }
        });
    }


    protected void setPersonInfo() {
        provideData(new SimpleObserver<>(this, true,
                new SubscriberListener<Unit>() {

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent();
                        intent.putExtra(getExtra(), editCommonEt.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        super.onError(e);
                        finish();
                        return false;
                    }

                }), mUser.stuNum, mUser.idNum, editCommonEt.getText().toString());
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
        editCommonCount.setText(String.valueOf(num));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() < Integer.parseInt(getExtra())) {
            int num = s.length();
            num = Math.max(Integer.parseInt(getExtra()) - num, 0);
            editCommonCount.setText(String.valueOf(num));
        } else {
            editCommonCount.setText(String.valueOf(0));
        }
    }
}
