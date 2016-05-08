package com.mredrock.cyxbs.model.social;

import com.mredrock.cyxbs.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by skylineTan on 2016/5/1 18:04.
 */
public class BBDDDetail {

    public String id;
    public String photo_src;
    public String thumbnail_src;
    public String content;
    public String type_id;
    public String updated_time;
    public String created_time;
    public String like_num;
    public String remark_num;

    public static class BBDDDetailWrapper extends RedrockApiWrapper<List<BBDDDetail>> {

    }
}
