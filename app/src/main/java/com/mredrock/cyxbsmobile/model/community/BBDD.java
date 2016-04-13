package com.mredrock.cyxbsmobile.model.community;

/**
 * Created by mathiasluo on 16-4-11.
 */
public class BBDD {

    public static final int CYXW = 1;
    public static final int JWZX = 2;
    public static final int XSJZ = 3;
    public static final int XWGG = 4;
    public static final int BBDD = 5;


    private String stuNum;
    private String idNum;
    private int page;
    private int size;
    private int type_id;

    public BBDD(String stuNum, String idNum, int page, int size, int type_id) {
        this.stuNum = stuNum;
        this.idNum = idNum;
        this.page = page;
        this.size = size;
        this.type_id = type_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
