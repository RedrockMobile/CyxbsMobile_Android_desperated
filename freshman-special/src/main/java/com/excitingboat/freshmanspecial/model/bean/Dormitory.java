package com.excitingboat.freshmanspecial.model.bean;

import java.util.List;

/**
 * Created by PinkD on 2016/8/11.
 * Dormitory
 */
public class Dormitory {
    private String id;
    private String introduction;
    private List<Photo> photo;

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Dormitory{" +
                "data=" + photo +
                ", photo='" + id + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
