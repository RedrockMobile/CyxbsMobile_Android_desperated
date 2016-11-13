package com.mredrock.cyxbs.model;



/**
 * Created by zhengyuxuan on 2016/10/11.
 */

public class Affair extends Course {

    public static final int TYPE = 2;
    protected int courseType = TYPE;

    public String uid;
    public int time;

    @Override
    public int getCourseType() {
        return courseType;
    }
}
