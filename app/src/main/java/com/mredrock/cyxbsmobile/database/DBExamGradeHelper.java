package com.mredrock.cyxbsmobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.Course;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/22 18:57.
 */
public class DBExamGradeHelper {

    private static final String SELECT_ALL_FROM_GRADE_BY_STU_ID =
            "select * from grade where stu_id = ?";
    private static final String INSERT_GRADE_WITH_STU_ID =
            "insert into grade (stu_id,all_grade) values (?,?)";
    private static final String UPDATE_GRADE_WITH_STU_ID =
            "update grade set all_grade = ? where stu_id = ?";
    private static final String SELECT_ALL_FROM_EXAM_BY_STU_ID =
            "select * from exam where stu_id = ? and type = ?";
    private static final String INSERT_EXAM_WITH_STU_ID =
            "insert into exam (stu_id,type,all_exam) values (?,?,?)";
    private static final String UPDATE_EXAM_WITH_STU_ID =
            "update exam set all_exam = ? where stu_id = ? and type = ?";


    private DBExamGradeHelper() {
        throw new UnsupportedOperationException(
                "DBExamGradeHelper can't be instantiated");
    }

    public static List<String> selectGrade(Context context,String stuId){
        SQLiteDatabase db = openDB(context);
        List<String> gradeJsonList = new ArrayList<>();
        if(db != null){
            Cursor cursor = db.rawQuery(SELECT_ALL_FROM_GRADE_BY_STU_ID,new
                    String[]{stuId});
            while (cursor.moveToNext()){
                gradeJsonList.add(cursor.getString(cursor.getColumnIndex("all_grade")));
            }
            cursor.close();
        }
        closeDB(db);
        return gradeJsonList;
    }

    public static boolean insertGrade(Context context,String stuId,String
            grade){
        SQLiteDatabase db = openDB(context);
        if(db != null){
            db.execSQL(INSERT_GRADE_WITH_STU_ID,new String[]{stuId,grade});
            closeDB(db);
            return true;
        }
        closeDB(db);
        return false;
    }

    public static boolean updateGrade(Context context,String stuId,String
            grade){
        SQLiteDatabase db = openDB(context);
        if(db != null){
            db.execSQL(UPDATE_GRADE_WITH_STU_ID,new String[]{grade,stuId});
            closeDB(db);
            return true;
        }
        closeDB(db);
        return false;
    }

    public static void addGrade(final Context context,final String stuId, final
    String grade){
        new Thread(() -> {
            if(selectGrade(context,stuId).size() > 0){
                updateGrade(context,stuId,grade);
            }else {
                insertGrade(context,stuId,grade);
            }
        }).start();
    }

    public static List<String> selectExam(Context context,String stuId,String type){
        SQLiteDatabase db = openDB(context);
        List<String> examJsonList = new ArrayList<>();
        if(db != null){
            Cursor cursor = db.rawQuery(SELECT_ALL_FROM_EXAM_BY_STU_ID,new
                    String[]{stuId,type});
            while (cursor.moveToNext()){
                examJsonList.add(cursor.getString(cursor.getColumnIndex("all_exam")));
            }
            cursor.close();
        }
        closeDB(db);
        return examJsonList;
    }

    public static boolean insertExam(Context context,String stuId,String type,String exam){
        SQLiteDatabase db = openDB(context);
        if(db != null){
            db.execSQL(INSERT_EXAM_WITH_STU_ID,new String[]{stuId,type,exam});
            closeDB(db);
            return true;
        }
        closeDB(db);
        return false;
    }

    public static boolean updateExam(Context context,String stuId,String type,String exam){
        SQLiteDatabase db = openDB(context);
        if(db != null){
            db.execSQL(UPDATE_EXAM_WITH_STU_ID,new String[]{exam,stuId,type});
            closeDB(db);
            return true;
        }
        closeDB(db);
        return false;
    }

    public static boolean addExam(Context context,String stuId,String type,String exam){
        if(selectExam(context,stuId,type).size() > 0){
            return insertExam(context,stuId,type,exam);
        }else {
            return updateExam(context,stuId,type,exam);
        }
    }

    private static SQLiteDatabase openDB(Context context) {
        DBOpenHelper dbOpenHelper;
        dbOpenHelper = new DBOpenHelper(context, Const.DB_NAME, null,
                Const.DB_VERSION);
        return dbOpenHelper.getWritableDatabase();
    }

    private static void closeDB(SQLiteDatabase db){
        if(db != null && db.isOpen()){
            db.close();
        }
    }
}
