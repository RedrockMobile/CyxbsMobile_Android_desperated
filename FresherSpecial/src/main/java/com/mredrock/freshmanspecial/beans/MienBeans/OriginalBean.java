package com.mredrock.freshmanspecial.beans.MienBeans;

import com.mredrock.freshmanspecial.beans.RecyclerBean;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/9.
 */

public class OriginalBean extends RecyclerBean {

    /**
     * Status : 200
     * Info : success
     * Version : 1.0
     * Data : [{"name":"看见重邮","url":"http://v.youku.com/v_show/id_XNzExODM3Njk2.html?from=y1.2-1-95.3.12-2.1-1-1-11-0","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/看见重邮.png"},{"name":"时间都去哪了","url":"http://v.youku.com/v_show/id_XMTI2NjE0MDcwNA==.html?from=s1.8-1-1.2","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/时间都去哪了.png"},{"name":"舌尖上的重邮第一集","url":"http://v.youku.com/v_show/id_XNzA0MDc2ODA0.html?from=s1.8-1-1.1","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/舌尖上的重邮.jpg"},{"name":"舌尖上的重邮第二集","url":"http://v.youku.com/v_show/id_XNDAzNzQ1MjA4.html?from=s1.8-1-1.1","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/舌尖上的重邮.jpg"},{"name":"舌尖上的重邮第三集","url":"http://v.youku.com/v_show/id_XNDMyNTIzMzAw.html?from=s1.8-1-1.1","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/舌尖上的重邮第三集.jpg"},{"name":"舌尖上的重邮第二季","url":"http://v.youku.com/v_show/id_XNzIxODU1OTYw.html?from=s1.8-1-1.1","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/舌尖上的重邮第二季.png"},{"name":"2015重庆邮电大学宣传片","url":"http://v.youku.com/v_show/id_XMTc1OTA2MzUzMg==.html?spm=a2h0k.8191407.0.0&from=s1.8-1-1.2","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/重庆邮电大学宣传片.jpg"},{"name":"2016红岩网校工作站招新视频","url":"http://v.youku.com/v_show/id_XMTcxOTM2MTc4MA==.html?spm=a2h0j.8191423.module_basic_relation.5~5!2~5~5!7~5~5~A","cover":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/2016红岩网校招新宣传.png"}]
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
         * name : 看见重邮
         * url : http://v.youku.com/v_show/id_XNzExODM3Njk2.html?from=y1.2-1-95.3.12-2.1-1-1-11-0
         * cover : http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yccy/看见重邮.png
         */

        private String name;
        private String url;
        private String cover;
        private String time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
