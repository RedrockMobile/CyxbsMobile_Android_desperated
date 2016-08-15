package com.excitingboat.freshmanspecial.model.bean;

/**
 * Created by PinkD on 2016/8/11.
 * Photo
 */
public class Photo {
    private String id;
    private String photo_src;
    private String photo_thumbnail_src;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto_src() {
        return photo_src;
    }

    public void setPhoto_src(String photo_src) {
        this.photo_src = photo_src;
    }

    public String getPhoto_thumbnail_src() {
        return photo_thumbnail_src;
    }

    public void setPhoto_thumbnail_src(String photo_thumbnail_src) {
        this.photo_thumbnail_src = photo_thumbnail_src;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", photo_src='" + photo_src + '\'' +
                ", photo_thumbnail_src='" + photo_thumbnail_src + '\'' +
                '}';
    }
}
