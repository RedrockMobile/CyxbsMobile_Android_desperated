package com.excitingboat.yellowcake;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by PinkD on 2016/8/8.
 * ColorTextListView
 */
public class ColorTextListView extends FrameLayout {

    private static final boolean DEBUG = false;
    private static final String TAG = "ColorTextListView";

    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_LEFT = 2;
    public static final int GRAVITY_RIGHT = 3;


    private ColorTextAdapter colorTextAdapter;
    private int max;
    private int gravity;
    private float margin;
    private MyObserver myObserver;


    public ColorTextListView(Context context) {
        this(context, null);
    }

    public ColorTextListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ColorTextListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTextListView);
        max = typedArray.getInt(R.styleable.ColorTextListView_max, 3);
        gravity = typedArray.getInt(R.styleable.ColorTextListView_gravity, 1);
        margin = typedArray.getDimension(R.styleable.ColorTextListView_marginTop, 10);
        if (DEBUG) {
            Log.d(TAG, "ColorTextListView: gravity:" + gravity);
            Log.d(TAG, "ColorTextListView: max:" + max);
        }
        typedArray.recycle();

        myObserver = new MyObserver();
    }

    public void setAdapter(ColorTextAdapter colorTextAdapter) {
        this.colorTextAdapter = colorTextAdapter;
        colorTextAdapter.registerDataSetObserver(myObserver);
        refreshView();
    }

    private void refreshView() {
        int count = colorTextAdapter.getCount();
        for (int i = 0; i < count; i++) {
            ColorTextView child = new ColorTextView(getContext());
            colorTextAdapter.setData(new ViewHolder(child), i);
            addView(child, -1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int tmp = 1;
        int height = getMeasuredHeight();
        if (childCount != 0) {
            tmp = childCount / max;
            if (childCount % max != 0) {
                tmp++;
            }
            height = getChildAt(tmp - 1).getMeasuredHeight();
        }
        if (DEBUG) {
            Log.d(TAG, "onMeasure: " + tmp + "*" + height);
        }
        setMeasuredDimension(getMeasuredWidth(), (int) (tmp * (height + margin)));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom);
    }

    private void layoutChildren(int left, int top, int right, int bottom) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();
        int childCount = getChildCount();
        if (DEBUG) {
            Log.d(TAG, "layoutChildren:parentLeft: " + parentLeft);
            Log.d(TAG, "layoutChildren:parentRight: " + parentRight);
            Log.d(TAG, "layoutChildren:parentTop: " + parentTop);
            Log.d(TAG, "layoutChildren:parentBottom: " + parentBottom);
        }
        int remain = max;

        left = parentLeft;
        top = parentTop;
        boolean newLine = true;
        float len = (parentRight - parentLeft) / max;
        float marginStart = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (DEBUG) {
                Log.d(TAG, "layoutChildren:left: " + left);
                Log.d(TAG, "layoutChildren:top: " + top);
                Log.d(TAG, "layoutChildren:remain: " + remain);
                Log.d(TAG, "child.getMeasuredWidth(): " + child.getMeasuredWidth());
            }
            if (newLine) {
                switch (gravity) {
                    case GRAVITY_CENTER:
                        marginStart = (len - child.getMeasuredWidth()) / 2;
                        break;
                    case GRAVITY_LEFT:

                        break;
                    case GRAVITY_RIGHT:
                        marginStart = len - child.getMeasuredWidth() - margin;
                        break;
                }
                left += marginStart;
            }
            child.layout(left, top, (int) (left + len), parentBottom);
            if (remain > 1) {
                left += len;
                remain--;
                newLine = false;
            } else {
                remain = max;
                left = (int) (parentLeft + marginStart);
                top += child.getMeasuredHeight() + margin;
                if (DEBUG) {
                    Log.d(TAG, "layoutChildren:top----->" + top);
                }
            }
        }
    }


    class MyObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            removeAllViews();
            refreshView();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public class ViewHolder {
        private ColorTextView colorTextView;

        public ViewHolder(ColorTextView colorTextView) {
            this.colorTextView = colorTextView;
        }

        public RoundedRectangleView getRoundedRectangleView() {
            return colorTextView.getRoundedRectangleView();
        }

        public TextView getTextView() {
            return colorTextView.getTextView();
        }

    }
}
