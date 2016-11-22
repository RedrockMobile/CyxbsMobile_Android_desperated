package com.mredrock.cyxbs.model;


import java.io.Serializable;

/**
 * Created by zhengyuxuan on 2016/10/11.
 */

public class Affair extends Course implements Serializable {

    public static final int TYPE = 2;

    public String uid;
    //提醒时间，正数
    public int time;

    public Affair() {
        courseType = Affair.TYPE;
    }

}
