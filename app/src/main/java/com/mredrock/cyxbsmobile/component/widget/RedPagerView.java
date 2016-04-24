package com.mredrock.cyxbsmobile.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.util.DensityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class RedPagerView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private int delay = 0;
    private int mode;
    private int gravity;
    private View mHintView;
    private Timer timer = new Timer();

    public RedPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray type = getContext().obtainStyledAttributes(attrs, R.styleable.red_viewpager);
        gravity = type.getInteger(R.styleable.red_viewpager_hint_gravity, 1);
        mode = type.getInteger(R.styleable.red_viewpager_hint_mode, 0);
        delay = type.getInt(R.styleable.red_viewpager_auto_play, 0);

        mViewPager = new ViewPager(getContext());
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mViewPager);
        type.recycle();

        initHint();
    }

    private void startPlay() {
        if (delay == 0 || mAdapter.getCount() <= 1) {
            return;
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int cur = mViewPager.getCurrentItem() + 1;
                    if (cur >= mAdapter.getCount()) {
                        cur = 0;
                    }
                    mViewPager.setCurrentItem(cur);
                    ((HintView) mHintView).setCurrent(cur);
                });
            }
        }, 0, delay);
    }


    private void initHint() {
        View hintView;
        switch (mode) {
            case 1:
                hintView = new TextHintView(getContext());
                break;
            default:
                hintView = new PointHintView(getContext());
                break;
        }
        addView(hintView);
        mHintView = hintView;
    }


    public ViewPager getmViewPager() {
        return mViewPager;
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        mAdapter = adapter;
        initHintView(adapter);
    }

    private void initHintView(PagerAdapter adapter) {

        //设置提示view的位置
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(), 24));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        (mHintView).setLayoutParams(lp);

        //设置提示view的背景
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT);
        gd.setAlpha(120);
        mHintView.setBackgroundDrawable(gd);

        //默认选择第一项
        ((HintView) mHintView).initView(adapter.getCount(), gravity);
        ((HintView) mHintView).setCurrent(0);

        startPlay();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        ((HintView) mHintView).setCurrent(arg0);
    }

    public interface HintView {
        void initView(int length, int gravity);

        void setCurrent(int current);
    }

    static class PointHintView extends LinearLayout implements RedPagerView.HintView {
        private ImageView[] mDots;
        private int length = 0;
        private int lastPosition = 0;

        private GradientDrawable dot_normal;
        private GradientDrawable dot_focus;

        public PointHintView(Context context) {
            super(context);
        }

        public PointHintView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void initView(int length, int gravity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                setLayoutDirection(LAYOUT_DIRECTION_LTR);
            }
            switch (gravity) {
                case 0:
                    setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    break;
                case 1:
                    setGravity(Gravity.CENTER);
                    break;
                case 2:
                    setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    break;
            }

            this.length = length;
            mDots = new ImageView[length];

            dot_focus = new GradientDrawable();
            dot_focus.setColor(getResources().getColor(R.color.colorAccent));
            dot_focus.setCornerRadius(DensityUtils.dp2px(getContext(), 4));
            dot_focus.setSize(DensityUtils.dp2px(getContext(), 8), DensityUtils.dp2px(getContext(), 8));

            dot_normal = new GradientDrawable();
            dot_normal.setColor(Color.GRAY);
            dot_normal.setAlpha(125);
            dot_normal.setCornerRadius(DensityUtils.dp2px(getContext(), 4));
            dot_normal.setSize(DensityUtils.dp2px(getContext(), 8), DensityUtils.dp2px(getContext(), 8));


            for (int i = 0; i < length; i++) {
                mDots[i] = new ImageView(getContext());
                LayoutParams dotLp = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                dotLp.setMargins(10, 0, 10, 0);
                mDots[i].setLayoutParams(dotLp);
                mDots[i].setBackgroundDrawable(dot_normal);
                addView(mDots[i]);
            }
        }

        @Override
        public void setCurrent(int current) {
            if (current < 0 || current > length - 1) {
                return;
            }
            mDots[lastPosition].setBackgroundDrawable(dot_normal);
            mDots[current].setBackgroundDrawable(dot_focus);
            lastPosition = current;
        }
    }

    static class TextHintView extends TextView implements RedPagerView.HintView {

        private int length;

        public TextHintView(Context context) {
            super(context);
        }

        public TextHintView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void initView(int length, int gravity) {
            this.length = length;
            setTextColor(Color.WHITE);
            switch (gravity) {
                case 0:
                    setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    break;
                case 1:
                    setGravity(Gravity.CENTER);
                    break;
                case 2:
                    setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    break;
            }
        }

        @Override
        public void setCurrent(int current) {
            setText(current + 1 + "/" + length);
        }

    }
}
