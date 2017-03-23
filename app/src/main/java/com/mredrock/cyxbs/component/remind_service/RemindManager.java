package com.mredrock.cyxbs.component.remind_service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.mredrock.cyxbs.component.remind_service.Task.BaskRemindTask;
import com.mredrock.cyxbs.component.remind_service.Task.CourseRemindTask;
import com.mredrock.cyxbs.component.remind_service.Task.DayRemindTask;
import com.mredrock.cyxbs.component.remind_service.receiver.RebootReceiver;
import com.mredrock.cyxbs.component.remind_service.service.RemindJobService;
import com.mredrock.cyxbs.component.remind_service.service.NotificationService;

import java.util.ArrayList;

import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.ALARM_FLAG_REBOOT_CODE;

/**
 * Created by simonla on 2017/3/22.
 * 上午10:08
 */

public class RemindManager {

    public static final String TAG = RemindManager.class.getSimpleName();
    public static final String EXTRA_REMINDABLE = "remindable";
    public static final int INTENT_FLAG_PUSH = 0;
    public static final int INTENT_FLAG_CANCEL = 1;
    public static final String INTENT_FLAG = "remind_intent_flag";
    private volatile static RemindManager INSTANCE;

    private RemindManager() {
    }

    public static RemindManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RemindManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemindManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * @param baskRemindTask 任务，用来获取Reminder
     */
    public void push(BaskRemindTask baskRemindTask) {
        Context context = baskRemindTask.mContext;
        startRebootAuto(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            startDemon(context);
        }
        baskRemindTask.task(reminders -> {
            if (baskRemindTask.isTurnOn()) {
                push(reminders, context);
            } else {
                cancel(reminders, context);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startDemon(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1,
                new ComponentName(context.getPackageName(),
                        RemindJobService.class.getName()));
        builder.setPeriodic(1000 * 60 * 30).setRequiresCharging(true).setPersisted(true).setRequiresDeviceIdle(true);
        jobScheduler.schedule(builder.build());
    }

    private void push(ArrayList<Reminder> reminders, Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putParcelableArrayListExtra(EXTRA_REMINDABLE, reminders)
                .putExtra(INTENT_FLAG, INTENT_FLAG_PUSH);
        context.startService(intent);
    }

    private void cancel(ArrayList<Reminder> reminders, Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putParcelableArrayListExtra(EXTRA_REMINDABLE, reminders)
                .putExtra(INTENT_FLAG, INTENT_FLAG_CANCEL);
        context.startService(intent);
    }

    private void startRebootAuto(Context context) {
        ComponentName receiver = new ComponentName(context, RebootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Intent intent = new Intent(context, RebootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                ALARM_FLAG_REBOOT_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 60 * 1000
                , 60 * 1000 * 30, pendingIntent);
    }

    //需要重复执行的任务放在这里
    public void pushAll(Context context) {
        push(new CourseRemindTask(context));
        push(new DayRemindTask(context));
    }
}
