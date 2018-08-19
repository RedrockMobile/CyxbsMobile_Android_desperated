package com.mredrock.cyxbs.freshman.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mredrock.cyxbs.freshman.utils.kt.SpKt;

import io.reactivex.annotations.Nullable;

public class SPHelper {

    public static SharedPreferences getSP(String name) {
        return name == null ? SpKt.getDefaultSp() : SpKt.sp(name);
    }

    public static SharedPreferences getSP(String name, Context context) {
        return name == null ? SpKt.getDefaultSp(context) : SpKt.sp(context, name);
    }

    @Nullable
    public static <T> T getBean(String spName, String keyName, Class<T> clazz) {
        return SpKt.getBeanFromSP(keyName, clazz, spName);
    }

    @Nullable
    public static <T> T getBean(String keyName, Class<T> clazz) {
        return SpKt.getBeanFromSP(keyName, clazz);
    }

    public static <T> void putBean(String spName, String keyName, T bean) {
        SpKt.putBeanToSP(keyName, bean, spName);
    }

    public static <T> void putBean(String keyName, T bean) {
        SpKt.putBeanToSP(keyName, bean);
    }

}
