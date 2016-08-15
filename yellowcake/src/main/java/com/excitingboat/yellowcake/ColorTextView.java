package com.excitingboat.yellowcake;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by PinkD on 2016/8/8.
 * ColorTextView
 */
public class ColorTextView extends FrameLayout {
    private int paddingStart;
    private TextView mTextView;
    private RoundedRectangleView mRoundedRectangleView;


    public ColorTextView(Context context) {
        this(context, null);
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.color_text_view, this);
        mTextView = (TextView) view.findViewById(R.id.text_view);
        mRoundedRectangleView = (RoundedRectangleView) view.findViewById(R.id.rounded_rectangle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mTextView.getMeasuredWidth() + mRoundedRectangleView.getMeasuredWidth(), getMeasuredHeight());
    }

    public int getColor() {
        return mRoundedRectangleView.getColor();
    }

    public void setColor(int color) {
        mRoundedRectangleView.setColor(color);
    }

    public String getText() {
        return mTextView.getText().toString();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setPaddingStart(int paddingStart) {
        this.paddingStart = paddingStart;
        mTextView.setPadding(paddingStart, 0, 0, 0);
    }

    public RoundedRectangleView getRoundedRectangleView() {
        return mRoundedRectangleView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public int getPaddingStart() {
        return paddingStart;
    }
}
