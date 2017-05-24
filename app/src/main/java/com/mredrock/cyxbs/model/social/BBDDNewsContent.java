package com.mredrock.cyxbs.model.social;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathiasluo on 16-5-5.
 */
public class BBDDNewsContent {
    @SerializedName("article_photo_src")
    public String articlePhotoSrc;
    @SerializedName("article_thumbnail_src")
    public String articleThumbnailSrc;
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
    @SerializedName("nickname")
    public String nickName;
    @SerializedName("photo_src")
    public String photoSrc;
    @SerializedName("photo_thumbnail_src")
    public String photoThumbnailSrc;
    @SerializedName("is_my_like")
    public boolean isMyLike;
    @SerializedName("stunum")
    public String stuNum;

    public String title;
    public String id;
    public String content;
}
