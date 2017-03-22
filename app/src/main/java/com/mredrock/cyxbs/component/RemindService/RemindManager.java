package com.mredrock.cyxbs.component.RemindService;

import android.content.Context;
import android.content.Intent;

import com.mredrock.cyxbs.component.RemindService.Task.BaskRemindTask;
import com.mredrock.cyxbs.component.RemindService.service.NotificationService;

import java.util.ArrayList;

/**
 * Created by simonla on 2017/3/22.
 * 上午10:08
 */

public class RemindManager {

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
     *
     * @param baskRemindTask 任务，用来获取Reminder
     */
    public void push(BaskRemindTask baskRemindTask) {
        baskRemindTask.task(reminders -> {
            if (baskRemindTask.isTurnOn()) {
                push(reminders,baskRemindTask.mContext);
            } else {
                cancel(reminders, baskRemindTask.mContext);
            }
        });
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
}
