package com.excitingboat.freshmanspecial.model.bean;

import java.util.List;

/**
 * Created by PinkD on 2016/8/12.
 * Sight
 */
public class Sight {
    private String id;
    private String name;
    private List<Photo> photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Sight{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photo=" + photo +
                '}';
    }
}
