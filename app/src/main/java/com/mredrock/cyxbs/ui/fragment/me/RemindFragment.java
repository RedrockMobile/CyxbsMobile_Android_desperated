package com.mredrock.cyxbs.ui.fragment.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.mredrock.cyxbs.component.remind_service.RemindManager;
import com.mredrock.cyxbs.component.remind_service.Task.CourseRemindTask;

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
        if (key.equals(SP_REMIND_EVERY_CLASS) || key.equals(SP_REMIND_EVERY_CLASS_DELAY)) {
            RemindManager.getInstance().push(new CourseRemindTask(this.getActivity()));
        }
        if (key.equals(SP_REMIND_EVERY_DAY) || key.equals(SP_REMIND_EVERY_DAY_TIME)) {
            RemindManager.getInstance().push(new CourseRemindTask(this.getActivity()));
        }
    }
}
