package com.mredrock.cyxbs.component.remind_service.func;

import com.mredrock.cyxbs.component.remind_service.Reminder;
import com.mredrock.cyxbs.model.Course;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;


/**
 * Created by simonla on 2017/3/21.
 * 下午9:58
 */

public abstract class BaseRemindFunc implements Function<List<Course>, List<Reminder>> {

    int mDelay;
    List<Reminder> mReminders = new ArrayList<>();

    BaseRemindFunc(int delay) {
        mDelay = delay;
    }

    @Override
    abstract public List<Reminder> apply(List<Course> courses) throws Exception;
}
