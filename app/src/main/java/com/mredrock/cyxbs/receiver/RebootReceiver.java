package com.mredrock.cyxbs.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.fragment.me.RemindFragment;
import com.mredrock.cyxbs.util.CourseTimeUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.TimeUtils;

import java.util.Calendar;
import java.util.List;

import static android.R.attr.mode;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.INTENT_FLAG_BY_DAY;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.INTENT_MODE;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_CLASS;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_CLASS_DELAY;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_DAY;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_DAY_TIME;

/**
 * Created by simonla on 2016/10/24.
 * 下午4:38
 */

public class RebootReceiver extends BroadcastReceiver {

    private static final String TAG = "RebootReceiver";
    private SharedPreferences mSp;
    private AlarmManager mAlarmManager;
    public static final String EXTRA_COURSE_CLASSROOM = "course_classroom";
    public static final String EXTRA_COURSE_NAME = "course_name";

    @Override
    public void onReceive(Context context, Intent intent) {
        mSp = PreferenceManager.getDefaultSharedPreferences(context);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        reRegister(context, intent);
        int mode = intent.getIntExtra(INTENT_MODE, 3);

        if (mode == RemindFragment.INTENT_FLAG_BY_CLASS && mSp.getBoolean(SP_REMIND_EVERY_CLASS, false)) {
            byClass(context, mode);
            Log.d(TAG, "onReceive: Receive by class! ");
        }
        if (mode == INTENT_FLAG_BY_DAY && mSp.getBoolean(SP_REMIND_EVERY_DAY, false)) {
            byDay(context, mode);
            Log.d(TAG, "onReceive: Receive by day! ");
        }
    }

    //反复注册，反复启动
    private void reRegister(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                Log.d(TAG, "reRegister: 开机自启");
                Intent intent2 = new Intent(context, RebootReceiver.class);
                intent2.putExtra(INTENT_MODE, mode);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 51, intent, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 7);
                calendar.set(Calendar.MINUTE, 0);
                if (mSp.getBoolean(SP_REMIND_EVERY_DAY, false) || mSp.getBoolean(SP_REMIND_EVERY_CLASS, false)) {
                    mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                            , AlarmManager.INTERVAL_DAY, pendingIntent);
                    //似乎还应该立即生效一次
                    Intent intent3 = new Intent(context, RebootReceiver.class);
                    PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 52, intent3, 0);
                    mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() +
                                    10 * 500, pendingIntent3);
                } else {
                    mAlarmManager.cancel(pendingIntent);
                    //取消开机自启
                    ComponentName receiver2 = new ComponentName(context, RebootReceiver.class);
                    PackageManager pm2 = context.getPackageManager();
                    pm2.setComponentEnabledSetting(receiver2,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }
            }
        }
    }

    private void byClass(Context context, int mode) {
        getCourseList(new CourseCallback() {
            @Override
            public void onSuccess(List<Course> courses) {
                for (Course c : courses) {
                    Calendar today = Calendar.getInstance();
                    today.setTimeInMillis(System.currentTimeMillis());
                    Calendar courseCalendar = CourseTimeUtils.CourseToCalendar(c);
                    //如果不是今天的课就退出
                    if (courseCalendar.get(Calendar.DAY_OF_WEEK) != today.get(Calendar.DAY_OF_WEEK)) {
                        continue;
                    }
                    int hourDelay = courseCalendar.get(Calendar.MINUTE) - Integer.valueOf(mSp.
                            getString(SP_REMIND_EVERY_CLASS_DELAY, "0")) < 0 ? 1 : 0;
                    courseCalendar.set(Calendar.HOUR_OF_DAY, courseCalendar.get(Calendar.HOUR_OF_DAY)
                            - hourDelay);
                    if (hourDelay != 0) {
                        courseCalendar.set(Calendar.MINUTE, 60 + courseCalendar.get(Calendar.MINUTE) -
                                Integer.valueOf(mSp.
                                getString(SP_REMIND_EVERY_CLASS_DELAY, "0")));
                    }

                    Log.d(TAG, "byClass: 下一节课时间是：" + courseCalendar.get(Calendar.HOUR_OF_DAY) +
                            " : " + courseCalendar.get(Calendar.MINUTE) + "课程名:" +
                            c.course + "教室：" + c.classroom);

                    Log.d(TAG, "onSuccess: name:" + c.course + " time: " +
                            TimeUtils.timeStampToStr(courseCalendar.getTimeInMillis()/1000));
                    Intent intent = new Intent(context, RemindReceiver.class);
                    intent.putExtra(INTENT_MODE, mode);
                    intent.putExtra(EXTRA_COURSE_NAME, c.course);
                    intent.putExtra(EXTRA_COURSE_CLASSROOM, c.classroom);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, c.hash_lesson +
                            10, intent, 0);
                    if (mSp.getBoolean(SP_REMIND_EVERY_CLASS, false)) {
                        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, courseCalendar.getTimeInMillis(),
                                pendingIntent);
                    } else {
                        mAlarmManager.cancel(pendingIntent);
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                Log.d(TAG, "onFail: " + e);
            }
        }, context);
    }

    private void byDay(Context context, int mode) {
        Intent intent = new Intent(context, RemindReceiver.class);
        intent.putExtra(INTENT_MODE, mode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 53, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(mSp.getString(SP_REMIND_EVERY_DAY_TIME, "22")));
        calendar.set(Calendar.MINUTE, 0);
        if (mSp.getBoolean(SP_REMIND_EVERY_DAY, false)) {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            mAlarmManager.cancel(pendingIntent);
        }
    }

    private void getCourseList(CourseCallback courseCallback, Context context) {
        RequestManager.getInstance().getCourseList(new SimpleSubscriber<>(context, false
                        , false, new SubscriberListener<List<Course>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        courseCallback.onFail(e);
                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        super.onNext(courses);
                        courseCallback.onSuccess(courses);
                    }
                }),
                APP.getUser(context).stuNum, APP.getUser(context).idNum, new SchoolCalendar()
                        .getWeekOfTerm(), false);
    }

    private interface CourseCallback {
        void onSuccess(List<Course> courses);

        void onFail(Throwable e);
    }
}
