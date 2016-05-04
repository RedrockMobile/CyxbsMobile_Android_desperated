package com.mredrock.cyxbsmobile.model.community;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class ContentBean implements Parcelable {
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
    String id;
    String title;
    String date;
    String content;
    String articletype_id;
    String name;
    String address;
    String articleid;
    String read;
    String head;
    String unit;
    String remark_num;
    String like_num;
    boolean is_my_like;

    public ContentBean(String content) {
        this.content = content;
    }

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

    public String getArticletype_id() {
        return articletype_id;
    }

    public void setArticletype_id(String articletype_id) {
        this.articletype_id = articletype_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark_num() {
        return remark_num;
    }

    public void setRemark_num(String remark_num) {
        this.remark_num = remark_num;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public boolean is_my_like() {
        return is_my_like;
    }

    public void setIs_my_like(boolean is_my_like) {
        this.is_my_like = is_my_like;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
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
