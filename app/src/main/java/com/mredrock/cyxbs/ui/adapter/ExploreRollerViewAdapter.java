package com.mredrock.cyxbs.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbs.model.RollerViewInfo;
import com.mredrock.cyxbs.ui.activity.explore.RollerViewActivity;
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

    public ExploreRollerViewAdapter(Context context, List<RollerViewInfo> urlList ) {
        if (context != null){
            mImageViews = new ArrayList<>();
            if (urlList == null) return;
            for (RollerViewInfo url : urlList) {
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (url.picture_goto_url != null) {
                    imageView.setOnClickListener(v -> RollerViewActivity.startRollerViewActivity(url.picture_goto_url, context)
                            //context.startActivity(new Intent(context, SplashActivity.class))
                    );
                }
                Glide.with(context).load(url.picture_url).into(imageView);
                mImageViews.add(imageView);
            }
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
