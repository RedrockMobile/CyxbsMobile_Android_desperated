package com.mredrock.cyxbs.component.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

/**
 * Created by simonla on 2017/3/20.
 * 下午6:05
 */

public class TextLimitButton extends android.support.v7.widget.AppCompatButton {

    private int mTextLimit;
    private int mLimitColor;
    private int mFreeColor;
    private Paint mPaint;
    private float mRadius;
    private int mAnimSwitch = 0x01;
    private static final int OPEN_MASK = 0x01;
    private static final int CLOSE_MASK = 0x10;
    public static final String TAG = TextLimitButton.class.getSimpleName();

    public TextLimitButton(Context context) {
        this(context, null);
    }

    public TextLimitButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextLimitButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextLimitButton, defStyleAttr, 0);
        try {
            mTextLimit = array.getInteger(R.styleable.TextLimitButton_min_text_length, 0);
            mLimitColor = array.getColor(R.styleable.TextLimitButton_limit_background_color, Color.GRAY);
            mFreeColor = array.getColor(R.styleable.TextLimitButton_free_background_color, Color.BLUE);
        } finally {
            array.recycle();
        }
        setEnabled(false);
        setBackgroundColor(mLimitColor);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mFreeColor);
    }

    public void addTextView(TextView textView) {
        textView.addTextChangedListener(new OutTextWatcher());
    }

    private class OutTextWatcher implements TextWatcher {
        TextLimitButton mTextLimitButton = TextLimitButton.this;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (shouldOpen(s.length())) {
                mTextLimitButton.setEnabled(true);
                mTextLimitButton.setBackgroundColor(mFreeColor);
                if ((mAnimSwitch & OPEN_MASK) == OPEN_MASK) doAnim(true);
            } else {
                mTextLimitButton.setBackgroundColor(mLimitColor);
                mTextLimitButton.setEnabled(false);
                if ((mAnimSwitch & CLOSE_MASK) == CLOSE_MASK) doAnim(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean shouldOpen(int length) {
        return (mTextLimit != 0 && length >= mTextLimit) || (mTextLimit == 0 && length > 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mFreeColor);
        canvas.drawCircle(0, getHeight(), mRadius, mPaint);
        super.onDraw(canvas);
    }

    private void doAnim(boolean isOpen) {
        ValueAnimator animator = ObjectAnimator.ofFloat(this, "null", 0.0f,
                (float) Math.hypot(getWidth(), getHeight()));
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(animation -> {
            if (isOpen) {
                mRadius = (float) animation.getAnimatedValue();
                mAnimSwitch &= ~OPEN_MASK;
                mAnimSwitch |= CLOSE_MASK;
            } else {
                mRadius = (float) Math.hypot(getWidth(), getHeight()) - (float) animation.getAnimatedValue();
                mAnimSwitch &= ~CLOSE_MASK;
                mAnimSwitch |= OPEN_MASK;
            }
            invalidate();
        });
        animator.start();
    }
}
