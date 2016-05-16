package com.mredrock.cyxbs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ninelayout.CustomImageView;

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

    public void loadOfficalImg(String url, ImageView imageView, View itemView) {
        Context context = itemView.getContext();
        Glide.with(APP.getContext())
                .load(url.startsWith("http") ? url : CustomImageView.BASE_THUMBNAIL_IMG_URL + url)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .crossFade()
                .transform(new BitmapTransformation(context) {
                    @Override
                    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
                        int width = itemView.getWidth();
                        float resize;
                        int w;
                        if (width == 0) {
                            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                            w = wm.getDefaultDisplay()
                                    .getWidth() - Utils.dip2px(context, 16);
                            resize = (float) (w * 1.0 / toTransform.getWidth());
                        } else {
                            resize = (float) (width * 1.0 / toTransform.getWidth());
                            w = width;
                        }
                        int h = (int) (toTransform.getHeight() * resize);
                        if (h <= 0 || w <= 0) {
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            return toTransform;
                        }
                        Bitmap res = pool.get(w, h, Bitmap.Config.ARGB_8888);
                        if (res == null) {
                            res = Bitmap.createScaledBitmap(toTransform, w, h, false);
                        }
                        if (res != toTransform) {
                            toTransform.recycle();
                        }
                        return res;
                    }

                    @Override
                    public String getId() {
                        return "item.news.single.image";
                    }
                })
                .into(imageView);
    }


    public void loadRedrockImage(String url, ImageView imageView) {
        if (url.length() > 0) {
            Glide.with(APP.getContext())
                    .load(url.startsWith("http") ? url : CustomImageView.BASE_THUMBNAIL_IMG_URL + url)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .crossFade()
                    .into(imageView);

        }
    }

    public void loadAvatar(String url, ImageView imageView) {
        Glide.with(APP.getContext())
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(imageView);
    }

    public void loadDefaultNewsAvatar(ImageView imageView) {
        imageView.setImageResource(R.drawable.ic_official_notification);
    }


    public void loadImageWithTargetView(String url, SimpleTarget simpleTarget) {
        Glide.with(APP.getContext())
                .load(url.startsWith("http") ? url : CustomImageView.BASE_THUMBNAIL_IMG_URL + url)
                .asBitmap()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(simpleTarget);
    }

    @SuppressWarnings("unchecked")
    public void loadLocalImage(int resId, SimpleTarget<Bitmap> simpleTarget) {
        Glide.with(APP.getContext())
                .load(resId)
                .asBitmap()
                .centerCrop()
                .into(simpleTarget);
    }

}
