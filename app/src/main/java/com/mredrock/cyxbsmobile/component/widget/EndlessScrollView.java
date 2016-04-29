package com.mredrock.cyxbsmobile.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Stormouble on 16/4/23.
 */
public class EndlessScrollView extends ScrollView {

    private OnBottomListener listener;

    public interface OnBottomListener {
        void onBottom();
    }

    public EndlessScrollView(Context context) {
        super(context);
    }

    public EndlessScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = getChildAt(getChildCount() - 1);

        int diff = (view.getBottom() - (getHeight() + getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            listener.onBottom();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnBottomListener(OnBottomListener listener) {
        this.listener = listener;
    }
}
