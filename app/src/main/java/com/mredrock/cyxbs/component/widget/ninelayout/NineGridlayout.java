package com.mredrock.cyxbs.component.widget.ninelayout;

/**
 * Created by mathiasluo on 16-4-11.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mredrock.cyxbs.model.social.Image;
import com.mredrock.cyxbs.util.ScreenTools;

import java.util.List;


/**
 * Created by Pan_ on 2015/2/2.
 */
public class NineGridlayout extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    protected int gap = 8;
    protected int columns;//
    protected int rows;//
    protected List listData;
    protected int totalWidth;
    protected OnAddImagItemClickListener mOnAddImagItemClickListener;
    protected OnNormalImagItemClickListener mOnNormalImagItemClickListener;
    protected OnClickDeletecteListener mOnClickDeletecteListener;

    public NineGridlayout(Context context) {
        super(context);
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ScreenTools screenTools = ScreenTools.instance(getContext());
        totalWidth = screenTools.getScreenWidth() - screenTools.dip2px(32);
    }

    public void setmOnClickDeletecteListener(OnClickDeletecteListener mOnClickDeletecteListener) {
        this.mOnClickDeletecteListener = mOnClickDeletecteListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setOnAddImagItemClickListener(OnAddImagItemClickListener mOnAddImagItemClickListener) {
        this.mOnAddImagItemClickListener = mOnAddImagItemClickListener;
    }

    public OnNormalImagItemClickListener getOnNormalImagItemClickListener() {
        return mOnNormalImagItemClickListener;
    }

    public void setOnNormalImagItemClickListener(OnNormalImagItemClickListener mOnNormalImagItemClickListener) {
        this.mOnNormalImagItemClickListener = mOnNormalImagItemClickListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    protected void layoutChildrenView(Image first_Image) {
        int childrenCount = listData.size();

        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        int singleHeight = singleWidth;

        //根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);
        for (int i = 0; i < childrenCount; i++) {
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            childrenView.setFocusable(false);
            // childrenView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            final int finalI = i;
            final int finalI1 = i;
            childrenView.setOnClickListener(view -> {
                if (((Image) listData.get(finalI)).getType() == Image.TYPE_ADD)
                    if (mOnAddImagItemClickListener != null)
                        mOnAddImagItemClickListener.onClick(view, finalI);
                    else if (mOnNormalImagItemClickListener != null)
                        mOnNormalImagItemClickListener.onClick(view, finalI1);
            });
            childrenView.setOnClickDelecteLIstener(v -> mOnClickDeletecteListener.onClickDelete(v, finalI));
            childrenView.setImageUrl(((Image) listData.get(i)).url);

            childrenView.setType(((Image) listData.get(i)).getType());
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);
        }
    }


    protected int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImagesData(List<Image> lists) {
        if (lists == null || lists.isEmpty()) {
            this.setVisibility(GONE);
            return;
        }
        this.setVisibility(VISIBLE);
        //初始化布局
        generateChildrenLayout(lists.size());


        //这里做一个重用view的处理
        /*  if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                CustomImageView iv = generateImageView();
                addView(iv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = lists.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    CustomImageView iv = generateImageView();
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }*/


        removeAllViews();
        int i = 0;
        while (i < lists.size()) {
            CustomImageView iv = generateImageView(i);
            addView(iv, generateDefaultLayoutParams());
            i++;
        }
        listData = lists;
        layoutChildrenView(lists.get(0));
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    protected CustomImageView generateImageView(int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setBackgroundColor(Color.parseColor("#ffffff"));
        if (i == 0) iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        else iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }

    public interface OnNormalImagItemClickListener {
        void onClick(View v, int position);
    }

    public interface OnAddImagItemClickListener {
        void onClick(View v, int position);
    }

    public interface OnClickDeletecteListener {
        void onClickDelete(View v, int position);
    }
}