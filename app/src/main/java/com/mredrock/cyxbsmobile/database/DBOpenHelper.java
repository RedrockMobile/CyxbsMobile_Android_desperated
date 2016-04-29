package com.mredrock.cyxbsmobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mredrock.cyxbsmobile.config.Const;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_COURSE_TABLE_V1 =
            "CREATE TABLE course" +
            "(" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "    stu_id TEXT NOT NULL," +
            "    all_course TEXT NOT NULL" +
            ")";

    public static final String CREATE_GRADE_TEBLE_V1 =
            "CREATE TABLE grade ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "stu_id TEXT NOT NULL,"
                    + "all_grade TEXT NOT NULL)";

    public static final String CREATE_EXAM_TEBLE_V1 =
            "CREATE TABLE exam ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + "stu_id TEXT NOT NULL,"
                    + "type TEXT NOT NULL,"
                    + "all_exam TEXT NOT NULL)";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(getDatabaseName() == Const.DB_NAME){
            db.execSQL(CREATE_GRADE_TEBLE_V1);
            db.execSQL(CREATE_EXAM_TEBLE_V1);
        }else {
            db.execSQL(CREATE_COURSE_TABLE_V1);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
