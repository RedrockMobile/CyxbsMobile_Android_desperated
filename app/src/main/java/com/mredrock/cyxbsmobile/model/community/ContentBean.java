package com.mredrock.cyxbsmobile.model.community;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class ContentBean implements Parcelable {

    public String id;
    public String title;
    public String date;
    public String content;
    public String articletype_id;
    public String name;
    public String address;
    public String articleid;
    public String read;
    public String head;
    public String unit;
    public String remark_num;
    public String like_num;
    public boolean is_my_like;


    public ContentBean(String content) {
        this.content = content;
    }

    public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
        @Override
        public ContentBean createFromParcel(Parcel in) {
            return new ContentBean(in);
        }

        @Override
        public ContentBean[] newArray(int size) {
            return new ContentBean[size];
        }
    };




    protected ContentBean(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        content = in.readString();
        articletype_id = in.readString();
        name = in.readString();
        address = in.readString();
        articleid = in.readString();
        read = in.readString();
        head = in.readString();
        unit = in.readString();
        remark_num = in.readString();
        like_num = in.readString();
        is_my_like = in.readByte() != 0;
    }

    public static Creator<ContentBean> getCREATOR() {
        return CREATOR;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(content);
        parcel.writeString(articletype_id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(articleid);
        parcel.writeString(read);
        parcel.writeString(head);
        parcel.writeString(unit);
        parcel.writeString(remark_num);
        parcel.writeString(like_num);
        parcel.writeByte((byte) (is_my_like ? 1 : 0));
    }
}
