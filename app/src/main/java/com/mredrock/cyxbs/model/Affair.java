package com.mredrock.cyxbs.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zhengyuxuan on 2016/10/11.
 */

public class Affair extends Course implements Serializable, Parcelable {

    public static final int TYPE = 2;

    public String uid;
    //提醒时间，正数
    public int time;

    public Affair() {
        courseType = Affair.TYPE;
    }

    /****************************************/
    /* Parcelable implementation under here */
    /****************************************/
    /* If you update this class, please     */
    /* merge the change here.               */
    /****************************************/

    protected Affair(Parcel in) {
        super(in);
        uid = in.readString();
        time = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(uid);
        dest.writeInt(time);
    }

    public static final Parcelable.Creator<Affair> CREATOR = new Parcelable.Creator<Affair>()
    {
        public Affair createFromParcel(Parcel in)
        {
            return new Affair(in);
        }

        public Affair[] newArray(int size)
        {
            return new Affair[size];
        }
    };

}
