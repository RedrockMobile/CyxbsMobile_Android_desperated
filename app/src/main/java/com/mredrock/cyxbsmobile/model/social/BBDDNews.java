package com.mredrock.cyxbsmobile.model.social;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class BBDDNews extends RedrockApiWrapper<List<BBDDNews.BBDDBean>> {

    public static final int CYXW = 1;
    public static final int JWZX = 2;
    public static final int XSJZ = 3;
    public static final int XWGG = 4;
    public static final int BBDD = 5;
    public static final String LISTNEWS = "6";

    public static final int SHOTARTICLE = 0001;
    public static final int LISTARTICLE = 0002;
    public static final int JWZXARTICLE = 0003;

    public String page;

    public class BBDDBean {
        public String title;
        public String id;
        public String article_photo_src;
        public String article_thumbnail_src;
        public String type_id;
        public String content;
        public String updated_time;
        public String created_time;
        public String like_num;
        public String remark_num;
        public String nickname;
        public String photo_src;
        public String photo_thumbnail_src;
        public boolean is_my_like;
    }

}