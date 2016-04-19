package com.mredrock.cyxbsmobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_COURSE_TABLE_V1 =
            "CREATE TABLE course" +
                    "(" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "    stu_id TEXT NOT NULL," +
                    "    all_course TEXT NOT NULL" +
                    ")";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE_TABLE_V1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
