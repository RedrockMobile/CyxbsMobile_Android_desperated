package com.mredrock.cyxbs.component.RemindService.Func;

import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.component.RemindService.Reminder;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by simonla on 2017/3/21.
 * 下午9:58
 */

public abstract class BaseRemindFunc implements Func1<List<Course>, List<Reminder>> {

    int mDelay;
    List<Reminder> mReminders = new ArrayList<>();

    BaseRemindFunc(int delay) {
        mDelay = delay;
    }

    @Override
    abstract public List<Reminder> call(List<Course> courses);
}
