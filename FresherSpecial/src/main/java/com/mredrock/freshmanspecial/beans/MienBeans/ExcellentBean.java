package com.mredrock.freshmanspecial.beans.MienBeans;

import android.graphics.drawable.Drawable;

import com.mredrock.freshmanspecial.beans.RecyclerBean;

/**
 * Created by zxzhu on 2017/8/7.
 */

public class ExcellentBean extends RecyclerBean {
    private Drawable img;
    private String name, content, url_img;

    public void setImg(Drawable img){
        this.img = img;
    }

    public Drawable getImg(){
        return img;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setUrl_img(String url_img){
        this.url_img = url_img;
    }

    public String getUrl_img(){
        return url_img;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }
}
