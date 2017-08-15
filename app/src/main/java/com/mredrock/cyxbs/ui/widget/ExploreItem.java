package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

/**
 * Created by Jay on 2017/8/15.
 */

public class ExploreItem extends RatioLayout {
    public ExploreItem(@NonNull Context context) {
        super(context);
    }

    public ExploreItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_explore, this, true);
        ImageView imageView = $(R.id.icon);
        TextView textView = $(R.id.title);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExploreItem);
        imageView.setImageResource(array.getResourceId(R.styleable.ExploreItem_icon, 0));
        textView.setText(array.getString(R.styleable.ExploreItem_title));
        array.recycle();
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }
}
