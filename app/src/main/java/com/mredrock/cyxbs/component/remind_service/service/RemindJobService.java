package com.mredrock.cyxbs.component.remind_service.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.mredrock.cyxbs.component.remind_service.RemindManager;

/**
 * Created by simonla on 2017/3/22.
 * 下午3:49
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RemindJobService extends JobService {

    public static final String TAG = RemindJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters params) {
        RemindManager.getInstance().pushAll(getApplicationContext());
        Log.d(TAG, "onStartJob: 正在唤起主服务");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
