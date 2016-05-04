package com.mredrock.cyxbsmobile.model.community;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class UploadImgResponse extends RedrockApiWrapper<UploadImgResponse.Response> {

    public static class Response {
        public String stunum;
        public String date;
        public String photosrc;
        public String thumbnail_src;
        public int state;

    }
}
