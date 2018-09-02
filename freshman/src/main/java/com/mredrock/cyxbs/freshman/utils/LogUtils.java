package com.mredrock.cyxbs.freshman.utils;

import com.mredrock.cyxbs.freshman.utils.kt.Level;
import com.mredrock.cyxbs.freshman.utils.kt.LogBuilder;

/**
 * 全局Log工具类
 * 你可以直接调用静态方法进行日志输出。
 * 但是我还是更建议你为单独的模块设置单独的LogBuilder，
 * 因为这样方便管理模块间的Log输出等级。
 *
 * @see LogBuilder
 */
public class LogUtils {

    private static LogBuilder builder = new LogBuilder("Global log", Level.ALL);

    public static void setLevel(Level level) {
        builder.setLevel(level);
    }

    public static void v(String msg) {
        builder.v(msg);
    }

    public static void d(String msg) {
        builder.d(msg);
    }

    public static void i(String msg) {
        builder.i(msg);
    }

    public static void w(String msg) {
        builder.w(msg);
    }

    public static void e(String msg) {
        builder.e(msg);
    }

}
