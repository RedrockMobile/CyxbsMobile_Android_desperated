package com.mredrock.cyxbsmobile.model.social;

import android.os.Parcel;
import android.os.Parcelable;

import com.mredrock.cyxbsmobile.util.TimeUtils;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class News  {


    /**
     * status : 200
     * page : 1
     * data : {"type":"bbdd","id":"5","user_id":null,"user_name":null,"user_head":"","time":"2016-04-12 13:58:35","contentBean":"数据库读写错误","img":{"img_small_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png","img_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png"},"like_num":"1","remark_num":"0","is_my_Like":false}
     */

    public int status;
    public String page;

    /**
     * type : bbdd
     * id : 5
     * user_id : null
     * user_name : null
     * user_head :
     * time : 2016-04-12 13:58:35
     * contentBean : 数据库读写错误
     * img : {"img_small_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png","img_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png"}
     * like_num : 1
     * remark_num : 0
     * is_my_Like : false
     */

    public DataBean data;

    public News(ContentBean contentBean) {
        this.data = new DataBean(contentBean);
    }

    public News(BBDDNews.BBDDBean bbddBean) {
        this.data = new DataBean(bbddBean.type_id
                , bbddBean.id
                , 5
                , "userid"
                , bbddBean.nickname
                , bbddBean.article_photo_src
                , bbddBean.created_time
                , new ContentBean(bbddBean.content)
                , new DataBean.ImgBean(bbddBean.article_thumbnail_src
                , bbddBean.article_photo_src)
                , bbddBean.like_num, bbddBean.remark_num
                , bbddBean.is_my_like);
    }

    public News(String content, List<Image> list) {
        list.remove(0);
        String a = "";
        String b = "";
        for (Image image : list) {
            a += image.url + ",";
            b += image.url + ",";

        }

        this.data = new DataBean(new DataBean.ImgBean(a, b), new ContentBean(content));
    }



    public static class DataBean implements Parcelable {

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String type;
        public String id;
        public int type_id;
        public String user_id;
        public String nick_name;
        public String user_head;
        public String time;
        public ContentBean content;

        /**
         * img_small_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png
         * img_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png
         */

        public ImgBean img;
        public String like_num;
        public String remark_num;
        public boolean is_my_Like;

        public DataBean(String type, String id, int type_id, String user_id, String nick_name,
                        String user_head, String time, ContentBean content, ImgBean img, String like_num,
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

        public DataBean(ContentBean content) {
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

        public DataBean(ImgBean img, ContentBean content) {
            this.img = img;
            this.content = content;
            this.type_id = BBDDNews.BBDD;

            this.nick_name = Stu.STU_NAME;
            this.time = TimeUtils.getTodayDate();
            this.like_num = "0";
            this.remark_num = "0";
        }

        protected DataBean(Parcel in) {
            type = in.readString();
            id = in.readString();
            type_id = in.readInt();
            user_id = in.readString();
            nick_name = in.readString();
            user_head = in.readString();
            time = in.readString();
            content = in.readParcelable(ContentBean.class.getClassLoader());
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


}
