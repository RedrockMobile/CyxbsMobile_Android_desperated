package com.mredrock.cyxbs.model.social;

/**
 * Created by mathiasluo on 16-4-11.
 */
public class Image {
    public static final int TYPE_ADD = 1;
    public static final int TYPE_NORMAL = 2;

    public String url;
    public int type;

    public Image(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Image setType(int type) {
        this.type = type;
        return this;
    }


}
