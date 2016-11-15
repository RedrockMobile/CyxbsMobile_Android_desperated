package com.mredrock.cyxbs.util.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.model.Affair;
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



    public boolean insert(String uid,String stuNum,String json) {
        open();
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO affair(uid,stuNum,isUpload,data) VALUES ('" + uid
                    +"','"+stuNum +"','"+false+"','"+json+"');");
            db.setTransactionSuccessful();  //设置事务成功完成

        } catch (Exception e){
            return false;
        } finally {
            db.endTransaction();    //结束事务
        }
        return true;
    }


    public void updateIsUpLoad(String uid) {
        ContentValues cv = new ContentValues();
        cv.put("isUpload", true);
        db.update("affair", cv, "uid = ?", new String[]{uid});
    }

    public Observable deleteAffair(String uid) {
        open();
        return Observable.create((subscriber -> {
            db.delete("affair", "uid = ?", new String[]{uid});
            subscriber.onNext(null);
            subscriber.onCompleted();
        }));
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
                Affair affair =  gson.fromJson(a,Affair.class);
                if (week == 0)
                    courses.add(affair);
                else if (affair.week.contains(week))
                    courses.add(affair);
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
