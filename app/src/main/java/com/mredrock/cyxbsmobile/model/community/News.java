package com.mredrock.cyxbsmobile.model.community;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class News {
    /**
     * status : 200
     * page : 1
     * data : {"type":"bbdd","id":"5","user_id":null,"user_name":null,"user_head":"","time":"2016-04-12 13:58:35","content":"数据库读写错误","img":{"img_small_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png","img_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png"},"like_num":"1","remark_num":"0","is_my_Like":false}
     */

    private int status;
    private String page;
    /**
     * type : bbdd
     * id : 5
     * user_id : null
     * user_name : null
     * user_head :
     * time : 2016-04-12 13:58:35
     * content : 数据库读写错误
     * img : {"img_small_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png","img_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png"}
     * like_num : 1
     * remark_num : 0
     * is_my_Like : false
     */


    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {

        private String type;
        private String id;
        private int type_id;


        private String user_id;
        private String nick_name;
        private String user_head;
        private String time;
        private String content;
        /**
         * img_small_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460427947_1265413131.png
         * img_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460427947_1265413131.png
         */

        private ImgBean img;
        private String like_num;
        private String remark_num;
        private boolean is_my_Like;

        protected DataBean(Parcel in) {
            type = in.readString();
            id = in.readString();
            user_head = in.readString();
            time = in.readString();
            content = in.readString();
            img = in.readParcelable(ImgBean.class.getClassLoader());
            like_num = in.readString();
            remark_num = in.readString();
            is_my_Like = in.readByte() != 0;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public boolean is_my_Like() {
            return is_my_Like;
        }

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return nick_name;
        }

        public void setUser_name(String user_name) {
            this.nick_name = user_name;
        }

        public String getUser_head() {
            return user_head;
        }

        public void setUser_head(String user_head) {
            this.user_head = user_head;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public ImgBean getImg() {
            return img;
        }

        public void setImg(ImgBean img) {
            this.img = img;
        }

        public String getLike_num() {
            return like_num;
        }

        public void setLike_num(String like_num) {
            this.like_num = like_num;
        }

        public String getRemark_num() {
            return remark_num;
        }

        public void setRemark_num(String remark_num) {
            this.remark_num = remark_num;
        }

        public boolean isIs_my_Like() {
            return is_my_Like;
        }

        public void setIs_my_Like(boolean is_my_Like) {
            this.is_my_Like = is_my_Like;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(type);
            parcel.writeString(id);
            parcel.writeString(user_head);
            parcel.writeString(time);
            parcel.writeString(content);
            parcel.writeParcelable(img, i);
            parcel.writeString(like_num);
            parcel.writeString(remark_num);
            parcel.writeByte((byte) (is_my_Like ? 1 : 0));
        }

        public static class ImgBean implements Parcelable {
            private String img_small_src;
            private String img_src;

            protected ImgBean(Parcel in) {
                img_small_src = in.readString();
                img_src = in.readString();
            }

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

            public String getImg_small_src() {
                return img_small_src;
            }

            public void setImg_small_src(String img_small_src) {
                this.img_small_src = img_small_src;
            }

            public String getImg_src() {
                return img_src;
            }

            public void setImg_src(String img_src) {
                this.img_src = img_src;
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
