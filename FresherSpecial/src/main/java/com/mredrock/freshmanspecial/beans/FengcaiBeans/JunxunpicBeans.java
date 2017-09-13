package com.mredrock.freshmanspecial.beans.FengcaiBeans;

import java.util.List;

/**
 * Created by zia on 17-8-11.
 */

public class JunxunpicBeans {

    /**
     * Status : 200
     * Info : success
     * Version : 1.0
     * Data : {"title":["重邮军魂","整齐划一","拉练活动","拨挡冲拳","射击训练","分列式风采"],"url":["http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/1.重邮军魂.jpg","http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/2.整齐划一.jpg","http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/3.拉练活动.jpg","http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/4.拨挡冲拳.jpg","http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/5.射击训练.jpg","http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/6.分列式风采.jpg"]}
     */

    private int Status;
    private String Info;
    private String Version;
    private DataBean Data;

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private List<String> title;
        private List<String> url;

        public List<String> getTitle() {
            return title;
        }

        public void setTitle(List<String> title) {
            this.title = title;
        }

        public List<String> getUrl() {
            return url;
        }

        public void setUrl(List<String> url) {
            this.url = url;
        }
    }
}
