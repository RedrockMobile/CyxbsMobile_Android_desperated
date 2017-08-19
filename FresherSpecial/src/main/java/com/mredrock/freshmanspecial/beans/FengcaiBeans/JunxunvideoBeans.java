package com.mredrock.freshmanspecial.beans.FengcaiBeans;

import java.util.List;

/**
 * Created by zia on 17-8-11.
 */

public class JunxunvideoBeans {

    /**
     * Status : 200
     * Info : success
     * Version : 1.0
     * Data : [{"title":"重庆邮电大学2016级学生军训回顾","url":"https://v.qq.com/x/page/f0526oi2zyx.html","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/cover/重庆邮电大学2016级学生军训回顾.png"},{"title":"重庆邮电大学2016级学生军训纪实","url":"https://v.qq.com/x/page/p0522eqzqzz.html","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/cover/重庆邮电大学2016级学生军训纪实.png"}]
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
         * title : 重庆邮电大学2016级学生军训回顾
         * url : https://v.qq.com/x/page/f0526oi2zyx.html
         * cover : http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/jxfc/cover/重庆邮电大学2016级学生军训回顾.png
         */

        private String title;
        private String url;
        private String cover;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
