package com.mredrock.cyxbsmobile.model;

/**
 * Created by cc on 16/3/19.
 */
public class Cast {
    public String id;
    public String name;
    public String alt;
    public Avatars avatars;

    @Override
    public String toString() {
        return "Cast{" +
                "alt='" + alt + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatars=" + avatars +
                '}';
    }
}
