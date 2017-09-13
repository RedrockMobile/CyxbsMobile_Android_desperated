package com.mredrock.freshmanspecial.beans.ShujuBeans;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/6.
 */

public class SexBean {
    /**
     * Status : 200
     * Info : 成功
     * Version : 1.0
     * Data : [{"college":"通信与信息工程学院","MenRatio":"0.70170895908856","WomenRatio":"0.29829104091144"},{"college":"光电工程学院","MenRatio":"0.75974025974026","WomenRatio":"0.24025974025974"},{"college":"经济管理学院","MenRatio":"0.47773032336791","WomenRatio":"0.52226967663209"},{"college":"计算机科学与技术学院","MenRatio":"0.78189994378865","WomenRatio":"0.21810005621135"},{"college":"外国语学院","MenRatio":"0.19402985074627","WomenRatio":"0.80597014925373"},{"college":"生物信息学院","MenRatio":"0.58082706766917","WomenRatio":"0.41917293233083"},{"college":"网络空间安全与信息法学院","MenRatio":"0.31578947368421","WomenRatio":"0.68421052631579"},{"college":"自动化学院","MenRatio":"0.81203473945409","WomenRatio":"0.18796526054591"},{"college":"先进制造工程学院","MenRatio":"0.91925465838509","WomenRatio":"0.080745341614907"},{"college":"体育学院","MenRatio":"0.79888268156425","WomenRatio":"0.20111731843575"},{"college":"理学院","MenRatio":"0.70185185185185","WomenRatio":"0.29814814814815"},{"college":"传媒艺术学院","MenRatio":"0.29898648648649","WomenRatio":"0.70101351351351"},{"college":"软件工程学院","MenRatio":"0.84781188765513","WomenRatio":"0.15218811234487"},{"college":"国际半导体学院","MenRatio":"0.83630470016207","WomenRatio":"0.16369529983793"},{"college":"国际学院","MenRatio":"0.75757575757576","WomenRatio":"0.24242424242424"},{"college":"全校","MenRatio":"0.66471399035148","WomenRatio":"0.33528600964852"}]
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
         * college : 通信与信息工程学院
         * MenRatio : 0.70170895908856
         * WomenRatio : 0.29829104091144
         */

        private String college;
        private String MenRatio;
        private String WomenRatio;

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public String getMenRatio() {
            return MenRatio;
        }

        public void setMenRatio(String MenRatio) {
            this.MenRatio = MenRatio;
        }

        public String getWomenRatio() {
            return WomenRatio;
        }

        public void setWomenRatio(String WomenRatio) {
            this.WomenRatio = WomenRatio;
        }
    }
}
