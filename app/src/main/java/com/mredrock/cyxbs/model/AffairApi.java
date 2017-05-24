package com.mredrock.cyxbs.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

   public static class AffairItem {
       /**
        * id : 14791202347972187
        * time : 5
        * title : 交作业
        * content : 今天记得交作业
        * date : [{"class":2,"day":5,"week":[3]}]
       */

       private String id;
       private int time;
       private String title;
       private String content;
       private List<DateBean> date = new ArrayList<>();

       public static AffairItem objectFromData(String str) {

           return new Gson().fromJson(str, AffairItem.class);
       }

       public static AffairItem objectFromData(String str, String key) {

           try {
               JSONObject jsonObject = new JSONObject(str);

               return new Gson().fromJson(jsonObject.getString(str), AffairItem.class);
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return null;
       }

       public static List<AffairItem> arrayAffairItemFromData(String str) {

           Type listType = new TypeToken<ArrayList<AffairItem>>() {
           }.getType();

           return new Gson().fromJson(str, listType);
       }

       public static List<AffairItem> arrayAffairItemFromData(String str, String key) {

           try {
               JSONObject jsonObject = new JSONObject(str);
               Type listType = new TypeToken<ArrayList<AffairItem>>() {
               }.getType();

               return new Gson().fromJson(jsonObject.getString(str), listType);

           } catch (JSONException e) {
               e.printStackTrace();
           }

           return new ArrayList();


       }

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
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

       public List<DateBean> getDate() {
           return date;
       }

       public void setDate(List<DateBean> date) {
           this.date = date;
       }

       public static class DateBean {
           /**
            * class : 2
            * day : 5
            * week : [3]
            */

           @SerializedName("class")
           private int classX;
           private int day;
           private List<Integer> week = new ArrayList<>();

           public static DateBean objectFromData(String str) {

               return new Gson().fromJson(str, DateBean.class);
           }

           public static DateBean objectFromData(String str, String key) {

               try {
                   JSONObject jsonObject = new JSONObject(str);

                   return new Gson().fromJson(jsonObject.getString(str), DateBean.class);
               } catch (JSONException e) {
                   e.printStackTrace();
               }

               return null;
           }

           public static List<DateBean> arrayDateBeanFromData(String str) {

               Type listType = new TypeToken<ArrayList<DateBean>>() {
               }.getType();

               return new Gson().fromJson(str, listType);
           }

           public static List<DateBean> arrayDateBeanFromData(String str, String key) {

               try {
                   JSONObject jsonObject = new JSONObject(str);
                   Type listType = new TypeToken<ArrayList<DateBean>>() {
                   }.getType();

                   return new Gson().fromJson(jsonObject.getString(str), listType);

               } catch (JSONException e) {
                   e.printStackTrace();
               }

               return new ArrayList();


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

           public List<Integer> getWeek() {
               return week;
           }

           public void setWeek(List<Integer> week) {
               this.week = week;
           }
       }


       /**
        * status : 200
        * info : success
        * term : 201620171
        * stuNum : 2015211876
        * data : [{"id":14791202347972187,"time":5,"title":"交作业","content":"今天记得交作业","date":[{"class":2,"day":5,"week":[3]}]},{"id":14791202647657801,"time":5,"title":"交作业","content":"今天记得交作业","date":[{"class":2,"day":5,"week":[1,2,3]}]},{"id":14791202715842802,"time":5,"title":"交作业","content":"今天记得交作业","date":[{"class":2,"day":5,"week":[1]}]}]
        */



   }
}
