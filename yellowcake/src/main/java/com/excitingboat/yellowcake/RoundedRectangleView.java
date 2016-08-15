package com.excitingboat.yellowcake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by PinkD on 2016/8/8.
 * RoundedRectangleView
 */
public class RoundedRectangleView extends View {
    private boolean DEBUG = false;
    private static final String TAG = "RoundedRectangleView";

    private int color;

    private Paint mPaint;
    private Path path;
    private RectF rectF;
    private float radius;

    public RoundedRectangleView(Context context) {
        this(context, null);
    }

    public RoundedRectangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedRectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        path = new Path();
        rectF = new RectF();
        color = 0xFF66CCFF;


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                if (DEBUG) {

                    Log.d(TAG, "widthMode: EXACTLY");
                }
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                if (DEBUG) {
                    Log.d(TAG, "widthMode: UNSPECIFIED");
                }
            case MeasureSpec.AT_MOST:
                if (DEBUG) {
                    Log.d(TAG, "widthMode: AT_MOST");
                }
                width = Utils.dp2px(getContext(), 24);
                break;

        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                if (DEBUG) {
                    Log.d(TAG, "heightMode: EXACTLY");
                }
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                if (DEBUG) {
                    Log.d(TAG, "heightMode: UNSPECIFIED");
                }
            case MeasureSpec.AT_MOST:
                if (DEBUG) {
                    Log.d(TAG, "heightMode: AT_MOST");
                }
                height = Utils.dp2px(getContext(), 24);
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "onMeasure: width:" + width + "height:" + height);
        }
        radius = Math.min(height, width) / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startX = 0;
        float startY = 0;
        if (getMeasuredWidth() > radius) {
            startX = getMeasuredWidth() / 2 - radius;
        }
        if (getMeasuredHeight() > radius) {
            startY = getMeasuredHeight() / 2 - radius;
        }
        resetPaint();
        mPaint.setColor(color);
        rectF.set(startX, startY, startX + 2 * radius, startY + 2 * radius);
        path.addRoundRect(rectF, radius / 3, radius / 3, Path.Direction.CCW);
        canvas.drawPath(path, mPaint);
    }


    private void resetPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


}
