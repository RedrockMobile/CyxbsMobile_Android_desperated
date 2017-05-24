package com.mredrock.cyxbs.model.social;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathiasluo on 16-5-6.
 */
public class PersonInfo {

    public String phone;
    public String qq;
    public String gender;
    public String introduction;
    public String id;

    @SerializedName("stunum")
    public String stuNum;
    @SerializedName("username")
    public String userName;
    @SerializedName("nickname")
    public String nickName;
    @SerializedName("photo_thumbnail_src")
    public String photoThumbnail;
    @SerializedName("photo_src")
    public String photo;
    @SerializedName("updated_time")
    public String updatedTime;

    public String getIntroduction() {
        return introduction.equals("") ? "简介: " + "ta很懒,没有留下个人简介" : "简介： " + introduction;
    }


}
