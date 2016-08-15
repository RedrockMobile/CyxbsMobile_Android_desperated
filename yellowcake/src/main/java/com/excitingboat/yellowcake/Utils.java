package com.excitingboat.yellowcake;

import android.content.Context;
import android.graphics.Color;

import java.text.DecimalFormat;

/**
 * Created by PinkD on 2016/8/5.
 * px/dp/sp Utils
 */
public class Utils {

    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static String formatNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(number);
    }


    /**
     * Function 自动改变颜色
     *
     * @param color input color
     * @return changed color
     */
    public static int getDarkColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red *= (float) red / 0xFF * 0.8;
        green *= (float) green / 0xFF * 0.8;
        blue *= (float) blue / 0xFF * 0.8;
        color = Color.rgb(red, green, blue);
        return color;
    }


}
