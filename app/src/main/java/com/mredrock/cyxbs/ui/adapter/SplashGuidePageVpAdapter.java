package com.mredrock.cyxbs.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created By jay68 on 2018/8/23.
 */
public class SplashGuidePageVpAdapter extends PagerAdapter {
    private ImageView[] mImageViews;

    public SplashGuidePageVpAdapter(ImageView[] imageViews) {
        mImageViews = imageViews;
    }

    @Override
    public int getCount() {
        return mImageViews == null ? 0 : mImageViews.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mImageViews[position]);
        return mImageViews[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
