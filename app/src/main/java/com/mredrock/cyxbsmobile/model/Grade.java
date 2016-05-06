package com.mredrock.cyxbsmobile.model;

import java.util.List;

/**
 * Created by skylineTan on 2016/4/21 19:31.
 */
public class Grade {

    public String course;
    public String grade;
    public String property;
    public String status;
    public String student;
    public String term;

    public static class GradeWrapper extends RedrockApiWrapper<List<Grade>> {

    }
}
