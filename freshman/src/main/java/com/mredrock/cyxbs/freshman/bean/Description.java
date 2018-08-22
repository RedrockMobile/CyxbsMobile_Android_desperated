package com.mredrock.cyxbs.freshman.bean;

import java.util.List;

public class Description {

    /**
     * 接口7的实体类
     */

    private String index;
    private List<DescribeBean> describe;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<DescribeBean> getDescribe() {
        return describe;
    }

    public void setDescribe(List<DescribeBean> describe) {
        this.describe = describe;
    }

    public static class DescribeBean {

        private String content;
        private int id;
        private String name;
        private String property;
        private boolean isCheck;
        private boolean isDelete;
        private boolean isOpen;

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }

        public void setCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public boolean isDelete() {
            return isDelete;
        }

        public void setDelete(boolean delete) {
            isDelete = delete;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

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
    }
}
