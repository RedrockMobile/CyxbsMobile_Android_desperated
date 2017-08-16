package com.mredrock.freshmanspecial.units;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by zia on 17-8-12.
 */

public class MyViewPager extends ViewPager {
    private boolean isLocked;
    public interface OnMyClick{
        void onActionDown(MotionEvent event);
        void onTwoPointClick(MotionEvent event);
        void onOnePointClick(MotionEvent event);
        void onActionUp(MotionEvent event);
    }
    private OnMyClick onMyClick = null;

    public MyViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public void setOnMyClick(OnMyClick onMyClick){
        this.onMyClick = onMyClick;
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return !isLocked && super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if(ev.getPointerCount() >= 2){
            if(onMyClick != null) onMyClick.onTwoPointClick(ev);
        }else {
            if(onMyClick != null) onMyClick.onOnePointClick(ev);
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                Log.d("onTouchEvent", "MotionEvent.ACTION_UP");
                if(onMyClick != null) onMyClick.onActionUp(ev);
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d("onTouchEvent", "MotionEvent.ACTION_DOWN");
                if(onMyClick != null) onMyClick.onActionDown(ev);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
