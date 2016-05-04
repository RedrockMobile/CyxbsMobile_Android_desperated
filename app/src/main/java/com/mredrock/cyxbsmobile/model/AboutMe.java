package com.mredrock.cyxbsmobile.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/10 16:44.
 */
public class AboutMe implements Serializable{

    public String type;
    public String content;
    public String article_content;
    public String article_photo_src;
    public String created_time;
    public String article_id;
    public String stunum;
    public String nickname;
    public String photo_src;

    public static class AboutMeWapper extends RedrockApiWrapper<List<AboutMe>>{
    }
}
