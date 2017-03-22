package com.mredrock.cyxbs.component.remind_service;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by simonla on 2017/3/21.
 * 下午9:33
 */

public class Reminder implements Parcelable {

    private Calendar mCalendar;
    private String mTitle;
    private String mSubTitle;

    public Calendar getCalendar() {
        return mCalendar;
    }

    /**
     *
     * @param calendar 提醒的准确时间
     */
    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     *
     * @param title 显示在通知栏上的标题
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    /**
     *
     * @param subTitle 显示在通知栏上的副标题
     */
    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    @Override
    public int hashCode() {
        return  (mTitle + mSubTitle).hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mCalendar);
        dest.writeString(mTitle);
        dest.writeString(mSubTitle);
    }

    public Reminder(Parcel source) {
        mCalendar = (Calendar) source.readSerializable();
        mTitle = source.readString();
        mSubTitle = source.readString();
    }

    public Reminder() {}

    public static final Parcelable.Creator<Reminder> CREATOR = new Creator<Reminder>() {

        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
