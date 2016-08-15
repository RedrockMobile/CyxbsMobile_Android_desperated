package com.excitingboat.freshmanspecial.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.excitingboat.freshmanspecial.R;

/**
 * Created by PinkD on 2016/8/9.
 * RoundRectImageView
 */
public class RoundRectImageView extends ImageView {
    private static final String TAG = "RoundRectImageView";
    private float roundDegree = 0;
    private Path clipPath;
    private RectF rectF;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public RoundRectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView);
        roundDegree = typedArray.getDimension(R.styleable.RoundRectImageView_roundDegree, 0);
        typedArray.recycle();
        clipPath = new Path();
        rectF = new RectF();
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        clipPath.addRoundRect(rectF, roundDegree, roundDegree, Path.Direction.CW);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.clipPath(clipPath, Region.Op.INTERSECT);
        super.onDraw(canvas);
    }
}
