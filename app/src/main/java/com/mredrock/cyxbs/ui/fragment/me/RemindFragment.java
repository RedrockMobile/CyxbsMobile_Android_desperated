package com.mredrock.cyxbs.ui.fragment.me;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mredrock.cyxbs.component.RemindService.Task.CourseRemindTask;
import com.mredrock.cyxbs.component.RemindService.receiver.RebootReceiver;
import com.mredrock.cyxbs.component.RemindService.RemindManager;
import com.mredrock.cyxbs.util.TimeUtils;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

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
    public static final String INTENT_HASH_LESSON = "hash_lesson";

    public static final int ALARM_FLAG_REBOOT_CODE = 0;
    public static final int ALARM_FLAG_BY_DAY = 2;

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
        mChooseDelayList.setSummary("提前" + mSp.getString(SP_REMIND_EVERY_CLASS_DELAY, "20") + "分钟");
        mChooseTime.setSummary(mSp.getString(SP_REMIND_EVERY_DAY_TIME, "10：00") + ":00");
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
        mChooseDelayList.setSummary("提前" + mSp.getString(SP_REMIND_EVERY_CLASS_DELAY, "20") + "分钟");
        mChooseTime.setSummary(mSp.getString(SP_REMIND_EVERY_DAY_TIME, "10：00") + ":00");
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        startRebootAuto();
        if (key.equals(SP_REMIND_EVERY_CLASS) || key.equals(SP_REMIND_EVERY_CLASS_DELAY)) {
            RemindManager.getInstance().push(new CourseRemindTask(this.getActivity()));
        }
        if (key.equals(SP_REMIND_EVERY_DAY) || key.equals(SP_REMIND_EVERY_DAY_TIME)) {
            RemindManager.getInstance().push(new CourseRemindTask(this.getActivity()));
        }
    }

    private void startRebootAuto() {
        //开机自启
        ComponentName receiver = new ComponentName(getActivity(), RebootReceiver.class);
        PackageManager pm = getActivity().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        //每半小时启动一次
        Intent intent = new Intent(getActivity(), RebootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                ALARM_FLAG_REBOOT_CODE, intent, FLAG_CANCEL_CURRENT);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 1000 * 5
                , AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
        Log.d(TAG, "startRebootAuto: 每半时启动一次" + TimeUtils.timeStampToStr(System.currentTimeMillis() / 1000 + 60 * 5));
    }

}
