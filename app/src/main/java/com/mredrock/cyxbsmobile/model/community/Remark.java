package com.mredrock.cyxbsmobile.model.community;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class Remark {

    /**
     * state : 200
     * data : [{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:36:22","content":"我就看看可不可以评论"},{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:36:21","content":"我就看看可不可以评论"},{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:36:20","content":"我就看看可不可以评论"},{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:35:09","content":"我就看看可不可以评论"}]
     */

    private int state;
    /**
     * stunum : 2013211594
     * nickname : Microsoft
     * username : 杨宇星
     * photo_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png
     * photo_thumbnail_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png
     * created_time : 2016-04-14 12:36:22
     * content : 我就看看可不可以评论
     */

    private List<ReMark> data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<ReMark> getData() {
        return data;
    }

    public void setData(List<ReMark> data) {
        this.data = data;
    }

    public static class ReMark {
        private String stunum;
        private String nickname;
        private String username;
        private String photo_src;
        private String photo_thumbnail_src;
        private String created_time;
        private String content;

        public String getStunum() {
            return stunum;
        }

        public void setStunum(String stunum) {
            this.stunum = stunum;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhoto_src() {
            return photo_src;
        }

        public void setPhoto_src(String photo_src) {
            this.photo_src = photo_src;
        }

        public String getPhoto_thumbnail_src() {
            return photo_thumbnail_src;
        }

        public void setPhoto_thumbnail_src(String photo_thumbnail_src) {
            this.photo_thumbnail_src = photo_thumbnail_src;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
