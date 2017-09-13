package com.mredrock.freshmanspecial.beans.MienBeans;

import com.mredrock.freshmanspecial.beans.RecyclerBean;

/**
 * Created by zxzhu on 2017/8/10.
 */

public class GroupBean extends RecyclerBean {

    private String title,content;

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }
}
