package com.mredrock.cyxbs.model.social;

import com.mredrock.cyxbs.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by simonla on 2017/4/5.
 * 22:50
 */

public class TopicArticle extends RedrockApiWrapper<List<TopicArticle>> {

    /**
     * keyword : test
     * content : bbdd test
     * photo_src :
     * thumbnail_src :
     * like_num : 0
     * remark_num : 2
     * join_num : 3
     * article_num : 2
     * created_time : 2017-03-22 01:28:34
     * updated_time : 2017-03-22 01:28:34
     * state : 1
     * topic_id : 1
     * nickname : …(๑╯ﻌ╰๑)=3
     * is_my_join : false
     * user_photo_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1481705086_584114143.jpg
     * user_thumbnail_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1481705086_584114143.jpg
     * articles : [{"article_id":6,"type_id":7,"article_photo_src":"","article_thumbnail_src":"","title":"test","content":"test","nickname":"Microsoft","stunum":2013211581,"user_photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460358786_1173508514.png","user_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460358786_1173508514.png","like_num":0,"remark_num":0}]
     */

    private String keyword;
    private String content;
    private String photo_src;
    private String thumbnail_src;
    private int like_num;
    private int remark_num;
    private int join_num;
    private int article_num;
    private String created_time;
    private String updated_time;
    private int state;
    private int topic_id;
    private String nickname;
    private boolean is_my_join;
    private String user_photo_src;
    private String user_thumbnail_src;
    private List<ArticlesBean> articles;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto_src() {
        return photo_src;
    }

    public void setPhoto_src(String photo_src) {
        this.photo_src = photo_src;
    }

    public String getThumbnail_src() {
        return thumbnail_src;
    }

    public void setThumbnail_src(String thumbnail_src) {
        this.thumbnail_src = thumbnail_src;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getRemark_num() {
        return remark_num;
    }

    public void setRemark_num(int remark_num) {
        this.remark_num = remark_num;
    }

    public int getJoin_num() {
        return join_num;
    }

    public void setJoin_num(int join_num) {
        this.join_num = join_num;
    }

    public int getArticle_num() {
        return article_num;
    }

    public void setArticle_num(int article_num) {
        this.article_num = article_num;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isIs_my_join() {
        return is_my_join;
    }

    public void setIs_my_join(boolean is_my_join) {
        this.is_my_join = is_my_join;
    }

    public String getUser_photo_src() {
        return user_photo_src;
    }

    public void setUser_photo_src(String user_photo_src) {
        this.user_photo_src = user_photo_src;
    }

    public String getUser_thumbnail_src() {
        return user_thumbnail_src;
    }

    public void setUser_thumbnail_src(String user_thumbnail_src) {
        this.user_thumbnail_src = user_thumbnail_src;
    }

    public List<ArticlesBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesBean> articles) {
        this.articles = articles;
    }

    public static class ArticlesBean {
        /**
         * article_id : 6
         * type_id : 7
         * article_photo_src :
         * article_thumbnail_src :
         * title : test
         * content : test
         * nickname : Microsoft
         * stunum : 2013211581
         * user_photo_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460358786_1173508514.png
         * user_thumbnail_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460358786_1173508514.png
         * like_num : 0
         * remark_num : 0
         */

        private int article_id;
        private int type_id;
        private String article_photo_src;
        private String article_thumbnail_src;
        private String title;
        private String content;
        private String nickname;
        private int stunum;
        private String user_photo_src;
        private String user_thumbnail_src;
        private int like_num;
        private int remark_num;

        public int getArticle_id() {
            return article_id;
        }

        public void setArticle_id(int article_id) {
            this.article_id = article_id;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getStunum() {
            return stunum;
        }

        public void setStunum(int stunum) {
            this.stunum = stunum;
        }

        public String getUser_photo_src() {
            return user_photo_src;
        }

        public void setUser_photo_src(String user_photo_src) {
            this.user_photo_src = user_photo_src;
        }

        public String getUser_thumbnail_src() {
            return user_thumbnail_src;
        }

        public void setUser_thumbnail_src(String user_thumbnail_src) {
            this.user_thumbnail_src = user_thumbnail_src;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getRemark_num() {
            return remark_num;
        }

        public void setRemark_num(int remark_num) {
            this.remark_num = remark_num;
        }
    }
}
