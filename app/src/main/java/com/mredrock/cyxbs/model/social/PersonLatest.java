package com.mredrock.cyxbs.model.social;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathiasluo on 16-5-6.
 */
public class PersonLatest {

    public String id;
    @SerializedName("photo_src")
    public String photo;
    @SerializedName("thumbnail_src")
    public String thumbnailPhoto;
    public String content;
    @SerializedName("type_id")
    public String typeId;
    @SerializedName("created_time")
    public String createdTime;
    @SerializedName("updated_time")
    public String updatedTime;
    @SerializedName("like_num")
    public String likeNum;
    @SerializedName("remark_num")
    public String remarkNum;


}
