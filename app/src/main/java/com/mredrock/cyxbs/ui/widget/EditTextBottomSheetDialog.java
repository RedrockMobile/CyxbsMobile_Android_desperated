package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.DensityUtils;

/**
 * Created by Jay on 2017/8/2.
 */

public class EditTextBottomSheetDialog extends BottomSheetDialog implements View.OnClickListener {
    public interface OnClickListener {
        void onCancel();

        void onSend(EditText editText);
    }

    private ImageView mCancel;
    private TextView mSend;
    private EditText mEditText;
    private OnClickListener mOnClickListener;

    private String mText = "";

    public EditTextBottomSheetDialog(@NonNull Context context) {
        super(context);
        setFullWidth(context);
        mOnClickListener = new OnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSend(EditText editText) {

            }
        };
    }

    public void setText(String text) {
        mText = text;
        if (mEditText != null) {
            mEditText.setText(mText);
        }
    }

    private void setFullWidth(Context context) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        View decorView = window.getDecorView();
        decorView.getWindowVisibleDisplayFrame(new Rect());
        layoutParams.width = DensityUtils.getScreenWidth(context);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(layoutParams);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_bottom_sheet);

        mCancel = (ImageView) findViewById(R.id.cancel);
        mSend = (TextView) findViewById(R.id.send);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mCancel.setOnClickListener(this);
        mSend.setOnClickListener(this);

        mEditText.setText(mText);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mText = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                mOnClickListener.onCancel();
                dismiss();
                break;

            case R.id.send:
                mOnClickListener.onSend(mEditText);
                break;
        }
    }
}
