package com.mredrock.cyxbsmobile.model.social;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

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

    public String getArticletype_id() {
        int typeId = 0;
        String type = "红岩网校工作站";
        if (StringUtils.isNotBlank(articletype_id)) {
            typeId = Integer.parseInt(articletype_id);
            switch (typeId) {
                case 1:
                    type = "重邮新闻";
                    break;
                case 2:
                    type = "教务新闻";
                    break;
                case 3:
                    type = "E彩鎏光";
                    break;
                case 4:
                    type = "校务公告";
                    break;
                case 5:
                    type = "哔哔叨叨";
                    break;
                default:
                    break;
            }
        }
        return type;
    }

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
