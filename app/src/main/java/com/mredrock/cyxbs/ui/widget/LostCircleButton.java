package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by wusui on 2017/3/4.
 */

public class LostCircleButton extends Button {
    private GradientDrawable gradientDrawable;
    //背景色，分为String和int类型
    private String backColors = "";
    private int backColori = 0;
    //按下后的背景色，也分为String和int类型
    private String backColorSelecteds = "";
    private int backColorSelectedi = 0;
    //背景图，只提供id
    private int backGroundImage = 0;
    private int backGroundImageSelected = 0;
    //文字的颜色,分为String和int类型
    private String textColors = "";
    private int textColori = 0;

    private float radius = 8;
    private int shape = 0;
    private Boolean fillet = false;

    public LostCircleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LostCircleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LostCircleButton(Context context) {
        this(context, null);
    }

    private void init() {
        if (fillet) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
            }
            gradientDrawable.setColor(Color.TRANSPARENT);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
        setGravity(Gravity.CENTER);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setColor(motionEvent.getAction());
                return false;
            }
        });
    }

    private void setColor(int state) {
        if (state == MotionEvent.ACTION_DOWN) {
            if (backColorSelectedi != 0) {

                if (fillet) {
                    if (gradientDrawable == null) {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(backColorSelectedi);
                } else {
                    setBackgroundColor(backColorSelectedi);
                }
            } else if (!backColorSelecteds.equals("")) {
                if (fillet) {
                    if (gradientDrawable == null) {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(Color.parseColor(backColorSelecteds));
                } else {
                    setBackgroundColor(Color.parseColor(backColorSelecteds));
                }
            }
            if (backGroundImageSelected != 0) {
                setBackgroundResource(backGroundImageSelected);
            }
        }
        /*if (state == MotionEvent.ACTION_UP){
            if (backColori == 0 && backColors.equals("")){
                if (fillet){
                    if (gradientDrawable == null){
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(Color.TRANSPARENT);
                }else {
                    setBackgroundColor(Color.TRANSPARENT);
                }
            }else if (backColori != 0){
                if (fillet){
                    if (gradientDrawable == null){
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(backColori);
                }else {
                    setBackgroundColor(backColori);
                }
            }else {
                if (fillet){
                    if (gradientDrawable == null){
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(Color.parseColor(backColors));
                }else {
                    setBackgroundColor(Color.parseColor(backColors));
                }
            }
            if (textColori == 0 && textColors.equals("")){
                setTextColor(Color.BLACK);
            }else if (textColori != 0){
                setTextColor(textColori);
            }else {
                setTextColor(Color.parseColor(textColors));
            }
            if (backGroundImage != 0){
                setBackgroundResource(backGroundImage);
            }
        }*/
    }

    /**
     * 设置按钮的背景色,如果未设置则默认为透明
     *
     * @param backColor
     */
    public void setBackColor(String backColor) {
        this.backColors = backColor;
        if (backColor.equals("")) {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(Color.TRANSPARENT);
            } else {
                setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(Color.parseColor(backColor));
            } else {
                setBackgroundColor(Color.parseColor(backColor));
            }
        }
    }

    /**
     * 设置按钮的背景色,如果未设置则默认为透明
     *
     * @param backColor
     */
    public void setBackColor(int backColor) {
        this.backColori = backColor;
        if (backColori == 0) {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(Color.TRANSPARENT);
            } else {
                setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            if (fillet) {
                if (gradientDrawable == null) {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(backColor);
            } else {
                setBackgroundColor(backColor);
            }
        }
    }

    /**
     * 设置按钮按下后的颜色
     *
     * @param backColorSelected
     */
    public void setBackColorSelected(int backColorSelected) {
        this.backColorSelectedi = backColorSelected;
    }

    /**
     * 设置按钮按下后的颜色
     *
     * @param backColorSelected
     */
    public void setBackColorSelected(String backColorSelected) {
        this.backColorSelecteds = backColorSelected;
    }

    /**
     * 设置按钮的背景图
     *
     * @param backGroundImage
     */
    public void setBackGroundImage(int backGroundImage) {
        this.backGroundImage = backGroundImage;
        if (backGroundImage != 0) {
            setBackgroundResource(backGroundImage);
        }
    }

    /**
     * 设置按钮按下的背景图
     *
     * @param backGroundImageSeleted
     */
    public void setBackGroundImageSeleted(int backGroundImageSeleted) {
        this.backGroundImageSelected = backGroundImageSeleted;
    }

    /**
     * 设置按钮圆角半径大小
     *
     * @param radius
     */
    public void setRadius(float radius) {
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable();
        }
        gradientDrawable.setCornerRadius(radius);
    }

    /**
     * 设置按钮文字颜色
     *
     * @param textColor
     */
    public void setTextColors(String textColor) {
        this.textColors = textColor;
        setTextColor(Color.parseColor(textColor));
    }

    /**
     * 设置按钮文字颜色
     *
     * @param textColor
     */
    public void setTextColori(int textColor) {
        this.textColori = textColor;
        setTextColor(textColor);
    }

    /**
     * 按钮的形状
     *
     * @param shape
     */
    public void setShape(int shape) {
        this.shape = shape;
    }

    /**
     * 设置其是否为圆角
     *
     * @param fillet
     */
    @SuppressWarnings("deprecation")
    public void setFillet(Boolean fillet) {
        this.fillet = fillet;
        if (fillet) {
            if (gradientDrawable == null) {
                gradientDrawable = new GradientDrawable();
            }
            //GradientDrawable.RECTANGLE
            gradientDrawable.setShape(shape);
            gradientDrawable.setCornerRadius(radius);
            setBackgroundDrawable(gradientDrawable);
        }
    }

}
