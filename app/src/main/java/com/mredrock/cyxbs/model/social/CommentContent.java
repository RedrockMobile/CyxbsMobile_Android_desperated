package com.mredrock.cyxbs.model.social;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathiasluo on 16-5-5.
 */
public class CommentContent {

    @SerializedName("stunum")
    public String stuNum;
    @SerializedName("nickname")
    public String nickName;
    @SerializedName("username")
    public String userName;
    @SerializedName("photo_src")
    public String photoSrc;
    @SerializedName("photo_thumbnail_src")
    public String photoThumbnailSrc;
    @SerializedName("created_time")
    public String createdTime;

    public String content;

    public String getNickname() {
        return nickName != null && !nickName.equals("") ? nickName : "来自一位没有姓名的同学";
    }
}
