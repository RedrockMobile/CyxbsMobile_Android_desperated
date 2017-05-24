package com.mredrock.cyxbs.component.widget.bottombar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

public class BottomButton extends RelativeLayout implements IBottomView {

    private ImageView mImageView;
    private TextView mTextView;
    // attributes
    private String mText;
    private float mTextSize;
    private int mTextNormalColor;
    private int mTextChooseColor;
    private Drawable mNormalImage;
    private Drawable mChooseImage;
    private int mImageNormalColor;
    private int mImageChooseColor;
    private float mTextImageMargin;

    public BottomButton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BottomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BottomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        initAttributes(context, attrs);
        initView(context);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomButton);
        mText = a.getString(R.styleable.BottomButton_button_text);
        mTextSize = a.getDimension(R.styleable.BottomButton_button_text_size, 10);
        int normalColor = a.getColor(R.styleable.BottomButton_color_normal,
                getColor(context, R.attr.colorPrimary));
        int chooseColor = a.getColor(R.styleable.BottomButton_color_choose,
                getColor(context, R.attr.colorPrimary));
        mTextNormalColor = a.getColor(R.styleable.BottomButton_button_text_color_normal,
                normalColor);
        mTextChooseColor = a.getColor(R.styleable.BottomButton_button_text_color_choose,
                chooseColor);
        mImageNormalColor = a.getColor(R.styleable.BottomButton_button_image_color_normal,
                normalColor);
        mImageChooseColor = a.getColor(R.styleable.BottomButton_button_image_color_choose,
                chooseColor);
        mNormalImage = a.getDrawable(R.styleable.BottomButton_button_image_normal);
        mChooseImage = a.getDrawable(R.styleable.BottomButton_button_image_choose);
        mTextImageMargin = a.getDimension(R.styleable.BottomButton_button_text_image_margin, 2);
        a.recycle();
    }

    private void initView(Context context) {
        mImageView = new ImageView(context);
        mTextView = new TextView(context);

        if (mNormalImage != null) {
            mImageView.setImageDrawable(mNormalImage);
        }
        mImageView.setColorFilter(mImageNormalColor, PorterDuff.Mode.SRC_IN);
        mImageView.setId(R.id.bottom_button_image);
        mTextView.setText(mText);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        mTextView.setTextColor(mTextNormalColor);

        LayoutParams imageParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
        addView(mImageView, imageParams);

        LayoutParams textParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.topMargin = (int) mTextImageMargin;
        textParams.addRule(CENTER_HORIZONTAL, TRUE);
        textParams.addRule(RelativeLayout.BELOW, mImageView.getId());
        addView(mTextView, textParams);
    }

    @Override
    public void onChoose() {
        mTextView.setTextColor(mTextChooseColor);
        mTextView.setVisibility(VISIBLE);
        mImageView.setColorFilter(mImageChooseColor, PorterDuff.Mode.SRC_IN);
        if (mChooseImage != null && mNormalImage != null) {
            mImageView.setImageDrawable(mChooseImage);
        }
    }

    @Override
    public void onNormal() {
        mTextView.setVisibility(GONE);
        mTextView.setTextColor(mTextNormalColor);
        mImageView.setColorFilter(mImageNormalColor, PorterDuff.Mode.SRC_IN);
        if (mChooseImage != null) {
            mImageView.setImageDrawable(mNormalImage);
        }
    }

    private int getColor(Context context, int color) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(color, tv, true);
        return tv.data;
    }
}
