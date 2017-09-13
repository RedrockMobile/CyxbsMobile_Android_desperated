package com.mredrock.cyxbs.model.social;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mredrock.cyxbs.util.Utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by mathiasluo on 16-5-5.
 */
public class HotNewsContent implements Parcelable {


    public String type;
    public String id;
    public ImgBean img;
    public String time;

    @SerializedName("type_id")
    public int typeId;
    @SerializedName("user_id")
    public String user_id;
    @SerializedName("nick_name")
    public String nickName;
    @SerializedName("user_head")
    public String userHead;
    @SerializedName("article_id")
    public String articleId;
    @SerializedName("content")
    public OfficeNewsContent officeNewsContent;
    @SerializedName("like_num")
    public String likeNum;
    @SerializedName("remark_num")
    public String remarkNum;
    @SerializedName("is_my_Like")
    public boolean isMyLike;

    public HotNewsContent() {
    }

    public static final Creator<HotNewsContent> CREATOR = new Creator<HotNewsContent>() {
        @Override
        public HotNewsContent createFromParcel(Parcel in) {
            return new HotNewsContent(in);
        }

        @Override
        public HotNewsContent[] newArray(int size) {
            return new HotNewsContent[size];
        }
    };


    public String getNickName() {
        String name = typeId < BBDDNews.BBDD ? geTypeId() : StringUtils.isEmpty(nickName) ? "来自一位没有名字的同学" : nickName;
        return name;
    }


    public String getTime() {
        if (time.contains(":")) return time;
        else if (time.contains("-")) return time + " 00:00:00";
        else if (time.contains(".")) return time.replace(".", "-") + " 00:00:00";
        else return "2015-01-01 00:00:00";
    }

    public String geTypeId() {
        String type = "红岩网校工作站";
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
            case 7:
                type = "话题";
                break;
            default:
                break;
        }
        return type;
    }


    /**
     * img_small_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png
     * img_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png
     */


    public HotNewsContent(String type, String id, int typeId, String user_id, String nick_name,
                          String user_head, String time, OfficeNewsContent content, ImgBean img, String like_num,
                          String remark_num, boolean is_my_Like, String article_id) {
        this.type = type;
        this.id = id;
        this.typeId = typeId;
        this.user_id = user_id;
        this.nickName = nick_name;
        this.userHead = user_head;
        this.time = time;
        this.officeNewsContent = content;
        this.img = img;
        this.likeNum = like_num;
        this.remarkNum = remark_num;
        this.isMyLike = is_my_Like;
        this.articleId = article_id;
    }

    public HotNewsContent(OfficeNewsContent content) {
        this(new ImgBean("", ""), content);
    }

    public HotNewsContent(ImgBean img, OfficeNewsContent content) {
        this.img = img;

        this.officeNewsContent = content;
        this.type = content.articletypeId;
        this.typeId = Integer.parseInt(Utils.checkNotNullWithDefaultValue(content.articletypeId, "5"));
        this.nickName = content.name;
        this.userHead = content.head;
        this.time = content.date;
        this.remarkNum = content.remarkNum;
        this.id = content.id;

        this.likeNum = content.likeNum;
        this.isMyLike = content.isMyLike;
        this.articleId = content.id;
    }

    protected HotNewsContent(Parcel in) {
        type = in.readString();
        id = in.readString();
        typeId = in.readInt();
        user_id = in.readString();
        nickName = in.readString();
        userHead = in.readString();
        time = in.readString();
        officeNewsContent = in.readParcelable(OfficeNewsContent.class.getClassLoader());
        img = in.readParcelable(ImgBean.class.getClassLoader());
        likeNum = in.readString();
        remarkNum = in.readString();
        isMyLike = in.readByte() != 0;
        articleId = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(id);
        parcel.writeInt(typeId);
        parcel.writeString(user_id);
        parcel.writeString(nickName);
        parcel.writeString(userHead);
        parcel.writeString(time);
        parcel.writeParcelable(officeNewsContent, i);
        parcel.writeParcelable(img, i);
        parcel.writeString(likeNum);
        parcel.writeString(remarkNum);
        parcel.writeByte((byte) (isMyLike ? 1 : 0));
        parcel.writeString(articleId);
    }


    public static class ImgBean implements Parcelable {

        @SerializedName("img_small_src")
        public String smallImg;
        @SerializedName("img_src")
        public String normalImg;

        public static final Creator<ImgBean> CREATOR = new Creator<ImgBean>() {
            @Override
            public ImgBean createFromParcel(Parcel in) {
                return new ImgBean(in);
            }

            @Override
            public ImgBean[] newArray(int size) {
                return new ImgBean[size];
            }
        };

        public ImgBean(String smallImg, String normalImg) {
            this.smallImg = smallImg;
            this.normalImg = normalImg;
        }

        protected ImgBean(Parcel in) {
            smallImg = in.readString();
            normalImg = in.readString();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(smallImg);
            parcel.writeString(normalImg);
        }
    }

}
