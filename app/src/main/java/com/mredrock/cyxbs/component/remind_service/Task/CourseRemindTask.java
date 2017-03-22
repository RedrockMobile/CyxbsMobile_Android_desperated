package com.mredrock.cyxbs.component.remind_service.Task;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mredrock.cyxbs.component.remind_service.func.CourseRemindConvertFunc;
import com.mredrock.cyxbs.component.remind_service.Reminder;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;

import java.util.ArrayList;
import java.util.List;

import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_CLASS_DELAY;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_DAY;

/**
 * Created by simonla on 2017/3/22.
 * 下午12:06
 */

public class CourseRemindTask extends BaskRemindTask {

    private SharedPreferences mSp;

    public CourseRemindTask(Context context) {
        super(context);
        mSp = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public boolean isTurnOn() {
        return mSp.getBoolean(SP_REMIND_EVERY_DAY, false);
    }

    @Override
    public void task(Callback callback) {
        int delay = Integer.valueOf(mSp.getString(SP_REMIND_EVERY_CLASS_DELAY, "0"));
        RequestManager.getInstance().getRemindableList(new SimpleSubscriber<>(mContext,
                new SubscriberListener<List<Reminder>>() {

                    @Override
                    public boolean onError(Throwable e) {
                        e.printStackTrace();
                        return true;
                    }

                    @Override
                    public void onNext(List<Reminder> reminders) {
                        super.onNext(reminders);
                        callback.done((ArrayList<Reminder>) reminders);
                    }
                }), mContext, new CourseRemindConvertFunc
                (delay));
    }

}
