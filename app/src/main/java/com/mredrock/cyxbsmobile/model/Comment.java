package com.mredrock.cyxbsmobile.model;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class Comment {


    /**
     * status : 200
     * data : [{"user_id":"2013211123","user_name":"用户名","user_head":"用户头像地址","time":"时间戳","content":"内容"}]
     */

    private int status;
    /**
     * user_id : 2013211123
     * user_name : 用户名
     * user_head : 用户头像地址
     * time : 时间戳
     * content : 内容
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String user_id;
        private String user_name;
        private String user_head;
        private String time;
        private String content;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_head() {
            return user_head;
        }

        public void setUser_head(String user_head) {
            this.user_head = user_head;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
