package com.mredrock.cyxbsmobile.model.community;

import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class Comment extends RedrockApiWrapper<List<Comment.ReMark>> {

    /**
     * state : 200
     * data : [{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:36:22","content":"我就看看可不可以评论"},{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:36:21","content":"我就看看可不可以评论"},{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:36:20","content":"我就看看可不可以评论"},{"stunum":"2013211594","nickname":"Microsoft","username":"杨宇星","photo_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png","photo_thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png","created_time":"2016-04-14 12:35:09","content":"我就看看可不可以评论"}]
     */

    public int state;
    /**
     * stunum : 2013211594
     * nickname : Microsoft
     * username : 杨宇星
     * photo_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460429045_790216695.png
     * photo_thumbnail_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460429045_790216695.png
     * created_time : 2016-04-14 12:36:22
     * content : 我就看看可不可以评论
     */

    public static class ReMark {
        public String stunum;
        public String nickname;
        public String username;
        public String photo_src;
        public String photo_thumbnail_src;
        public String created_time;
        public String content;
    }
}
