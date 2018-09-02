package com.mredrock.cyxbs.freshman.bean;

import java.util.List;

/**
 * 接口1的实体类
 */
public class Entity {
    /**
     * index : 学生食堂
     * amount : 6
     * name : ["千喜鹤","中心食堂","大学城食堂","延生","大西北","红高粱"]
     */

    private String index;
    private int amount;
    private List<String> name;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

}
