package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.Course;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.functions.Func1;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class UserCourseFilterByWeekDayFunc implements Func1<List<Course>, List<Course>> {

    private int dayOfWeek;

    /**
     * filter courses by week day in {@link Calendar}
     *
     * @param dayOfWeek the same as {@link java.util.Calendar#DAY_OF_WEEK}<br>
     * should be one of {@link java.util.Calendar#SUNDAY},
     * {@link java.util.Calendar#MONDAY},
     * {@link java.util.Calendar#TUESDAY},
     * {@link java.util.Calendar#WEDNESDAY},
     * {@link java.util.Calendar#THURSDAY},
     * {@link java.util.Calendar#FRIDAY},
     * {@link java.util.Calendar#SATURDAY}
     */
    public UserCourseFilterByWeekDayFunc(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public List<Course> call(List<Course> courses) {
        int hashDay;
        if (dayOfWeek >= Calendar.MONDAY) {
            hashDay = dayOfWeek - 2;
        } else {  // Sunday
            hashDay = 6;
        }
        List<Course> resultList = new ArrayList<>();
        for (Course c: courses) {
            if (c.hash_day == hashDay) {
                resultList.add(c);
            }
        }
        return resultList;
    }
}
