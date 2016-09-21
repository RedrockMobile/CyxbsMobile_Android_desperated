package com.mredrock.cyxbs.config;

/**
 * Created by david on 15/4/12.
 */
public class Config {
    public static final String DIR = "/cyxbs";
    public static final String DIR_PHOTO = "/cyxbs/photo";
    public static final String DIR_FILE = "/cyxbs/file";

    public static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    public static final String PREF_USER_LOGIN_ALREADY = "login_already";
    public static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    public static final String SP_KEY_USER = "user";

    public final static String dataFilePath = android.os.Environment.getExternalStorageDirectory() +"/"+"Android/data/com.mredrock.cyxbs/";
    public final static String updateFilePath = android.os.Environment.getExternalStorageDirectory() +"/"+"download/";
    public final static String updateFilename = "com.mredrock.cyxbs.apk";
}
