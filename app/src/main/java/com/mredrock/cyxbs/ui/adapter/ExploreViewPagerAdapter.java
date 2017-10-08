package com.mredrock.cyxbs.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;

import java.util.ArrayList;

/**
 * Created by Jay on 2017/8/4.
 */

public class ExploreViewPagerAdapter extends PagerAdapter {
    private static final int[] IMAGES = new int[]{
            R.drawable.img_cqupt1,
            R.drawable.img_cqupt2,
            R.drawable.img_cqupt3
    };

    private ArrayList<ImageView> mImageViews;

    public ExploreViewPagerAdapter() {
        mImageViews = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int id : IMAGES) {
                ImageView imageView = new ImageView(BaseAPP.getContext());
                imageView.setImageResource(id);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                mImageViews.add(imageView);
            }
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mImageViews == null || mImageViews.isEmpty()) {
            return null;
        }
        View view = mImageViews.get(position % mImageViews.size());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews.get(position % mImageViews.size()));
    }
}
