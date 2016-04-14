package com.mredrock.cyxbsmobile.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.R;

/**
 * Created by MathiasLuo on 2016/3/1.
 */
public class ImageLoader {

    private static ImageLoader instance;

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {

        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null)
                    instance = new ImageLoader();
            }
        }
        return instance;
    }

    public void loadImage(String url, ImageView imageView) {
        Glide.with(APP.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.avatar_default)
                .error(R.mipmap.avatar_default)
                .into(imageView);
    }

    public void loadImageWithTargetView(String url, SimpleTarget simpleTarget) {
        Glide.with(APP.getContext())
                .load(url)
                .asBitmap()
                .placeholder(R.mipmap.avatar_default)
                .error(R.mipmap.avatar_default)
                .into(simpleTarget);
    }

    public void loadImageWithListener(String url, SimpleTarget simpleTarget, RequestListener listener) {
        Glide.with(APP.getContext())
                .load(url)
                .asBitmap()
                .listener(listener)
                .placeholder(R.mipmap.avatar_default)
                .error(R.mipmap.avatar_default)
                .into(simpleTarget);
    }
}
