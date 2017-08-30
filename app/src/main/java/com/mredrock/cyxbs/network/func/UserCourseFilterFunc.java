package com.mredrock.cyxbs.network.func;

import com.mredrock.cyxbs.model.Course;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;


/**
 * Created by cc on 16/5/8.
 */
public class UserCourseFilterFunc implements Function<List<Course>, List<Course>> {
    private int week;

    public UserCourseFilterFunc(int week) {
        this.week = week;
    }

    @Override
    public List<Course> apply(List<Course> courses) throws Exception {
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
