package com.mredrock.cyxbsmobile.component.widget.banner;

import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.util.LogUtils;

import java.util.List;

public abstract class BannerAdapter<T> {
    private List<T> mDatas;

    public List<T> getDatas() {
        return mDatas;
    }

    public BannerAdapter(List<T> datas) {
        mDatas = datas;
    }

    public void setImageViewSource(ImageView imageView, TextView textView, int position) {
        bindImage(imageView, mDatas.get(position));
    }

    public void selectTips(TextView tv, int position) {
        if (mDatas != null && mDatas.size() > 0)
            bindTips(tv, mDatas.get(position));
    }

    protected abstract void bindTips(TextView tv, T t);

    public abstract void bindImage(ImageView imageView, T t);


}
