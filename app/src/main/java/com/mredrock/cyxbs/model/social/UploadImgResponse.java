package com.mredrock.cyxbs.model.social;

import com.google.gson.annotations.SerializedName;
import com.mredrock.cyxbs.model.RedrockApiWrapper;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class UploadImgResponse extends RedrockApiWrapper<UploadImgResponse.Response> {

    public static class Response {

        public int state;
        public String date;

        @SerializedName("stunum")
        public String stuNum;
        @SerializedName("photosrc")
        public String photoSrc;
        @SerializedName("thumbnail_src")
        public String thumbnailSrc;

    }

}
