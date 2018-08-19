package com.mredrock.cyxbs.freshman.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.MilitaryShow;
import com.mredrock.cyxbs.freshman.ui.activity.PhotoViewerActivityKt;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import java.util.List;

public class ViewPagerPhotoCardAdapter extends PagerAdapter {
    private Context context;
    private List<MilitaryShow.PictureBean> datas;
    private List<String> photos;
    private boolean isCat = false;//判断是否拼接了字符串

    public ViewPagerPhotoCardAdapter(Context context, List<MilitaryShow.PictureBean> datas, List<String> photos) {
        this.context = context;
        this.datas = datas;
        this.photos = photos;
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
        position %= datas.size();
        if (position < 0) {
            position = datas.size() + position;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.freshman_item_military_photo, null);
        TextView tv = view.findViewById(R.id.freshman_military_card_number);
        RoundedImageView imageView = view.findViewById(R.id.freshman_military_card_photo);


        tv.setText(datas.get(position).getName());
        Glide.with(context)
                .load(Const.IMG_BASE_URL + datas.get(position).getUrl())
                .asBitmap()
                .centerCrop()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        imageView.setImageBitmap(resource);
                    }
                });
        ViewParent parent = view.getParent();
        if (parent != null) {
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.removeView(view);
        }

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            ViewGroup.LayoutParams lp = tv.getLayoutParams();
            lp.height = imageView.getHeight() / 4;
            tv.setLayoutParams(lp);
            imageView.setAdjustViewBounds(true);
        });


        int finalPosition = position;


        imageView.setOnClickListener(v -> {
            if (!isCat) {
                for (int i = 0; i < photos.size(); i++) {
                    photos.set(i, Const.IMG_BASE_URL + photos.get(i));
                }
                isCat = true;
            }
            PhotoViewerActivityKt.start(context, photos, finalPosition);
        });
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
