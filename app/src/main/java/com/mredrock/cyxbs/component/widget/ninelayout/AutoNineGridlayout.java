package com.mredrock.cyxbs.component.widget.ninelayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mredrock.cyxbs.model.social.Image;
import com.mredrock.cyxbs.util.ScreenTools;

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
    protected void layoutChildrenView(Image firstImage) {
        int childrenCount = listData.size();
        switch (childrenCount) {
            case 1:
                int singleWidth = ScreenTools.instance(getContext()).getScreenWidth();
                int singleHeight = singleWidth / 6 * 7;

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
                params.height = singleHeight * rows + gap * (rows - 1);
                // params.height = LayoutParams.MATCH_PARENT;
                params.leftMargin = 0;
                params.rightMargin = 0;
                setLayoutParams(params);
                //Log.e("--->>>firstImage", firstImage.url);
                CustomImageView childrenView = (CustomImageView) getChildAt(0);
   /*             LayoutParams layoutParams = childrenView.getLayoutParams();
                layoutParams.width = LayoutParams.MATCH_PARENT;
                layoutParams.height =LayoutParams.MATCH_PARENT;
                childrenView.setLayoutParams(layoutParams);*/
                childrenView.setScaleType(ImageView.ScaleType.FIT_XY);

                childrenView.setOnClickListener(view -> {
                    if (((Image) listData.get(0)).getType() == Image.TYPE_ADD)
                        if (mOnAddImagItemClickListener != null)
                            mOnAddImagItemClickListener.onClick(view, 0);
                        else if (mOnNormalImagItemClickListener != null)
                            mOnNormalImagItemClickListener.onClick(view, 0);
                });
                childrenView.setOnClickDelecteLIstener(v -> mOnClickDeletecteListener.onClickDelete(v, 0));
                childrenView.setImageUrl(((Image) listData.get(0)).url);

                childrenView.setType(((Image) listData.get(0)).getType());
                int[] position = findPosition(0);
                int left = 0;
                int top = (singleHeight + gap) * position[0];
                int right = left + singleWidth;
                int bottom = top + singleHeight;
                childrenView.layout(left, top, right, bottom);
                break;

            default:
                LinearLayout.LayoutParams param_1 = (LinearLayout.LayoutParams) getLayoutParams();
                param_1.leftMargin = ScreenTools.instance(getContext()).dip2px(16);
                param_1.rightMargin = param_1.leftMargin;
                setLayoutParams(param_1);

                super.layoutChildrenView(firstImage);
                break;
        }
    }

    @Override
    protected CustomImageView generateImageView() {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setBackgroundColor(Color.parseColor("#ffffff"));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }
}
