package com.mredrock.cyxbs.event;

import com.mredrock.cyxbs.model.Course;

/**
 * Created by ：AceMurder
 * Created on ：2016/11/16
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class AffairDeleteEvent {
    private Course course;

    public AffairDeleteEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
