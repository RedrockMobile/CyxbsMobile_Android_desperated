package com.mredrock.cyxbs.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mredrock.cyxbs.APP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable, Parcelable {

    public static final int TYPE = 1;
    public int courseType = TYPE;

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

    public Course() {

    }

    public String toCourseString() {
        return course + '@' + classroom;
    }

    public int getCourseType() {
        return courseType;
    }

    public int getBeginLesson() {
        return hash_lesson * 2 + 1;
    }

    public static class CourseWrapper extends RedrockApiWrapper<List<Course>> {
        public String term;
        public String stuNum;
        public String nowWeek;
    }

    /****************************************/
    /* Parcelable implementation under here */
    /****************************************/
    /* If you update this class, please     */
    /* regenerate these code.               */
    /****************************************/

    protected Course(Parcel in) {
        courseType = in.readInt();
        hash_day = in.readInt();
        hash_lesson = in.readInt();
        begin_lesson = in.readInt();
        day = in.readString();
        lesson = in.readString();
        course = in.readString();
        teacher = in.readString();
        classroom = in.readString();
        rawWeek = in.readString();
        weekModel = in.readString();
        weekBegin = in.readInt();
        weekEnd = in.readInt();
        type = in.readString();
        week = new ArrayList<>(0);
        in.readList(week, APP.getContext().getClassLoader());
        status = in.readString();
        period = in.readInt();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(courseType);
        dest.writeInt(hash_day);
        dest.writeInt(hash_lesson);
        dest.writeInt(begin_lesson);
        dest.writeString(day);
        dest.writeString(lesson);
        dest.writeString(course);
        dest.writeString(teacher);
        dest.writeString(classroom);
        dest.writeString(rawWeek);
        dest.writeString(weekModel);
        dest.writeInt(weekBegin);
        dest.writeInt(weekEnd);
        dest.writeString(type);
        dest.writeList(week);
        dest.writeString(status);
        dest.writeInt(period);
    }

}
