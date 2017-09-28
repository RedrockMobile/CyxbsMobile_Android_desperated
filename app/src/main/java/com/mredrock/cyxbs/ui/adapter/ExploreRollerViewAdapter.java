package com.mredrock.cyxbs.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

    public ExploreRollerViewAdapter(Context context, List<Url> urlList ) {
        mImageViews = new ArrayList<>();
        for (Url url : urlList) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (url.webUrl != null) {
                imageView.setOnClickListener(v -> RollerViewActivity.startRollerViewActivity(url.webUrl, context)
                        //context.startActivity(new Intent(context, SplashActivity.class))
                );
            }
            Glide.with(context).load(url.imageUrl).into(imageView);
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

    public static class Url {
        public Url(String imageUrl, String webUrl) {
            this.imageUrl = imageUrl;
            this.webUrl = webUrl;
        }

        private String imageUrl;
        private String webUrl;
    }
}
