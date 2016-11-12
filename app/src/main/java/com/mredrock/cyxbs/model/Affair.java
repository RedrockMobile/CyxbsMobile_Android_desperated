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
 * Created by zhengyuxuan on 2016/10/11.
 */

public class Affair extends Course {

    /**
     * id :
     * class : 2
     * time : 5
     * title : test
     * content : null
     * state : 1
     * week : ["1"]
     */

   /* public int hash_day;
    public int hash_lesson;
    public int begin_lesson;
    public String day;
    public String lesson;
    public String course;
    public String teacher;
    public String classroom;
    public String rawWeek;
    public String weekModel;
    public int weekBegin;
    public int weekEnd;
    public String type;
    public List<Integer> week;
    public String status;
    // 连上几节
    public int period;*/


    public Affair(){
        day = "";
        lesson = "";
    }

    static class Date{

        /**
         * week :
         * class : 2
         * day : 5
         */

        private String week;
        @SerializedName("class")
        private int hash_lesson;
        private int hash_data;


        public int getHash_data() {
            return hash_data;
        }

        public void setHash_data(int hash_data) {
            this.hash_data = hash_data;
        }

        public int getHash_lesson() {
            return hash_lesson;
        }

        public void setHash_lesson(int hash_lesson) {
            this.hash_lesson = hash_lesson;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }
}
