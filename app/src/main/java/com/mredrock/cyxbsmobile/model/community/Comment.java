package com.mredrock.cyxbsmobile.model.community;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class Comment extends RedrockApiWrapper<List<Comment.Remark>> {

    public int state;

    public static class Remark {
        public String stunum;
        public String nickname;
        public String username;
        public String photo_src;
        public String photo_thumbnail_src;
        public String created_time;
        public String content;
    }
}
