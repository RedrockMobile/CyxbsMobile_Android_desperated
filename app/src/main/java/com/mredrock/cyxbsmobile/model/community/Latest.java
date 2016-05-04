package com.mredrock.cyxbsmobile.model.community;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class Latest {

    /**
     * type_id : 5
     * title : 我就玩玩
     * user_id : 2014212041
     * content : 就是流弊
     * thumbnail_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460439557_1391569509.jpg
     * photo_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460439557_1391569509.jpg
     * stuNum : 2014212041
     * idNum : 044737
     */

    public String type_id;
    public String title;
    public String user_id;
    public String content;
    public String thumbnail_src;
    public String photo_src;
    public String stuNum;
    public String idNum;

    public Latest(String type_id, String title, String user_id, String content, String thumbnail_src, String photo_src, String stuNum, String idNum) {
        this.type_id = type_id;
        this.title = title;
        this.user_id = user_id;
        this.content = content;
        this.thumbnail_src = thumbnail_src;
        this.photo_src = photo_src;
        this.stuNum = stuNum;
        this.idNum = idNum;
    }


}
