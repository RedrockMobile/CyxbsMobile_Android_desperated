package com.mredrock.cyxbsmobile.component.widget;

/**
 * Created by mathiasluo on 16-4-11.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.util.DensityUtil;

/**
 * Created by Pan_ on 2015/2/2.
 */
public class CustomImageView extends ImageView {


    private String url;
    private boolean isAttachedToWindow;
    private OnClickDelecteLIstener mOnClickDelecteLIstener;
    private int type;


    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context) {
        super(context);
    }


    public int getType() {
        return type;
    }

    public ImageView setType(int type) {
        this.type = type;
        return this;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(Color.GRAY,
                            PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
                    drawableUp.mutate().clearColorFilter();
                }
                int x = (int) event.getX();
                int y = (int) event.getY();
                int w = DensityUtil.dp2px(getContext(), 18);

                if (x > getWidth() - 2 * w && y < 2 * w) {
                    if (mOnClickDelecteLIstener != null && type == Image.NORMALIMAGE)
                        mOnClickDelecteLIstener.onClickDelect(this);
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
                Glide
                        .with(getContext())
                        .load(url)
                        .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5")))
                        .into(this);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (type == Image.NORMALIMAGE) {
            int w = canvas.getWidth();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete_black_18dp);
            canvas.drawBitmap(bitmap, w - DensityUtil.dp2px(getContext(), 18), 0, null);
        }
    }

    public void setOnClickDelecteLIstener(OnClickDelecteLIstener onClickDelecteLIstener) {
        this.mOnClickDelecteLIstener = onClickDelecteLIstener;
    }

    public interface OnClickDelecteLIstener {
        void onClickDelect(View v);
    }
}
