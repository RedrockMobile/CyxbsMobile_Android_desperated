package com.mredrock.cyxbs.model;

import java.util.List;

/**
 * Created by glossimar_wan on 2017/9/18.
 */

public class VolunteerTime {

    /**
     * status : 200
     * info : success
     * data : {"uid":"4783046","hours":12,"record":[{"title":"让校园多一份整洁","content":"做清洁","address":"重庆邮电大学","start_time":"2017-03-31","hours":"4.0","score":0},{"title":"让校园多一点绿色","content":"植树","address":"重庆邮电大学","start_time":"2017-03-17","hours":"4.0","score":0},{"title":"清除牛皮癣","content":"清除牛皮癣","address":"重庆邮电大学","start_time":"2017-03-07","hours":"1.0","score":0},{"title":"系统补录服务时长","content":null,"address":null,"start_time":"2017-01-01","hours":"3.0","score":0}]}
     */

    private int status;
    private String info;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 4783046
         * hours : 12
         * record : [{"title":"让校园多一份整洁","content":"做清洁","address":"重庆邮电大学","start_time":"2017-03-31","hours":"4.0","score":0},{"title":"让校园多一点绿色","content":"植树","address":"重庆邮电大学","start_time":"2017-03-17","hours":"4.0","score":0},{"title":"清除牛皮癣","content":"清除牛皮癣","address":"重庆邮电大学","start_time":"2017-03-07","hours":"1.0","score":0},{"title":"系统补录服务时长","content":null,"address":null,"start_time":"2017-01-01","hours":"3.0","score":0}]
         */

        private String uid;
        private int hours;
        private List<RecordBean> record;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public List<RecordBean> getRecord() {
            return record;
        }

        public void setRecord(List<RecordBean> record) {
            this.record = record;
        }

        public static class RecordBean {
            /**
             * title : 让校园多一份整洁
             * content : 做清洁
             * address : 重庆邮电大学
             * start_time : 2017-03-31
             * hours : 4.0
             * score : 0
             */

            private String title;
            private String content;
            private String address;
            private String start_time;
            private String hours;
            private int score;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getHours() {
                return hours;
            }

            public void setHours(String hours) {
                this.hours = hours;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }
        }
    }
}
