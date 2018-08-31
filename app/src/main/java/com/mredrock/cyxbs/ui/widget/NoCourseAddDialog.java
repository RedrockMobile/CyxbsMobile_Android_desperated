package com.mredrock.cyxbs.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ClearableEditText;
import com.mredrock.cyxbs.util.DensityUtils;

/**
 * Created by Jay on 2017/7/21.
 */

public class NoCourseAddDialog extends Dialog implements View.OnClickListener {
    public interface OnClickListener {
        void onCancel();

        void onSure(EditText editText);
    }

    private ClearableEditText mStuNum;
    private Button mCancel;
    private Button mSure;
    private OnClickListener mListener;

    public NoCourseAddDialog(@NonNull Context context) {
        super(context);
        Window window = getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        View decorView = window.getDecorView();
        decorView.getWindowVisibleDisplayFrame(new Rect());
        windowparams.width = DensityUtils.getScreenWidth(context)
                - DensityUtils.dp2px(context, 46);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(windowparams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_course_add_stu);

        mStuNum = (ClearableEditText) findViewById(R.id.stu_num);
        mCancel = (Button) findViewById(R.id.cancel);
        mSure = (Button) findViewById(R.id.sure);
        mCancel.setOnClickListener(this);
        mSure.setOnClickListener(this);
    }

    public void setListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null)
            return;
        switch (v.getId()) {
            case R.id.cancel:
                mListener.onCancel();
                break;

            case R.id.sure:
                mListener.onSure(mStuNum);
                break;
        }
    }
}
