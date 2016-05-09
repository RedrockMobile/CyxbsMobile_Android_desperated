package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by cc on 16/5/9.
 */
public class MainViewPager extends ViewPager {

    private boolean canScrollHorizontally = false;

    public void setCanScrollHorizontally(boolean canScrollHorizontally) {
        this.canScrollHorizontally = canScrollHorizontally;
    }

    public MainViewPager(Context context) {
        super(context);
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScrollHorizontally && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScrollHorizontally && super.onTouchEvent(ev);
    }
}
