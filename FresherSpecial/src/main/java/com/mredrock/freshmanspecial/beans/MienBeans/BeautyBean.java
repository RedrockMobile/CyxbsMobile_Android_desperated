package com.mredrock.freshmanspecial.beans.MienBeans;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/7.
 */

public class BeautyBean {

    /**
     * Status : 200
     * Info : success
     * Version : 1.0
     * Data : [{"title":"春日落樱","content":"樱花烂漫红陌上，花开花落皆是景\r\n春风暖，吹绿了重邮，朵朵樱花饱吮着雨露的滋润，与绿叶相衬，在春风的轻抚中缓缓舒展。让我们一起漫步在三月的重邮，去欣赏那些美丽的存在。\r\n","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_mzcy/spring.PNG"},{"title":"立夏蝉鸣","content":"蝉鸣午后雨红莲，日落黄昏文峰塔\r\n伴着盛夏的阳光与阵阵蝉鸣，我知道，那个夏天，就像青春一样回不来。 所以，你好，再见，这是毕业的季节。\r\n","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_mzcy/summer.PNG"},{"title":"杏染秋色","content":"秋风瑟瑟杏叶飞，雾绕重邮山麓间\r\n秋天，银杏叶由翠绿变得金黄，飒飒秋风，叶片如黄蝶飞舞，落到地上铺成金黄色的地毯。二教，老图，校园，秋意浓。\r\n","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_mzcy/autumn.PNG"},{"title":"冬至慕雪","content":"最美纷纷南山雪，邮苑万物裹银装\r\n南山的冬天，没有草木枯荣的景象，绿叶在寒风中展示着一种生命力，轻柔的雪花悠悠地飘落，形成白绿相映的美景。\r\n","url":"http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_mzcy/winter.PNG"}]
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
         * title : 春日落樱
         * content : 樱花烂漫红陌上，花开花落皆是景
         春风暖，吹绿了重邮，朵朵樱花饱吮着雨露的滋润，与绿叶相衬，在春风的轻抚中缓缓舒展。让我们一起漫步在三月的重邮，去欣赏那些美丽的存在。

         * url : http://hongyan.cqupt.edu.cn/welcome/2017/photoForWelcome/cyfc_mzcy/spring.PNG
         */

        private String title;
        private String content;
        private String url;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
