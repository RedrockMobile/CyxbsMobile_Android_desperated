package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.LogUtils;

/**
 * Created by Stormouble on 16/5/5.
 */
public class TextDrawableView extends TextView {
    private static final String TAG = LogUtils.makeLogTag(TextDrawableView.class);

    private int drawableWidth = 0;
    private int drawableHeight = 0;

    public TextDrawableView(Context context) {
        this(context, null);
    }

    public TextDrawableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextDrawableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        parseAttributeSet(context, attrs, defStyleAttr);

        setupDrawable();
    }

    private void parseAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(
                attrs, R.styleable.TextDrawableView, defStyleAttr, 0);

        drawableWidth = array.getDimensionPixelOffset(
                R.styleable.TextDrawableView_drawable_width, 0);
        drawableHeight = array.getDimensionPixelOffset(
                R.styleable.TextDrawableView_drawable_height, 0);
        array.recycle();
    }

    private void setupDrawable() {
        Drawable[] drawableList = getCompoundDrawables();
        for (int i = 0; i < drawableList.length; i++) {
            Drawable drawable = drawableList[0];
            if (drawable != null) {
                drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            }
        }
    }
}
