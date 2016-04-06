package com.mredrock.cyxbsmobile.model;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class News {


    /**
     * status : 200
     * page : 1
     * data : [{"type":"bbdd","id":"动态的ID","user_id":"2013211123","user_name":"用户名","user_head":"用户头像地址","time":"时间戳","content":"内容","img":[{"img_id":"图片的id，缩略图和原图相同","img_small_src":"http://www.baidu.com"},{"img_id":"图片的id，缩略图和原图相同","img_small_src":"http://www.qq.com"}],"like_num":13,"is_my_like":"true","comment_num":15}]
     */

    private int status;
    private int page;
    /**
     * type : bbdd
     * id : 动态的ID
     * user_id : 2013211123
     * user_name : 用户名
     * user_head : 用户头像地址
     * time : 时间戳
     * content : 内容
     * img : [{"img_id":"图片的id，缩略图和原图相同","img_small_src":"http://www.baidu.com"},{"img_id":"图片的id，缩略图和原图相同","img_small_src":"http://www.qq.com"}]
     * like_num : 13
     * is_my_like : true
     * comment_num : 15
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String type;
        private String id;
        private String user_id;
        private String user_name;
        private String user_head;
        private String time;
        private String content;
        private int like_num;
        private String is_my_like;
        private int comment_num;
        /**
         * img_id : 图片的id，缩略图和原图相同
         * img_small_src : http://www.baidu.com
         */

        private List<ImgBean> img;

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
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
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

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public String getIs_my_like() {
            return is_my_like;
        }

        public void setIs_my_like(String is_my_like) {
            this.is_my_like = is_my_like;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public List<ImgBean> getImg() {
            return img;
        }

        public void setImg(List<ImgBean> img) {
            this.img = img;
        }

        public static class ImgBean {
            private String img_id;
            private String img_small_src;

            public String getImg_id() {
                return img_id;
            }

            public void setImg_id(String img_id) {
                this.img_id = img_id;
            }

            public String getImg_small_src() {
                return img_small_src;
            }

            public void setImg_small_src(String img_small_src) {
                this.img_small_src = img_small_src;
            }
        }
    }
    
}
