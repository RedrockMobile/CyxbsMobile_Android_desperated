package com.mredrock.cyxbs.freshman.bean;

import java.util.List;

/*
 by Cynthia at 2018/8/16
 description : 
 */
public class SubjectProportion {
    private String name;
    private List<ArrayBean> array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArrayBean> getArray() {
        return array;
    }

    public void setArray(List<ArrayBean> array) {
        this.array = array;
    }

    public class ArrayBean {
        /**
         * below_amount : 138
         * id : 1
         * subject_name : 大学物理B
         */

        private int below_amount;
        private int id;
        private String subject_name;

        public int getBelow_amount() {
            return below_amount;
        }

        public void setBelow_amount(int below_amount) {
            this.below_amount = below_amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSubject_name() {
            return subject_name;
        }

        public void setSubject_name(String subject_name) {
            this.subject_name = subject_name;
        }
    }
}
