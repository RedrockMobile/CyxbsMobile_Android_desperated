package com.mredrock.cyxbsmobile.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/13 16:24.
 */
public class Student implements Serializable {

    public String stunum;
    public String name;
    public String gender;
    public String classnum;
    public String major;
    public String depart;
    public String grade;

    public static class StudentWrapper extends RedrockApiWrapper<List<Student>> {
        public int state;
    }
}
