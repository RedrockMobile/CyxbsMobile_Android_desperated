package com.mredrock.cyxbs.model.social;

import com.mredrock.cyxbs.model.RedrockApiWrapper;

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
