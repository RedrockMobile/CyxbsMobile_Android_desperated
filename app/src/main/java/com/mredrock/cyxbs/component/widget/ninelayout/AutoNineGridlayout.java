package com.mredrock.cyxbs.component.widget.ninelayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by mathiasluo on 16-4-11.
 */
public class AutoNineGridlayout extends NineGridlayout {
    public AutoNineGridlayout(Context context) {
        super(context);
    }

    public AutoNineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CustomImageView generateImageView(int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setBackgroundColor(Color.parseColor("#ffffff"));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }
}
