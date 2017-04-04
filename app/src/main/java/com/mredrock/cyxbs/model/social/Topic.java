package com.mredrock.cyxbs.model.social;

import java.util.List;

/**
 * Created by simonla on 2017/4/1.
 * 11:03
 */

public class Topic extends TopicApiWrapper<List<Topic>> {

    /**
     * topic_id : 1
     * content : {"content":"haha"}
     * keyword : 测试
     * join_num : 0
     * like_num : 0
     * article_num : 1
     * user_id : 2015211703
     * nickname : …(๑╯ﻌ╰๑)=3
     * user_photo : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1481705086_584114143.jpg
     * img : {"img_small_src":"","img_src":""}
     * is_my_join : false
     */

    private int topic_id;
    private ContentBean content;
    private String keyword;
    private int join_num;
    private int like_num;
    private int article_num;
    private int user_id;
    private String nickname;
    private String user_photo;
    private ImgBean img;
    private boolean is_my_join;

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getJoin_num() {
        return join_num;
    }

    public void setJoin_num(int join_num) {
        this.join_num = join_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getArticle_num() {
        return article_num;
    }

    public void setArticle_num(int article_num) {
        this.article_num = article_num;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public ImgBean getImg() {
        return img;
    }

    public void setImg(ImgBean img) {
        this.img = img;
    }

    public boolean isIs_my_join() {
        return is_my_join;
    }

    public void setIs_my_join(boolean is_my_join) {
        this.is_my_join = is_my_join;
    }

    public static class ContentBean {
        /**
         * content : haha
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class ImgBean {
        /**
         * img_small_src :
         * img_src :
         */

        private String img_small_src;
        private String img_src;

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
    }
}
