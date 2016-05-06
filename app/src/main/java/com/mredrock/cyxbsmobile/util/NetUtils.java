package com.mredrock.cyxbsmobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by skylineTan on 2016/4/13 15:53.
 */
public class NetUtils {

    /**
     * 是否有网络
     *
     * @param ctx
     * @return
     */
    public static boolean isNetWorkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return !(activeNetInfo == null || !activeNetInfo.isAvailable());
    }
}
