package com.mredrock.cyxbs.freshman.utils;

import com.mredrock.cyxbs.freshman.utils.kt.SpKt;

import io.reactivex.annotations.Nullable;

/**
 * Created by Hosigus
 */
public class SPHelper {

    @Nullable
    public static <T> T getBean(String spName, String keyName, Class<T> clazz) {
        return SpKt.getBeanFromSP(keyName, clazz, spName);
    }

    public static <T> void putBean(String spName, String keyName, T bean) {
        SpKt.putBeanToSP(keyName, bean, spName);
    }

    public static <T> void putBean(String keyName, T bean) {
        SpKt.putBeanToSP(keyName, bean);
    }

}
