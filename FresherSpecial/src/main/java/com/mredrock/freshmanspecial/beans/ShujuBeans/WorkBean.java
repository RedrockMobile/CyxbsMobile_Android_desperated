package com.mredrock.freshmanspecial.beans.ShujuBeans;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/6.
 */

public class WorkBean {

    /**
     * Status : 200
     * Info : 成功
     * Version : 1.0
     * Data : [{"college":"生物信息学院","ratio":"0.9724"},{"college":"传媒艺术学院","ratio":"0.9647"},{"college":"先进制造工程学院","ratio":"0.9661"},{"college":"计算机科学与技术学院","ratio":"0.9613"},{"college":"理学院","ratio":"0.9593"},{"college":"体育学院","ratio":"0.9559"},{"college":"光电工程学院/重庆国际半导体学院","ratio":"0.9553"},{"college":"软件工程学院","ratio":"0.9371"},{"college":"经济管理学院","ratio":"0.9231"},{"college":"通信与信息工程学院","ratio":"0.9231"},{"college":"自动化学院","ratio":"0.9104"},{"college":"外国语学院","ratio":"0.8611"},{"college":"法学院","ratio":"0.7222"}]
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
         * college : 生物信息学院
         * ratio : 0.9724
         */

        private String college;
        private String ratio;

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
    }
}
