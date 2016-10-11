package com.mredrock.cyxbs.ui.fragment.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.mredrock.cyxbs.R;

/**
 * Created by simonla on 2016/10/11.
 * 下午4:39
 */

public class RemindFragment extends PreferenceFragment {

    public static final String TAG = "RemindFragment";

    public static final String SP_REMIND_EVERY_CLASS = "remind_every_class";
    public static final String SP_REMIND_EVERY_CLASS_DELAY = "remind_every_class_delay";
    public static final String SP_REMIND_EVERY_DAY = "remind_every_day";
    public static final String SP_REMIND_EVERY_DAY_TIME = "remind_every_day_time";

    private Preference mSwitchEveryClass;
    private Preference mChooseDelayList;
    private Preference mSwitchEveryDay;
    private Preference mChooseTime;

    private SharedPreferences mSp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.remind_preferences);
        initPreference();
        initSetting();
        boolean a = mSp.getBoolean("remind_every_class", false);
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
}
