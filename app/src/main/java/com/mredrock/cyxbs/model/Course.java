package com.mredrock.cyxbs.model;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {
    public int hash_day;
    public int hash_lesson;
    public int begin_lesson;
    public String day;
    public String lesson;
    public String course;
    public String teacher;
    public String classroom;
    public String rawWeek;
    public String weekModel;
    public int weekBegin;
    public int weekEnd;
    public String type;
    public List<Integer> week;
    public String status;
    // 连上几节
    public int period;

    public String toCourseString() {
        return course + '@' + classroom;
    }

    public static class CourseWrapper extends RedrockApiWrapper<List<Course>> {
        public String term;
        public String stuNum;
        public String nowWeek;
    }
}
