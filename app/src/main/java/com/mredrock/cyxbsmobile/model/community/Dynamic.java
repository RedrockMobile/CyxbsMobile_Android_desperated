package com.mredrock.cyxbsmobile.model.community;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class Dynamic {

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

    private String type_id;
    private String title;
    private String user_id;
    private String content;
    private String thumbnail_src;
    private String photo_src;
    private String stuNum;
    private String idNum;

    public Dynamic(String type_id, String title, String user_id, String content, String thumbnail_src, String photo_src, String stuNum, String idNum) {
        this.type_id = type_id;
        this.title = title;
        this.user_id = user_id;
        this.content = content;
        this.thumbnail_src = thumbnail_src;
        this.photo_src = photo_src;
        this.stuNum = stuNum;
        this.idNum = idNum;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail_src() {
        return thumbnail_src;
    }

    public void setThumbnail_src(String thumbnail_src) {
        this.thumbnail_src = thumbnail_src;
    }

    public String getPhoto_src() {
        return photo_src;
    }

    public void setPhoto_src(String photo_src) {
        this.photo_src = photo_src;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }
}
