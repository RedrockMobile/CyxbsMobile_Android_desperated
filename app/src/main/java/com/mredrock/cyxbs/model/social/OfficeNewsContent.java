package com.mredrock.cyxbs.model.social;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class OfficeNewsContent implements Parcelable {

    public String id;
    public String title;
    public String date;
    public String content;
    public String name;
    public String address;
    public String read;
    public String head;
    public String unit;
    @SerializedName("articletype_id")
    public String articletypeId;
    @SerializedName("articleid")
    public String articleId;
    @SerializedName("like_num")
    public String likeNum;
    @SerializedName("remark_num")
    public String remarkNum;
    @SerializedName("is_my_like")
    public boolean isMyLike;

    public String getOfficeName() {
        int typeId = 0;
        String type = "红岩网校工作站";
        if (StringUtils.isNotBlank(articletypeId)) {
            typeId = Integer.parseInt(articletypeId);
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



    public OfficeNewsContent(String content) {
        this.content = content;
    }

    public static final Creator<OfficeNewsContent> CREATOR = new Creator<OfficeNewsContent>() {
        @Override
        public OfficeNewsContent createFromParcel(Parcel in) {
            return new OfficeNewsContent(in);
        }

        @Override
        public OfficeNewsContent[] newArray(int size) {
            return new OfficeNewsContent[size];
        }
    };


    protected OfficeNewsContent(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        content = in.readString();
        articletypeId = in.readString();
        name = in.readString();
        address = in.readString();
        articleId = in.readString();
        read = in.readString();
        head = in.readString();
        unit = in.readString();
        remarkNum = in.readString();
        likeNum = in.readString();
        isMyLike = in.readByte() != 0;
    }

    public static Creator<OfficeNewsContent> getCREATOR() {
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
        parcel.writeString(articletypeId);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(articleId);
        parcel.writeString(read);
        parcel.writeString(head);
        parcel.writeString(unit);
        parcel.writeString(remarkNum);
        parcel.writeString(likeNum);
        parcel.writeByte((byte) (isMyLike ? 1 : 0));
    }

    @Override
    public String toString() {
        return "OfficeNewsContent{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", articletype_id='" + articletypeId + '\'' +
                ", name='" + name + '\'' +
                ", articleid='" + articleId + '\'' +
                ", read='" + read + '\'' +
                ", head='" + head + '\'' +
                ", unit='" + unit + '\'' +
                ", remark_num='" + remarkNum + '\'' +
                ", like_num='" + likeNum + '\'' +
                ", is_my_like=" + isMyLike +
                '}';
    }
}
