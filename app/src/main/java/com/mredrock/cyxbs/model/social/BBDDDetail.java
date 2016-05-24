package com.mredrock.cyxbs.model.social;

import com.google.gson.annotations.SerializedName;
import com.mredrock.cyxbs.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by skylineTan on 2016/5/1 18:04.
 */
public class BBDDDetail {

    public String id;
    public String content;

    @SerializedName("photo_src")
    public String photoSrc;
    @SerializedName("thumbnail_src")
    public String thumbnailSrc;
    @SerializedName("type_id")
    public String typeId;
    @SerializedName("updated_time")
    public String updatedTime;
    @SerializedName("created_time")
    public String createdTime;
    @SerializedName("like_num")
    public String likeNum;
    @SerializedName("remark_num")
    public String remarkNum;

    public static class BBDDDetailWrapper extends RedrockApiWrapper<List<BBDDDetail>> {

    }
}
