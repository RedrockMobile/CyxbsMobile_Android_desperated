package com.mredrock.cyxbs.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.MainActivity;
import com.mredrock.cyxbs.ui.fragment.me.RemindFragment;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.Calendar;
import java.util.List;

import static android.R.attr.mode;

/**
 * Created by simonla on 2016/10/10.
 * 下午11:06
 */

public class RemindReceiver extends BroadcastReceiver {

    public static final String TAG = "RemindReceiver";
    private int mMode;

    private List<Course> mCourses;

    @Override
    public void onReceive(Context context, Intent intent) {
        mMode = intent.getIntExtra(RemindFragment.INTENT_MODE, RemindFragment.INTENT_FLAG_BY_DAY);
        Log.d(TAG, "onReceive: receive mode: " + mode);
        getCourseList(context);
    }

    private boolean tomorrowHaveCourse() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1;
        if (mCourses == null) {
            Log.d(TAG, "tomorrowHaveCourse: mCurses null!!!");
            return false;
        } else {
            for (Course c : mCourses) {
                Log.d(TAG, "tomorrowHaveCourse: hash_day:" + c.hash_day);
                Log.d(TAG, "tomorrowHaveCourse: " + dayOfWeek);
                if (c.hash_day + 2 == dayOfWeek % 7) {
                    return true;
                }
            }
            return false;
        }
    }

    private void showNotification(Context context) {
        Intent openMain = new Intent(context, MainActivity.class);
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentTitle("小邮提醒您，这是明天的课表")
                    .setContentText("点击查看");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(openMain);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openMain,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openMain,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("小邮提醒您，这是明天的课表")
                    .setContentText("点击查看");
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private void getCourseList(Context context) {
        RequestManager.getInstance().getCourseList(new SimpleSubscriber<List<Course>>(context,
                false, false, new SubscriberListener<List<Course>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        if (mMode == RemindFragment.INTENT_FLAG_BY_CLASS) {
                            Log.d(TAG, "onCompleted mode：byClass");
                            byClass(context);
                        }
                        if (mMode == RemindFragment.INTENT_FLAG_BY_DAY) {
                            Log.d(TAG, "onCompleted mode: byDay");
                            byDay(context);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        super.onNext(courses);
                        mCourses = courses;
                        Log.d(TAG, "onNext list siz: " + mCourses.size());
                    }
                }),
                APP.getUser(context).stuNum, APP.getUser(context).idNum, new SchoolCalendar()
                        .getWeekOfTerm(), false);
    }

    private void byDay(Context context) {
        if (tomorrowHaveCourse()) {
            showNotification(context);
        }
    }

    private void byClass(Context context) {

    }
}
