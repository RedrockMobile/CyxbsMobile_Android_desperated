package com.mredrock.cyxbsmobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mredrock.cyxbsmobile.config.Const;

import java.util.ArrayList;
import java.util.List;

public class DBEditHelper {

    public static final String SELECT_ALL_FROM_COURSE_BY_STU_ID =
            "SELECT * FROM course WHERE stu_id = ?";
    public static final String INSERT_COURSE_WITH_STU_ID =
            "INSERT INTO course(stu_id, all_course) VALUES (?, ?)";
    public static final String UPDATE_COURSE_BY_STU_ID =
            "UPDATE course SET all_course = ? WHERE stu_id = ?";
    public static final String DELETE_COURSE_BY_STU_ID =
            "DELETE FROM course WHERE stu_id = ?";
    public static final String CLEAN_COURSES =
            "DELETE FROM course";
    public static final int TYPE_COURSE_DB = 0;

    private DBEditHelper() {
        throw new UnsupportedOperationException("DBEditHelper can't be instantiated");
    }

    public static List<String> selectCourse(Context context, String stuId) {
        SQLiteDatabase db = openDB(context, TYPE_COURSE_DB);
        List<String> courseJsonList = new ArrayList<>();
        if (db != null) {
            Cursor cursor = db.rawQuery(SELECT_ALL_FROM_COURSE_BY_STU_ID, new String[]{stuId});
            while (cursor.moveToNext()) {
                courseJsonList.add(cursor.getString(cursor.getColumnIndex("all_course")));
            }
            cursor.close();
        }
        closeDB(db);
        return courseJsonList;
    }

    public static boolean insertCourse(Context context, String stuId, String courses) {
        SQLiteDatabase db = openDB(context, TYPE_COURSE_DB);
        try {
            if (db != null) {
                db.execSQL(INSERT_COURSE_WITH_STU_ID, new String[]{stuId, courses});
                closeDB(db);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDB(db);
        return false;
    }

    public static boolean updateCourse(Context context, String stuId, String courses) {
        SQLiteDatabase db = openDB(context, TYPE_COURSE_DB);
        try {
            if (db != null) {
                db.execSQL(UPDATE_COURSE_BY_STU_ID, new String[]{courses, stuId});
                closeDB(db);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDB(db);
        return false;
    }

    public static boolean addCourse(Context context, String stuId, String courses) {
        if (selectCourse(context, stuId).size() > 0) {
            return updateCourse(context, stuId, courses);
        } else {
            return insertCourse(context, stuId, courses);
        }
    }

    public static boolean deleteCourse(Context context, String stuId) {
        SQLiteDatabase db = openDB(context, TYPE_COURSE_DB);
        try {
            if (db != null) {
                db.execSQL(DELETE_COURSE_BY_STU_ID, new String[]{stuId});
                closeDB(db);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDB(db);
        return false;
    }

    public static boolean cleanCourse(Context context) {
        SQLiteDatabase db = openDB(context, TYPE_COURSE_DB);
        try {
            if (db != null) {
                db.execSQL(CLEAN_COURSES);
                closeDB(db);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDB(db);
        return false;
    }

    private static SQLiteDatabase openDB(Context context, int dbType) {
        DBOpenHelper dbOpenHelper;
        switch (dbType) {
            case TYPE_COURSE_DB:
                dbOpenHelper = new DBOpenHelper(context, Const.COURSE_DB_NAME, null, Const.COURSE_DB_VERSION);
                return dbOpenHelper.getWritableDatabase();
        }
        return null;
    }

    private static void closeDB(SQLiteDatabase db) {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
