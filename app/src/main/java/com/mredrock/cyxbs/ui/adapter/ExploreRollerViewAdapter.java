package com.mredrock.cyxbs.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbs.ui.widget.RollerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2017/8/4.
 */

public class ExploreRollerViewAdapter extends RollerView.RollerViewAdapter {
    private List<ImageView> mImageViews;

    public ExploreRollerViewAdapter(Context context, int[] images) {
        mImageViews = new ArrayList<>();
        for (int id : images) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(id).into(imageView);
            mImageViews.add(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mImageViews.size();
    }

    @Override
    public View getView(ViewGroup container, int position) {
        return mImageViews.get(position);
    }
}
