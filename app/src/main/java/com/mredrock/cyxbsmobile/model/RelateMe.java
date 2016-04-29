package com.mredrock.cyxbsmobile.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/10 16:44.
 */
public class RelateMe implements Serializable{

    public String type;
    public String content;
    public String article_content;
    public String created_time;
    public String article_id;
    public String stunum;
    public String nickname;
    public String photo_src;

    /**
     * "status": 200,
     * "data":[{"id":"动态的ID","user_id":"2013211123","user_name":"用户名","user_head":"用户头像地址","time":"时间戳","type":"类型:点赞还是评论","comment":"如果是评论就显示评论内容，如果是点赞就返回空字符串"}]
     */

    public static class RelateMeWapper extends RedrockApiWrapper<List<RelateMe>>{
    }
}
