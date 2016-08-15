package com.excitingboat.freshmanspecial.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
  
    public RoundImageView(Context context) {
        super(context);  
        // TODO Auto-generated constructor stub  
    }  
  
    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  
    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int w = this.getWidth();  
        int h = this.getHeight();  
        /** 
         * RectF  圆角矩形 
         * **/  
        clipPath.addRoundRect(new RectF(0, 0, w, h), 30.0f, 30.0f,
                Path.Direction.CW);  
        canvas.clipPath(clipPath);  
        super.onDraw(canvas);  
    }  
}  