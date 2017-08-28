package com.mredrock.cyxbs.component.remind_service.func;

import com.mredrock.cyxbs.component.remind_service.Reminder;
import com.mredrock.cyxbs.model.Course;

import java.util.Calendar;
import java.util.List;

/**
 * Created by simonla on 2017/3/21.
 * 下午9:01
 */

public class CourseRemindConvertFunc extends BaseRemindFunc {

    public CourseRemindConvertFunc(int delay) {
        super(delay);
    }

    @Override
    public List<Reminder> call(List<Course> courses) {
        for (Course c : courses) {
            Calendar calendar = delayCompute(courseToCalendar(c), mDelay);
            if (isToadyCourse(calendar) && !isLateToRemind(calendar)) {
                Reminder reminder = new Reminder();
                reminder.setCalendar(calendar);
                reminder.setTitle(c.course);
                calendar = courseToCalendar(c);
                String minute = "50";
                if (calendar.get(Calendar.MINUTE) == 0) {
                    minute = "00";
                }
                if (calendar.get(Calendar.MINUTE) == 5) {
                    minute = "05";
                }
                reminder.setSubTitle(calendar.get(Calendar.HOUR_OF_DAY) + ":" + minute + " @ " + c.classroom);
                mReminders.add(reminder);
            }
        }
        return mReminders;
    }

    private Calendar courseToCalendar(Course course) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        switch (course.hash_lesson) {
            case 0:
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND,0);
                break;
            case 1:
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 5);
                calendar.set(Calendar.SECOND,0);
                break;
            case 2:
                calendar.set(Calendar.HOUR_OF_DAY, 14);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND,0);
                break;
            case 3:
                calendar.set(Calendar.HOUR_OF_DAY, 16);
                calendar.set(Calendar.MINUTE, 5);
                calendar.set(Calendar.SECOND,0);
                break;
            case 4:
                calendar.set(Calendar.HOUR_OF_DAY, 19);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND,0);
                break;
            case 5:
                calendar.set(Calendar.HOUR_OF_DAY, 20);
                calendar.set(Calendar.HOUR_OF_DAY, 50);
                calendar.set(Calendar.SECOND,0);
                break;
        }
        switch (course.hash_day) {
            case 0:
                calendar.set(Calendar.DAY_OF_WEEK, 2);
                break;
            case 1:
                calendar.set(Calendar.DAY_OF_WEEK, 3);
                break;
            case 2:
                calendar.set(Calendar.DAY_OF_WEEK, 4);
                break;
            case 3:
                calendar.set(Calendar.DAY_OF_WEEK, 5);
                break;
            case 4:
                calendar.set(Calendar.DAY_OF_WEEK, 6);
                break;
            case 5:
                calendar.set(Calendar.DAY_OF_WEEK, 7);
                break;
            case 6:
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                break;
        }
        return calendar;
    }

    private boolean isToadyCourse(Calendar courseCalendar) {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        return !(courseCalendar.get(Calendar.DAY_OF_WEEK) !=
                today.get(Calendar.DAY_OF_WEEK) ||
                System.currentTimeMillis() > courseCalendar.getTimeInMillis());
    }

    private Calendar delayCompute(Calendar c,int minuteDelay) {
        int hourDelay = c.get(Calendar.MINUTE) - minuteDelay < 0 ? 1 : 0;
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)
                - hourDelay);
        if (hourDelay != 0) {
            c.set(Calendar.MINUTE, 60 + c.get(Calendar.MINUTE) - minuteDelay);
        }
        return c;
    }

    private boolean isLateToRemind(Calendar calendar) {
        return calendar.getTimeInMillis() < System.currentTimeMillis();
    }

}
