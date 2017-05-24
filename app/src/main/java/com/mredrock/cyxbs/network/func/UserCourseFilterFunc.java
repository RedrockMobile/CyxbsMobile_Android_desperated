package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.Course;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by cc on 16/5/8.
 */
public class UserCourseFilterFunc implements Func1<List<Course>, List<Course>> {
    private int week;

    public UserCourseFilterFunc(int week) {
        this.week = week;
    }

    @Override
    public List<Course> call(List<Course> courses) {

        ArrayList<Course> list = new ArrayList<>();

        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            if (week == 0 || c.week.contains(week)) {
                list.add(c);
            }
        }
        return list;
    }
}
