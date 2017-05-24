package com.mredrock.cyxbs.util.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.AffairApi;
import com.mredrock.cyxbs.model.Course;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


/**
 * Created by ：AceMurder
 * Created on ：2016/11/05
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public enum  DBManager {

    INSTANCE;


    private SQLiteDatabase db;


    DBManager() {
        open();
    }



    public Observable insert(boolean isUpload,String uid,String stuNum,String json) {
        return  Observable.create(subscriber -> {

            db.execSQL("INSERT INTO affair(uid,stuNum,isUpload,data) VALUES ('" + uid
                    + "','" + stuNum + "','" + isUpload + "','" + json + "');");
            subscriber.onNext(null);
            subscriber.onCompleted();
        });
    }



    public Observable insert(boolean isUpload,String uid,String stuNum,String json, boolean delete) {
        return  Observable.create(subscriber -> {
            if (delete)
                db.delete("affair", "uid = ?", new String[]{uid});

            db.execSQL("INSERT INTO affair(uid,stuNum,isUpload,data) VALUES ('" + uid
                    + "','" + stuNum + "','" + isUpload + "','" + json + "');");
            subscriber.onNext(null);
            subscriber.onCompleted();
        });
    }


    public void updateIsUpLoad(String uid) {
        ContentValues cv = new ContentValues();
        cv.put("isUpload", true);
        db.update("affair", cv, "uid = ?", new String[]{uid});
    }

    public Observable upDateAffair(String data,String uid){
        return Observable.create((subscriber -> {
            open();
            ContentValues values = new ContentValues();
            values.put("data",uid);
            db.update("affair",values,"uid = ?",new String[]{uid});
            subscriber.onNext(null);
            subscriber.onCompleted();
        }));
    }

    public Observable deleteAffair(String uid) {
        return Observable.create((subscriber -> {
            open();
            db.delete("affair", "uid = ?", new String[]{uid});
            subscriber.onNext(null);
            subscriber.onCompleted();
        }));
    }

    public Observable<AffairApi.AffairItem> queryItem(String uid){
        return Observable.create(subscriber -> {
            open();
            Cursor c = db.rawQuery("SELECT data FROM affair WHERE uid = "+uid,null);
            String data = null;
            if (c.moveToFirst()){
                data = c.getString(0);
            }
            AffairApi.AffairItem item = new Gson().fromJson(data, AffairApi.AffairItem.class);
            subscriber.onNext(item);
            c.close();

        });
    }

    public Observable<List<Course>> query(String stuNum,int week) {

        return Observable.create(subscriber -> {
            open();
            Cursor c = db.rawQuery("SELECT data FROM affair WHERE stuNum = "+stuNum,null);
            List<String> data = new ArrayList<>();
            List<Course> courses = new ArrayList<Course>();
            while (c.moveToNext()) {
                data.add(c.getString(0));
            }
            c.close();
            Gson gson = new Gson();
            for (String a : data){
                AffairApi.AffairItem  affairItem =  gson.fromJson(a, AffairApi.AffairItem.class);
                for (AffairApi.AffairItem.DateBean dateBean : affairItem.getDate()) {
                    Affair affair = new Affair();
                    affair.time = affairItem.getTime();
                    affair.teacher = affairItem.getContent();
                    affair.courseType = 2;
                    affair.week = dateBean.getWeek();
                    affair.hash_day = dateBean.getDay();
                    affair.begin_lesson = 2 * affair.hash_day + 1;
                    affair.hash_lesson = dateBean.getClassX();
                    affair.period = 2;
                    affair.uid = affairItem.getId();
                    affair.course = affairItem.getTitle();

                    if (week == 0)
                        courses.add(affair);
                    else if (affair.week.contains(week))
                        courses.add(affair);
                }

            }
            subscriber.onNext(courses);
        });
    }

    public Observable<List<Course>> query(String stuNum,int week,boolean isUpload) {

        return Observable.create(subscriber -> {
            open();
            Cursor c = db.rawQuery("SELECT data FROM affair WHERE stuNum = "+stuNum + "AND isUpload = " + isUpload  ,null);
            List<String> data = new ArrayList<>();
            List<Course> courses = new ArrayList<Course>();
            while (c.moveToNext()) {
                data.add(c.getString(0));
            }
            c.close();
            Gson gson = new Gson();
            for (String a : data){
                AffairApi.AffairItem  affairItem =  gson.fromJson(a, AffairApi.AffairItem.class);
                for (AffairApi.AffairItem.DateBean dateBean : affairItem.getDate()) {
                    Affair affair = new Affair();
                    affair.time = affairItem.getTime();
                    affair.teacher = affairItem.getContent();
                    affair.courseType = 2;
                    affair.week = dateBean.getWeek();
                    affair.hash_day = dateBean.getDay();
                    affair.begin_lesson = 2 * affair.hash_day + 1;
                    affair.hash_lesson = dateBean.getClassX();
                    affair.period = 2;
                    affair.uid = affairItem.getId();
                    affair.course = affairItem.getTitle();

                    if (week == 0)
                        courses.add(affair);
                    else if (affair.week.contains(week))
                        courses.add(affair);
                }

            }
            subscriber.onNext(courses);
        });
    }

    public void close() {
        if (db != null)
            db.close();
    }


    private void open(){
        if (db == null || !db.isOpen())
            db = new AffairDatabaseHelper(APP.getContext()).getWritableDatabase();
    }
}
