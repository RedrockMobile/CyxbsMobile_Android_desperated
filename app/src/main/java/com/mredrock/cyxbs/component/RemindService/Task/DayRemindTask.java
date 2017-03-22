package com.mredrock.cyxbs.component.RemindService.Task;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mredrock.cyxbs.component.RemindService.Func.NextDayCourseRemindConvertFunc;
import com.mredrock.cyxbs.component.RemindService.Reminder;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;

import java.util.ArrayList;
import java.util.List;

import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_CLASS;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_DAY_TIME;

/**
 * Created by simonla on 2017/3/22.
 * 下午12:32
 */

public class DayRemindTask extends BaskRemindTask {

    private SharedPreferences mSp;

    public DayRemindTask(Context context) {
        super(context);
        mSp = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public boolean isTurnOn() {
        return mSp.getBoolean(SP_REMIND_EVERY_CLASS, false);
    }

    @Override
    public void task(Callback callback) {
        RequestManager.getInstance().getRemindableList(new SimpleSubscriber<>(mContext,
                new SubscriberListener<List<Reminder>>() {
                    @Override
                    public void onNext(List<Reminder> reminders) {
                        super.onNext(reminders);
                        callback.done((ArrayList<Reminder>) reminders);
                    }
                }), mContext, new NextDayCourseRemindConvertFunc(Integer.valueOf(mSp.
                getString(SP_REMIND_EVERY_DAY_TIME, "22"))));
    }
}
