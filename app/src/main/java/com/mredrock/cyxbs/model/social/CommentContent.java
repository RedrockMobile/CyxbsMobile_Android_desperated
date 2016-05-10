package com.mredrock.cyxbs.model.social;

/**
 * Created by mathiasluo on 16-5-5.
 */
public class CommentContent {

    public String stunum;
    public String nickname;
    public String username;
    public String photo_src;
    public String photo_thumbnail_src;
    public String created_time;
    public String content;

    public String getNickname() {
        return nickname != null && !nickname.equals("") ? nickname : "来至一位没有姓名的同学";
    }
}
