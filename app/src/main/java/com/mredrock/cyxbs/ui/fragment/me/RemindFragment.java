package com.mredrock.cyxbs.ui.fragment.me;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.receiver.RemindReceiver;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.Calendar;
import java.util.List;

/**
 * Created by simonla on 2016/10/11.
 * 下午4:39
 */

public class RemindFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "RemindFragment";

    public static final String SP_REMIND_EVERY_CLASS = "remind_every_class";
    public static final String SP_REMIND_EVERY_CLASS_DELAY = "remind_every_class_delay";
    public static final String SP_REMIND_EVERY_DAY = "remind_every_day";
    public static final String SP_REMIND_EVERY_DAY_TIME = "remind_every_day_time";

    public static final int INTENT_FLAG_BY_CLASS = 0;
    public static final int INTENT_FLAG_BY_DAY = 1;

    public static final String INTENT_MODE = "remind_fragment_intent_mode";

    private Preference mSwitchEveryClass;
    private Preference mChooseDelayList;
    private Preference mSwitchEveryDay;
    private Preference mChooseTime;
    private SharedPreferences mSp;

    private AlarmManager mAlarmManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(com.mredrock.cyxbs.R.xml.remind_preferences);
        initPreference();
        initSetting();
    }

    private void initPreference() {
        mSp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mChooseDelayList = getPreferenceManager().findPreference(SP_REMIND_EVERY_CLASS_DELAY);
        mChooseTime = getPreferenceManager().findPreference(SP_REMIND_EVERY_DAY_TIME);
        mSwitchEveryClass = getPreferenceManager().findPreference(SP_REMIND_EVERY_CLASS);
        mSwitchEveryDay = getPreferenceManager().findPreference(SP_REMIND_EVERY_DAY);
    }

    private void initSetting() {
        boolean isEveryClass = mSp.getBoolean(SP_REMIND_EVERY_CLASS, false);
        boolean isEveryDay = mSp.getBoolean(SP_REMIND_EVERY_DAY, false);
        initChooseTime(isEveryDay);
        initChooseDelay(isEveryClass);

        mSwitchEveryDay.setOnPreferenceChangeListener((preference, newValue) -> {
            initChooseTime((Boolean) newValue);
            return true;
        });

        mSwitchEveryClass.setOnPreferenceChangeListener((preference, newValue) -> {
            initChooseDelay((Boolean) newValue);
            return true;
        });
    }

    private void initChooseDelay(boolean isEveryClass) {
        if (isEveryClass) {
            mChooseDelayList.setEnabled(true);
        } else {
            mChooseDelayList.setEnabled(false);
        }
    }

    private void initChooseTime(boolean isEveryDay) {
        if (isEveryDay) {
            mChooseTime.setEnabled(true);
        } else {
            mChooseTime.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSp.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (key.equals(SP_REMIND_EVERY_CLASS)) {
            remindByClass();
        }
        if (key.equals(SP_REMIND_EVERY_DAY)) {
            remindByDay();
        }
    }

    private void remindByDay() {
        Intent intent = new Intent(getActivity(), RemindReceiver.class);
        intent.putExtra(INTENT_MODE, INTENT_FLAG_BY_DAY);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(mSp.getString(SP_REMIND_EVERY_DAY_TIME, "22")));
        calendar.set(Calendar.MINUTE, 0);

        if (mSp.getBoolean(SP_REMIND_EVERY_DAY, false)) {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
                    , AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d(TAG, "remindByDay: push successful...");
        } else {
            Log.d(TAG, "remindByDay: push cancel...");
            mAlarmManager.cancel(pendingIntent);
        }

/*        //just for test...
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), RemindReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        10 * 1000, pendingIntent);*/
    }

    private void remindByClass() {
        Intent intent = new Intent(getActivity(), RemindReceiver.class);
        intent.putExtra(INTENT_MODE, INTENT_FLAG_BY_CLASS);
        getCourseList(new CourseCallback() {
            @Override
            public void onSuccess(List<Course> courses) {

            }

            @Override
            public void onFail(Throwable e) {
                Toast.makeText(getActivity(), "发生错误：" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCourseList(CourseCallback courseCallback) {
        RequestManager.getInstance().getCourseList(new SimpleSubscriber<List<Course>>(getActivity(), false, false, new SubscriberListener<List<Course>>() {
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
                APP.getUser(getActivity()).stuNum, APP.getUser(getActivity()).idNum, new SchoolCalendar().getWeekOfTerm(), false);
    }

    private interface CourseCallback {
        void onSuccess(List<Course> courses);

        void onFail(Throwable e);
    }
}
