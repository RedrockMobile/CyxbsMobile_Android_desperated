package com.mredrock.cyxbs.component.remind_service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mredrock.cyxbs.component.remind_service.RemindManager;
import com.mredrock.cyxbs.component.remind_service.Task.CourseRemindTask;

/**
 * Created by simonla on 2016/10/24.
 * 下午4:38
 */

public class RebootReceiver extends BroadcastReceiver {

    private static final String TAG = "RebootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        RemindManager.getInstance().push(new CourseRemindTask(context));
        RemindManager.getInstance().push(new CourseRemindTask(context));
        Log.d(TAG, "onReceive: 开机或者半小时启动一次服务");
    }
}
