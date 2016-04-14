package com.mredrock.cyxbsmobile.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbsmobile.model.community.Image;

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
    protected void layoutChildrenView() {
        int childrenCount = listData.size();
        switch (childrenCount) {
            case 1:
                int singleWidth = totalWidth - gap * 2;
                int singleHeight = singleWidth / 3 * 2;
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = singleHeight * rows + gap * (rows - 1);
                setLayoutParams(params);

                CustomImageView childrenView = (CustomImageView) getChildAt(0);
                childrenView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((Image) listData.get(0)).getType() == Image.ADDIMAG)
                            if (mOnAddImagItemClickListener != null)
                                mOnAddImagItemClickListener.onClick(view, 0);
                            else if (mOnNormalImagItemClickListener != null)
                                mOnNormalImagItemClickListener.onClick(view, 0);
                    }
                });
                childrenView.setOnClickDelecteLIstener(new CustomImageView.OnClickDelecteListener() {
                    @Override
                    public void onClickDelect(View v) {
                        mOnClickDeletecteListener.onClickDelete(v, 0);
                    }
                });
                childrenView.setImageUrl(((Image) listData.get(0)).getUrl());
                childrenView.setType(((Image) listData.get(0)).getType());
                int[] position = findPosition(0);
                int left = (singleWidth + gap) * position[1];
                int top = (singleHeight + gap) * position[0];
                int right = left + singleWidth;
                int bottom = top + singleHeight;
                childrenView.layout(left, top, right, bottom);
                break;

            default:
                super.layoutChildrenView();
                break;
        }
    }
}
