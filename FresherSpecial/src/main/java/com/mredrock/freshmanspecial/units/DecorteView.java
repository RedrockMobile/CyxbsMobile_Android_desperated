package com.mredrock.freshmanspecial.units;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by zia on 17-8-4.
 */

public class DecorteView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private int count = 3;

    public DecorteView(Context context) {
        super(context);
    }

    public DecorteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DecorteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.parseColor("#CCCCCC"));
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setCount(int count){
        this.count = count;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这里取高度的三分之二作为分割线
        canvas.drawLine(getWidth()/count,getHeight()/3,getWidth()/count,getHeight()/3*2,mPaint);
        canvas.drawLine(getWidth()/count*(count-1),getHeight()/3,getWidth()/count*(count-1),getHeight()/3*2,mPaint);
    }
}
