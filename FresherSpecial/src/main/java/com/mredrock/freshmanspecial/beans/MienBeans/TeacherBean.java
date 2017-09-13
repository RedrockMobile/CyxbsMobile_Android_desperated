package com.mredrock.freshmanspecial.beans.MienBeans;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/12.
 */

public class TeacherBean {
    /**
     * Status : 200
     * Info : success
     * Version : 1.0
     * Data : [{"name":"彭语良","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/9.彭语良.jpg"},{"name":"闵绪国","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/8.闵绪国.jpg"},{"name":"罗元","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/7.罗元.jpg"},{"name":"蒋青","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/6.蒋青.jpg"},{"name":"胡庆","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/5.胡庆.jpg"},{"name":"高非","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/4.高非.jpg"},{"name":"高冬","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/3.高冬.jpg"},{"name":"付蔚","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/2.付蔚.jpg"},{"name":"朱伟","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/19.朱伟.jpg"},{"name":"周兴茂","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/18.周兴茂.jpg"},{"name":"郑继明","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/17.郑继明.jpg"},{"name":"张琬悦","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/16.张琬悦.jpg"},{"name":"张清华","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/15.张清华.jpg"},{"name":"袁帅","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/14.袁帅.jpg"},{"name":"熊安萍","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/13.熊安萍.jpg"},{"name":"王樱桃","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/12.王樱桃.jpg"},{"name":"王睿","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/11.王睿.jpg"},{"name":"漆晶","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/10.漆晶.jpg"},{"name":"陈褀褀","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/1.陈褀褀.jpg"}]
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
         * name : 彭语良
         * url : http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_yxjs/9.彭语良.jpg
         */

        private String name;
        private String url;

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
    }
}
