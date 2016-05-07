package com.mredrock.cyxbsmobile.model;

import java.util.List;

/**
 * Created by skylineTan on 2016/4/22 19:44.
 */
public class Exam {
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

    public static class ExamWapper extends RedrockApiWrapper<List<Exam>> {

    }
}
