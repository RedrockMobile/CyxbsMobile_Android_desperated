package com.mredrock.cyxbs.component.remind_service.Func;

import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.component.remind_service.Reminder;

import java.util.Calendar;
import java.util.List;

/**
 * Created by simonla on 2017/3/21.
 * 下午10:11
 */

public class NextDayCourseRemindConvertFunc extends BaseRemindFunc {

    public NextDayCourseRemindConvertFunc(int delay) {
        super(delay);
    }

    @Override
    public List<Reminder> call(List<Course> courses) {
        for (Course c : courses) {
            if (isTomorrowHasClass(c.hash_day) && !isLate()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, mDelay);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND,0);
                Reminder reminder = new Reminder();
                reminder.setCalendar(calendar);
                reminder.setTitle("小邮提醒您：");
                if (c.hash_lesson == 0) {
                    reminder.setSubTitle("明天12节有课，可不要睡过了哟");
                } else if (c.hash_lesson == 1) {
                    reminder.setSubTitle("明天34节有课，记得吃早饭~");
                } else {
                    reminder.setSubTitle("明天早上没课，可还是要早睡早起: )");
                }
                mReminders.add(reminder);
                break;
            }
        }
        return mReminders;
    }

    private boolean isTomorrowHasClass(int hashDay) {
        hashDay = hashDay + 2;
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1;
        return hashDay == dayOfWeek % 7;
    }

    private boolean isLate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.HOUR_OF_DAY) >= mDelay;
    }
}
