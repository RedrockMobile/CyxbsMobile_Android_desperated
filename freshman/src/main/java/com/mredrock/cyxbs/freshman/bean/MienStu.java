package com.mredrock.cyxbs.freshman.bean;

import java.util.List;

/*
接口9
 */
public class MienStu {

    /**
     * index :
     * array : [{"id":"","name":""," content":"","picture":["url1","url2","url3"]},{"id":"","name":"","content":"","picture":["url1","url2","url3"]}]
     */

    private String index;
    private List<ArrayBean> array;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<ArrayBean> getArray() {
        return array;
    }

    public void setArray(List<ArrayBean> array) {
        this.array = array;
    }

    public static class ArrayBean {
        /**
         * id :
         * name :
         * content :
         * picture : ["url1","url2","url3"]
         */

        private int id;
        private String name;
        private String content;
        private List<String> picture;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }
    }
}
