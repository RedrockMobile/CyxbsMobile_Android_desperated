package com.mredrock.cyxbs.util.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.component.multi_image_selector.utils.TimeUtils;
import com.mredrock.cyxbs.model.Affair;

import java.util.ArrayList;
import java.util.List;

import static u.aly.av.S;
import static u.aly.av.d;
import static u.aly.av.j;
import static u.aly.av.n;
import static u.aly.av.t;

/**
 * Created by ：AceMurder
 * Created on ：2016/11/05
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */

public class DBManager {
    private AffairDatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new AffairDatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public boolean insert(String uid,String stuNum,String json) {
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

    public void deleteOldPerson(String uid) {
        db.delete("affair", "uid = ?", new String[]{uid});
    }

    public List<String> query(String stuNum) {

        Cursor c = db.rawQuery("SELECT data FROM affair WHERE stuNum = "+stuNum,null);
        List<String> data = new ArrayList<>();
        while (c.moveToNext()) {
            data.add(c.getString(0));
        }
        c.close();
        return data;
    }

    public void close() {
        if (db != null)
            db.close();
    }
}
