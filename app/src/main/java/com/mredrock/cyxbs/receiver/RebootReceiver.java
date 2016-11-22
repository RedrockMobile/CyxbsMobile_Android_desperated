package com.mredrock.cyxbs.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.mredrock.cyxbs.service.NotificationService;

/**
 * Created by simonla on 2016/10/24.
 * 下午4:38
 */

public class RebootReceiver extends BroadcastReceiver {

    private static final String TAG = "RebootReceiver";
    private SharedPreferences mSp;
    private AlarmManager mAlarmManager;
    public static final String EXTRA_NOTIFY_SUBTITLE = "course_classroom";
    public static final String EXTRA_NOTIFY_TITLE = "course_name";

    @Override
    public void onReceive(Context context, Intent intent) {
        //启动服务
        Intent remindService = new Intent(context, NotificationService.class);
        context.startService(remindService);
        Log.d(TAG, "onReceive: 启动服务");
    }
}
