package com.mredrock.cyxbs.util.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;



/**
 * Created by ：AceMurder
 * Created on ：2016/11/05
 * Created for : CyxbsMobile_Android.
 * Enjoy it !!!
 */
public class AffairDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "affair.db";
    private static final String TABLE_NAME = "affair";

    private final String CREATE_TABLE = "CREATE TABLE affair (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "uid VARCHAR," +
            "stuNum VARCHAR," +
            "isUpload boolean,"+
            "data TEXT"+
            ")";

    public AffairDatabaseHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context,DATABASE_NAME, null, 1);
    }

    public AffairDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
