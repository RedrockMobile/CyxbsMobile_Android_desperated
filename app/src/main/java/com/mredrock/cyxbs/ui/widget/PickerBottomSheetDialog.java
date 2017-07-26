package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.antfortune.freeline.util.ReflectUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.DensityUtils;

/**
 * Created by Jay on 2017/7/25.
 */

public class PickerBottomSheetDialog extends BottomSheetDialog implements View.OnClickListener {
    public interface OnClickListener {
        void onCancel();

        void onSure(String value, int position);
    }

    private NumberPicker mPicker;
    private ImageView mCancel;
    private TextView mSure;
    private String[] mData;
    private OnClickListener mOnClickListener;

    public PickerBottomSheetDialog(@NonNull Context context) {
        super(context);
        setFullWidth(context);
        mOnClickListener = new OnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String value, int position) {

            }
        };
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

    public void setData(String[] data) {
        mData = data;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picker_bottom_sheet);

        mCancel = (ImageView) findViewById(R.id.cancel);
        mSure = (TextView) findViewById(R.id.sure);
        mPicker = (NumberPicker) findViewById(R.id.number_picker);
        initNumberPicker();
        mCancel.setOnClickListener(this);
        mSure.setOnClickListener(this);
    }

    private void initNumberPicker() {
        mPicker.setDisplayedValues(mData);
        mPicker.setMinValue(0);
        mPicker.setMaxValue(mData.length - 1);
        changeNumberPickerTextColor();
    }

    private void changeNumberPickerTextColor() {
        final int count = mPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mPicker.getChildAt(i);
            if (child instanceof EditText) {
                fieldSet("mSelectionDivider", new ColorDrawable(Color.parseColor("#EDF6FD")));
                Paint paint = (Paint) fieldGet("mSelectorWheelPaint");
                if (paint != null) {
                    paint.setColor(Color.parseColor("#6D83EE"));
                }
                EditText editText = (EditText) child;
                editText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setTextColor(Color.parseColor("#6D83EE"));
                editText.setEnabled(false);
                editText.setFocusable(false);
            }
        }
        mPicker.invalidate();
    }

    private void fieldSet(String fieldName, Object value) {
        try {
            ReflectUtil.fieldSet(mPicker, mPicker.getClass(), fieldName, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object fieldGet(String fieldName) {
        try {
            return ReflectUtil.fieldGet(mPicker, mPicker.getClass(), fieldName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                mOnClickListener.onCancel();
                dismiss();
                break;

            case R.id.sure:
                mOnClickListener.onSure(mData[mPicker.getValue()], mPicker.getValue());
                dismiss();
                break;
        }
    }
}
