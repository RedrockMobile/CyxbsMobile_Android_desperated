package com.mredrock.freshmanspecial.beans;

import java.util.List;

/**
 * Created by glossimar on 2017/8/11.
 */

public class CampusBean {
    private  int Status;
    private String  Info;
    private double Version;
    private List<CampusDataBean> Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public double getVersion() {
        return Version;
    }

    public void setVersion(double version) {
        Version = version;
    }

    public List<CampusDataBean> getCampusDataBeanList() {
        return Data;
    }

    public void setCampusDataBeanList(List<CampusDataBean> Data) {
        this.Data = Data;
    }

    static public class CampusDataBean {
        private String title;
        private String content;
        private List<String> url;

        public String getDormitoryNumber() {
            return dormitoryNumber;
        }

        public void setDormitoryNumber(String dormitoryNumber) {
            this.dormitoryNumber = dormitoryNumber;
        }

        private String dormitoryNumber = "";

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

        public List<String> getUrl() {
            return url;
        }

        public void setUrl(List<String> url) {
            this.url = url;
        }
    }
}
