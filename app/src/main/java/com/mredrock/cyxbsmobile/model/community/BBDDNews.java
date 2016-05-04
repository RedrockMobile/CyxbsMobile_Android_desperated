package com.mredrock.cyxbsmobile.model.community;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class BBDDNews {

    private int status;
    private String page;
    private List<BBDDBean> data;

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

    public List<BBDDBean> getData() {
        return data;
    }

    public void setData(List<BBDDBean> data) {
        this.data = data;
    }

    public class BBDDBean {
        /**
         * title : 标题我该打什么才好？？
         * id : 140
         * article_photo_src : 1461175774_424818802.jpg,
         * article_thumbnail_src : 1461175774_424818802.jpg,
         * type_id : 5
         * content : 没时间解释了，快上车
         * updated_time : 2016-04-21 02:09:34
         * created_time : 2016-04-21 02:09:34
         * like_num : 0
         * remark_num : 0
         * nickname :
         * photo_src :
         * photo_thumbnail_src :
         * is_my_like : false
         */

        private String title;
        private String id;
        private String article_photo_src;
        private String article_thumbnail_src;
        private String type_id;
        private String content;
        private String updated_time;
        private String created_time;
        private String like_num;
        private String remark_num;
        private String nickname;
        private String photo_src;
        private String photo_thumbnail_src;
        private boolean is_my_like;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getArticle_photo_src() {
            return article_photo_src;
        }

        public void setArticle_photo_src(String article_photo_src) {
            this.article_photo_src = article_photo_src;
        }

        public String getArticle_thumbnail_src() {
            return article_thumbnail_src;
        }

        public void setArticle_thumbnail_src(String article_thumbnail_src) {
            this.article_thumbnail_src = article_thumbnail_src;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUpdated_time() {
            return updated_time;
        }

        public void setUpdated_time(String updated_time) {
            this.updated_time = updated_time;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhoto_src() {
            return photo_src;
        }

        public void setPhoto_src(String photo_src) {
            this.photo_src = photo_src;
        }

        public String getPhoto_thumbnail_src() {
            return photo_thumbnail_src;
        }

        public void setPhoto_thumbnail_src(String photo_thumbnail_src) {
            this.photo_thumbnail_src = photo_thumbnail_src;
        }

        public boolean isIs_my_like() {
            return is_my_like;
        }

        public void setIs_my_like(boolean is_my_like) {
            this.is_my_like = is_my_like;
        }
    }
}
