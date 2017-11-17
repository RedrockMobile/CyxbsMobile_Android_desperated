package com.mredrock.cyxbs.network;

import android.content.Context;
import android.widget.Toast;

import com.mredrock.cyxbs.BuildConfig;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Created by mingyang.zeng on 2017/8/30.
 */

public class ErrorHandler {
    private Context context;

    public ErrorHandler(Context context) {
        this.context = context;
    }

    public <T> void handle(Throwable e, SubscriberListener<T> listener) {
        if (listener != null && listener.onError(e)) {
            LogUtils.LOGI("SimpleSubscribe", "onError: Handled by listener", e);
        } else {
            if (e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof UnknownHostException) {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                }
            } else if (e.getMessage()!=null&&e.getMessage().equals("authentication error")) {
                Toast.makeText(context, "登录失败：学号或者密码错误,请检查输入", Toast.LENGTH_SHORT).show();
            } else if (e.getMessage()!=null&&e.getMessage().equals("student id error")) {
                Toast.makeText(context, "登录失败：学号不存在,请检查输入", Toast.LENGTH_SHORT).show();
            } else if (e instanceof HttpException) {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(context, "HttpException: " + ((HttpException) e).response().raw().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "此服务暂时不可用", Toast.LENGTH_SHORT).show();
                    //友盟错误统计
                    MobclickAgent.reportError(context, ((HttpException) e).response().raw().toString());
                }
                LogUtils.LOGE("HttpException", "RawResponse: " + ((HttpException) e).response().raw().toString());
            } else {
                if (e.getMessage()!=null&&BuildConfig.DEBUG) {
                    Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            LogUtils.LOGE("SimpleObserver", "onError", e);
        }
    }
}
