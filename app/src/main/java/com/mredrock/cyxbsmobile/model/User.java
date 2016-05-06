package com.mredrock.cyxbsmobile.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Info of user
 * <p>
 * Created by David on 15/5/15.
 */
public class User implements Parcelable{
    public User() {
    }

    public String stuNum;
    public String idNum;
    public String name;
    public String gender;
    public String classNum;
    public String major;
    public String college;
    public String grade;
    public String stu;
    public String stunum;
    public String photo_thumbnail_src;
    public String photo_src;
    public String nickname;
    public String qq;
    public String phone;
    public String introduction;


    protected User(Parcel in) {
        stuNum = in.readString();
        idNum = in.readString();
        name = in.readString();
        gender = in.readString();
        classNum = in.readString();
        major = in.readString();
        college = in.readString();
        grade = in.readString();
        stu = in.readString();
        stunum = in.readString();
        photo_thumbnail_src = in.readString();
        photo_src = in.readString();
        nickname = in.readString();
        qq = in.readString();
        phone = in.readString();
        introduction = in.readString();
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override public User createFromParcel(Parcel in) {
            return new User(in);
        }


        @Override public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override public int describeContents() {
        return 0;
    }


    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stuNum);
        dest.writeString(idNum);
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeString(classNum);
        dest.writeString(major);
        dest.writeString(college);
        dest.writeString(grade);
        dest.writeString(stu);
        dest.writeString(stunum);
        dest.writeString(photo_thumbnail_src);
        dest.writeString(photo_src);
        dest.writeString(nickname);
        dest.writeString(qq);
        dest.writeString(phone);
        dest.writeString(introduction);
    }


    public static class UserWrapper extends RedrockApiWrapper<User> {
    }

}
