package com.mredrock.cyxbs.model;


import java.io.Serializable;

import static com.umeng.analytics.social.e.t;

/**
 * Created by zhengyuxuan on 2016/10/11.
 */

public class Affair extends Course implements Serializable{

    public static final int TYPE = 2;

    public String uid;
    public int time;

    public Affair() {
        courseType = Affair.TYPE;
    }

}
