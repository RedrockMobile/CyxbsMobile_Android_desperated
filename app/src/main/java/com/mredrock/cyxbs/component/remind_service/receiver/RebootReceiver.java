package com.mredrock.cyxbs.component.remind_service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mredrock.cyxbs.component.remind_service.RemindManager;

/**
 * Created by simonla on 2016/10/24.
 * 下午4:38
 */

public class RebootReceiver extends BroadcastReceiver {

    private static final String TAG = "RebootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        RemindManager.getInstance().pushAll(context);
        Log.d(TAG, "onReceive: 每五分钟启动或者开机自启唤醒提醒线程");
    }
}
