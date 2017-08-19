package com.mredrock.freshmanspecial.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Jay on 2017/8/17.
 */

public class BarGraphView extends View {
    private static final int COLOR_FILL = Color.parseColor("#9efcee");
    private static final int COLOR_STROKE = Color.parseColor("#6cead5");
    private static final int COLOR_TEXT = Color.parseColor("#666666");

    private static final int MIN_VALUE = 10;
    private static final int MAX_VALUE = 300;
    private static final int GAP_VALUE = 10;

    private final int DEFAULT_RADIUS = (int) dp2px(80);
    private final int STROKE_WIDTH = dp2px(2);

    private int mValue;
    private int mRadius;
    private Paint mFillPaint;
    private Paint mStrokePaint;
    private Paint mTextPaint;

    public BarGraphView(Context context) {
        this(context, null);
    }

    public BarGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setDither(true);
        mFillPaint.setColor(COLOR_FILL);
        mFillPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setDither(true);
        mStrokePaint.setColor(COLOR_STROKE);
        mStrokePaint.setStrokeWidth(STROKE_WIDTH);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        // TODO: 2017/8/17 textPaint
        mTextPaint = new Paint();
        mTextPaint.setTextSize(dp2px(14));
        mTextPaint.setColor(COLOR_TEXT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*int width = 0;
        int height = 0;

        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                height = DEFAULT_RADIUS;
                mRadius = height / 2;
                width = value2px(MAX_VALUE);
                break;

            case MeasureSpec.AT_MOST:
                int leftWidth = MeasureSpec.getSize(widthMeasureSpec);
                width = Math.min(leftWidth, value2px(MAX_VALUE, DEFAULT_RADIUS));
                mRadius = width / (MAX_VALUE / GAP_VALUE);
                height = mRadius * 2;
                break;

            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                mRadius = width / (MAX_VALUE / GAP_VALUE);
                height = mRadius * 2;
                break;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);*/
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRadius = getMeasuredHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(0, 0, value2px(mValue), mRadius * 2);
        canvas.drawRoundRect(rect, mRadius, mRadius, mFillPaint);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (int) ((rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(mValue + "人", rect.right + dp2px(10), baseline, mTextPaint);
    }

    private int dp2px(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    private int value2px(int value) {
        return value2px(value, mRadius);
    }

    private int value2px(int value, int size) {
        if (value <= MIN_VALUE) {
            return size * 2;
        } else if (value >= MAX_VALUE) {
            return size * 2 + size * (MAX_VALUE - MIN_VALUE) / GAP_VALUE;
        } else {
            return size * 2 + size * (value - MIN_VALUE) / GAP_VALUE;
        }
    }

    public void setValue(int value) {
        mValue = value;
        postInvalidate();
    }

    public int getValue() {
        return mValue;
    }
}
