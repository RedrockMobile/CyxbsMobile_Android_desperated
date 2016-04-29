package com.mredrock.cyxbsmobile.model.community;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/28 00:30.
 */
public class Trend {

    public String id;
    public String photo_src;
    public String thumbnail_src;
    public String content;
    public String type_id;
    public String updated_time;
    public String created_time;
    public String like_num;
    public String remark_num;

    public static class TrendWrapper extends RedrockApiWrapper<List<Trend>>{

    }
}
