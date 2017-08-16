package com.mredrock.freshmanspecial.beans.ShujuBeans;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/6.
 */

public class FailBean {
    /**
     * Status : 200
     * Info : 成功
     * Version : 1.0
     * Data : [{"course":"高等数学","ratio":"0.5","college":"通信与信息工程学院","major":"通信与信息类"},{"course":"大学物理","ratio":"0.5","college":"通信与信息工程学院","major":"通信与信息类"},{"course":"大学英语","ratio":"0.5","college":"通信与信息工程学院","major":"通信与信息类"}]
     */

    private int Status;
    private String Info;
    private String Version;
    private List<DataBean> Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * course : 高等数学
         * ratio : 0.5
         * college : 通信与信息工程学院
         * major : 通信与信息类
         */

        private String course;
        private String ratio;
        private String college;
        private String major;

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }
    }
}
