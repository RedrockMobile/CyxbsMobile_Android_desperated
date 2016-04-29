package com.mredrock.cyxbsmobile.component.widget.bottombar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BottomBar extends LinearLayout {

    private int mCurrentPosition = -1;

    private OnBottomViewClickListener mListener;

    public BottomBar(Context context) {
        super(context);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mListener != null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                final int finalI = i;
                child.setOnClickListener(v -> setCurrentView(finalI));
            }
        }

        setCurrentView(0);
    }

    public void setOnBottomViewClickListener(OnBottomViewClickListener listener) {
        this.mListener = listener;
    }

    public void setCurrentView(final int position) {
        if (position != mCurrentPosition) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof IBottomView) {
                    if (i == position) {
                        ((IBottomView) child).onChoose();
                    } else {
                        ((IBottomView) child).onNormal();
                    }
                }
            }
            if (mListener != null) mListener.onClick(getChildAt(position), position);
            mCurrentPosition = position;
        }
    }
}
