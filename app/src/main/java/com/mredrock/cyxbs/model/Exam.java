package com.mredrock.cyxbs.model;

import java.util.List;

/**
 * Created by skylineTan on 2016/4/22 19:44.
 */
public class Exam implements Comparable<Exam>{
    public String week;
    public String weekday;
    public String student;
    public String xh;
    public String courseNo;
    public String course;
    public String classroom;
    public String examPrivillage;
    public String begin_time;
    public String end_time;
    public String seat;
    public String chineseWeekday;
    public String date;
    public String time;

    @Override
    public int compareTo(Exam exam) {
        int weekDifference =Integer.parseInt(week ) -  Integer.parseInt(exam.week);
        if (weekDifference != 0)
            return weekDifference;
        int weekDayDifference = Integer.parseInt(weekday) - Integer.parseInt(exam.weekday);
        if (weekDayDifference != 0)
            return weekDayDifference;
        return  Integer.parseInt(begin_time.replace(":",""))
                - Integer.parseInt(exam.begin_time.replace(":",""));


    }

    public static class ExamWapper extends RedrockApiWrapper<List<Exam>> {

    }
}
