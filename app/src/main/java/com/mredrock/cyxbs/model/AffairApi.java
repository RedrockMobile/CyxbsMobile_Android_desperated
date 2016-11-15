package com.mredrock.cyxbs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ：AceMurder
 * Created on ：2016/11/14
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class AffairApi<T> extends RedrockApiWrapper<T> {


    public String term;
    public String stuNum;

   public static class AffairItem{
       private String id;
       @SerializedName("class")
       private int classX;
       private int day;
       private int time;
       private String title;
       private String content;
       private String state;
       private List<Integer> week;

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }

       public int getClassX() {
           return classX;
       }

       public void setClassX(int classX) {
           this.classX = classX;
       }

       public int getDay() {
           return day;
       }

       public void setDay(int day) {
           this.day = day;
       }

       public int getTime() {
           return time;
       }

       public void setTime(int time) {
           this.time = time;
       }

       public String getTitle() {
           return title;
       }

       public void setTitle(String title) {
           this.title = title;
       }

       public String getContent() {
           return content;
       }

       public void setContent(String content) {
           this.content = content;
       }

       public String getState() {
           return state;
       }

       public void setState(String state) {
           this.state = state;
       }

       public List<Integer> getWeek() {
           return week;
       }

       public void setWeek(List<Integer> week) {
           this.week = week;
       }
   }
}
