package com.mredrock.cyxbsmobile.model;

import java.util.List;

/**
 * Created by cc on 16/3/19.
 */
public class Subject {

    public String id;
    public String alt;
    public String year;
    public String title;
    public String original_title;
    public List<String> genres;
    public List<Cast> casts;
    public List<Cast> directors;
    public Avatars images;

    @Override
    public String toString() {
        return "Subject{" +
                "alt='" + alt + '\'' +
                ", id='" + id + '\'' +
                ", year='" + year + '\'' +
                ", title='" + title + '\'' +
                ", original_title='" + original_title + '\'' +
                ", genres=" + genres +
                ", casts=" + casts +
                ", directors=" + directors +
                ", images=" + images +
                '}';
    }
}
