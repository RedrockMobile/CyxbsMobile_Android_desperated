package com.mredrock.cyxbs.component.widget.ninelayout;

/**
 * Created by mathiasluo on 16-4-11.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.social.Image;
import com.mredrock.cyxbs.util.DensityUtils;

/**
 * Created by Pan_ on 2015/2/2.
 */
public class CustomImageView extends android.support.v7.widget.AppCompatImageView {


    public static final String BASE_NORMAL_IMG_URL = "http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/";
    public static final String BASE_THUMBNAIL_IMG_URL = BASE_NORMAL_IMG_URL + "thumbnail/";

    private String url;
    private boolean isAttachedToWindow;
    private OnClickDeleteListener onClickDeleteListener;
    private int type;
    private Bitmap mBitmapBack;


    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomImageView(Context context) {
        this(context,null);
    }

    private void init(Context context) {
        mBitmapBack = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
        int iconSize = DensityUtils.dp2px(context, 26);
        mBitmapBack = Bitmap.createScaledBitmap(mBitmapBack, iconSize, iconSize, false);
    }

    public int getType() {
        return type;
    }

    public ImageView setType(int type) {
        this.type = type;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int w = DensityUtils.dp2px(getContext(), 18);
                if (x > getWidth() - 2 * w && y < 2 * w) {
                    if (onClickDeleteListener != null && type == Image.TYPE_NORMAL)
                        onClickDeleteListener.onClickDelete(this);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }

    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {
                Glide.with(getContext())
                        .load(url.startsWith("http") || url.startsWith("file:") || url.startsWith("/storage") ? url : BASE_THUMBNAIL_IMG_URL + url)
                        .placeholder(R.drawable.place_holder)
                        .into(this);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (type == Image.TYPE_NORMAL) {
            int w = canvas.getWidth();
            int blackPlace = mBitmapBack.getHeight();
            canvas.drawBitmap(mBitmapBack, w - blackPlace-blackPlace/8 , blackPlace/8, null);
        }
    }

    public void setOnClickDeleteListener(OnClickDeleteListener onClickDeleteListener) {
        this.onClickDeleteListener = onClickDeleteListener;
    }

    interface OnClickDeleteListener {
        void onClickDelete(View v);
    }
}
