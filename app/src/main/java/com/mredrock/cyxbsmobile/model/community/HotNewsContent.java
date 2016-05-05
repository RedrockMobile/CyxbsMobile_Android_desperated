package com.mredrock.cyxbsmobile.model.community;

import android.os.Parcel;
import android.os.Parcelable;

import com.mredrock.cyxbsmobile.util.TimeUtils;

/**
 * Created by mathiasluo on 16-5-5.
 */
public class HotNewsContent implements Parcelable {

    public static final Parcelable.Creator<HotNewsContent> CREATOR = new Parcelable.Creator<HotNewsContent>() {
        @Override
        public HotNewsContent createFromParcel(Parcel in) {
            return new HotNewsContent(in);
        }

        @Override
        public HotNewsContent[] newArray(int size) {
            return new HotNewsContent[size];
        }
    };

    public String type;
    public String id;
    public int type_id;
    public String user_id;
    public String nick_name;
    public String user_head;
    public String time;
    public OfficeNewsContent content;

    /**
     * img_small_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png
     * img_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png
     */

    public ImgBean img;
    public String like_num;
    public String remark_num;
    public boolean is_my_Like;

    public HotNewsContent(String type, String id, int type_id, String user_id, String nick_name,
                          String user_head, String time, OfficeNewsContent content, ImgBean img, String like_num,
                          String remark_num, boolean is_my_Like) {
        this.type = type;
        this.id = id;
        this.type_id = type_id;
        this.user_id = user_id;
        this.nick_name = nick_name;
        this.user_head = user_head;
        this.time = time;
        this.content = content;
        this.img = img;
        this.like_num = like_num;
        this.remark_num = remark_num;
        this.is_my_Like = is_my_Like;
    }

    public HotNewsContent(OfficeNewsContent content) {
        this.content = content;
        this.type_id = 6;
        this.nick_name = content.name;
        this.user_head = content.head;
        this.time = content.date;
        this.remark_num = content.remark_num;
        this.id = content.id;
        this.img = new ImgBean("", "");
        this.like_num = content.read;

    }

    public HotNewsContent(ImgBean img, OfficeNewsContent content) {
        this.img = img;
        this.content = content;
        this.type_id = BBDDNews.BBDD;

        this.nick_name = Stu.STU_NAME;
        this.time = TimeUtils.getTodayDate();
        this.like_num = "0";
        this.remark_num = "0";
    }

    protected HotNewsContent(Parcel in) {
        type = in.readString();
        id = in.readString();
        type_id = in.readInt();
        user_id = in.readString();
        nick_name = in.readString();
        user_head = in.readString();
        time = in.readString();
        content = in.readParcelable(OfficeNewsContent.class.getClassLoader());
        img = in.readParcelable(ImgBean.class.getClassLoader());
        like_num = in.readString();
        remark_num = in.readString();
        is_my_Like = in.readByte() != 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(id);
        parcel.writeInt(type_id);
        parcel.writeString(user_id);
        parcel.writeString(nick_name);
        parcel.writeString(user_head);
        parcel.writeString(time);
        parcel.writeParcelable(content, i);
        parcel.writeParcelable(img, i);
        parcel.writeString(like_num);
        parcel.writeString(remark_num);
        parcel.writeByte((byte) (is_my_Like ? 1 : 0));
    }


    public static class ImgBean implements Parcelable {

        public String img_small_src;
        public String img_src;

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


        public ImgBean(String img_small_src, String img_src) {
            this.img_small_src = img_small_src;
            this.img_src = img_src;
        }

        protected ImgBean(Parcel in) {
            img_small_src = in.readString();
            img_src = in.readString();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(img_small_src);
            parcel.writeString(img_src);
        }
    }

}
