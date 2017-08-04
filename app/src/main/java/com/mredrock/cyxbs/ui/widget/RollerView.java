package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mredrock.cyxbs.R;

/**
 * Created by Jay on 2017/8/4.
 */

public class RollerView extends FrameLayout implements Runnable, ViewPager.PageTransformer {
    private ViewPager mViewPager;
    private RollerViewAdapter mRollerViewAdapter;

    private int mPageMargin;
    private float mPageScale;
    private float mEdgeSize;
    private boolean mShowMultiPage;
    private float mAspectRatio;
    private int mDelay;

    private boolean mAllowAnimator;

    public RollerView(@NonNull Context context) {
        this(context, null);
    }

    public RollerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initView();
        if (mShowMultiPage) {
            showMultiPage();
        }
    }

    private void initView() {
        mViewPager = new ViewPager(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mViewPager.setLayoutParams(params);
        mViewPager.setAdapter(new Adapter());
        mViewPager.setPageTransformer(true, this);
        addView(mViewPager);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RollerView);
        mPageMargin = array.getDimensionPixelSize(R.styleable.RollerView_pageMargin, 0);
        mEdgeSize = array.getDimensionPixelSize(R.styleable.RollerView_EdgePageSize, 0);
        mPageScale = array.getFloat(R.styleable.RollerView_pageScale, 1);
        mAspectRatio = array.getFloat(R.styleable.RollerView_aspectRatio, 0);
        mShowMultiPage = array.getBoolean(R.styleable.RollerView_showMultiPage, true);
        mDelay = array.getInt(R.styleable.RollerView_delay, 5000);
        array.recycle();
    }

    private void showMultiPage() {
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setPageMargin(mPageMargin);

        mViewPager.setClipChildren(false);
        ViewGroup root = (ViewGroup) getRootView();
        ViewGroup parent = (ViewGroup) mViewPager.getParent();
        while (root != parent) {
            parent.setClipChildren(false);
            parent = (ViewGroup) parent.getParent();
        }
        root.setClipChildren(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //处理比例模式
        if (mAspectRatio != 0 && widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            widthSize = widthSize == 0 ? (int) (heightSize * mAspectRatio) : widthSize;
            heightSize = heightSize == 0 ? (int) (widthSize / mAspectRatio) : heightSize;
        }
        //使viewpager显示多个页面
        if (mShowMultiPage) {
            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.height = heightSize;
            params.width = (int) (widthSize - mPageMargin * 2 - mEdgeSize * 2);
            mViewPager.setLayoutParams(params);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, widthMode),
                MeasureSpec.makeMeasureSpec(heightSize, heightMode));
    }

    public void setAdapter(RollerViewAdapter adapter) {
        if (adapter != null) {
            mRollerViewAdapter = adapter;
            ((Adapter) mViewPager.getAdapter()).setAdapter(adapter);
            mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
            mViewPager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAllowAnimator = true;
        mViewPager.post(this);
    }

    @Override
    public void run() {
        int position = mViewPager.getCurrentItem() + 1;
        mViewPager.setCurrentItem(position < 0 ? 0 : position, true);
        if (mAllowAnimator) {
            mViewPager.postDelayed(this, mDelay);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAllowAnimator = false;
    }

    @Override
    public void transformPage(View view, float position) {
        final float minAlpha = 0.5f;
        CardView cardView;
        if (view instanceof CardView) {
            cardView = (CardView) view;
        }
        if (position < -1) {
            view.setAlpha(minAlpha);
            view.setScaleX(mPageScale);
            view.setScaleY(mPageScale);
        } else if (position <= 1) {
            float scaleFactor = Math.max(mPageScale, 1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setAlpha(minAlpha + (scaleFactor - mPageScale)
                    / (1 - mPageScale) * (1 - minAlpha));
        } else {
            view.setAlpha(minAlpha);
            view.setScaleX(mPageScale);
            view.setScaleY(mPageScale);
        }
    }

    private class Adapter extends PagerAdapter {
        private RollerViewAdapter mRollerViewAdapter;

        public void setAdapter(RollerViewAdapter adapter) {
            mRollerViewAdapter = adapter;
        }

        @Override
        public int getCount() {
            return mRollerViewAdapter == null ? 0 : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mRollerViewAdapter.getView(container,
                    position % mRollerViewAdapter.getItemCount());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            removeView((View) object);
        }
    }

    public abstract static class RollerViewAdapter {
        public abstract int getItemCount();

        public abstract View getView(ViewGroup container, int position);
    }
}