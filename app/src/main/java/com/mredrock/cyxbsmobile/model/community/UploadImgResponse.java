package com.mredrock.cyxbsmobile.model.community;

/**
 * Created by mathiasluo on 16-4-12.
 */
public class UploadImgResponse {

    /**
     * state : 200
     * info : success
     * data : {"stunum":"2014212041","date":"2016-04-12 18:36:34","photosrc":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460457394_1197497258.jpg","thumbnail_src":"http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460457394_1197497258.jpg","state":1}
     */

    private int state;
    private String info;
    /**
     * stunum : 2014212041
     * date : 2016-04-12 18:36:34
     * photosrc : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/1460457394_1197497258.jpg
     * thumbnail_src : http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/1460457394_1197497258.jpg
     * state : 1
     */

    private DataBean data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
        private String stunum;
        private String date;
        private String photosrc;
        private String thumbnail_src;
        private int state;

        public String getStunum() {
            return stunum;
        }

        public void setStunum(String stunum) {
            this.stunum = stunum;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPhotosrc() {
            return photosrc;
        }

        public void setPhotosrc(String photosrc) {
            this.photosrc = photosrc;
        }

        public String getThumbnail_src() {
            return thumbnail_src;
        }

        public void setThumbnail_src(String thumbnail_src) {
            this.thumbnail_src = thumbnail_src;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
