package com.mredrock.cyxbs.util;

import com.mredrock.cyxbs.model.Course;

import java.util.Calendar;

/**
 * Created by simonla on 2016/10/25.
 * 下午5:29
 */

public class CourseTimeUtils {
    public static Calendar CourseToCalendar(Course course) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        switch (course.hash_lesson) {
            case 0:
                calendar.set(Calendar.HOUR_OF_DAY,8);
                calendar.set(Calendar.MINUTE, 0);
                break;
            case 1:
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 5);
                break;
            case 2:
                calendar.set(Calendar.HOUR_OF_DAY, 14);
                calendar.set(Calendar.MINUTE, 0);
                break;
            case 3:
                calendar.set(Calendar.HOUR_OF_DAY, 16);
                calendar.set(Calendar.MINUTE, 5);
                break;
            case 4:
                calendar.set(Calendar.HOUR_OF_DAY, 19);
                calendar.set(Calendar.MINUTE, 0);
                break;
            case 5:
                calendar.set(Calendar.HOUR_OF_DAY, 20);
                calendar.set(Calendar.HOUR_OF_DAY, 50);
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

}
