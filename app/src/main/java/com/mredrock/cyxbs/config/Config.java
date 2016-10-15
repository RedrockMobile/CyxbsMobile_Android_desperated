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

    public final static String dataFilePath = android.os.Environment.getExternalStorageDirectory() +"/"+"Android/data/com.mredrock.cyxbs/";
    public final static String updateFilePath = android.os.Environment.getExternalStorageDirectory() +"/"+"download/";
    public final static String updateFilename = "com.mredrock.cyxbs.apk";

    public static final String APP_WIDGET_CACHE_FILE_NAME = "AppWidgetCache.json";

    /**
     * SharedPreferences key for encrypt version of user
     *
     * use by {@link com.mredrock.cyxbs.network.encrypt.UserInfoEncryption} for the current encrypt version,
     * you can update the encrypt method in the future and keep compatibility
     * @see com.mredrock.cyxbs.network.encrypt.UserInfoEncryption#onUpdate(int, int)
     */
    public static final String SP_KEY_ENCRYPT_VERSION_USER = "encrypt_version_user";

    /**
     * SharedPreferences value for encrypt version of user
     *
     * use by {@link com.mredrock.cyxbs.network.encrypt.UserInfoEncryption} for the current encrypt version,
     * you can update the encrypt method in the future and keep compatibility
     * @see com.mredrock.cyxbs.network.encrypt.UserInfoEncryption#onUpdate(int, int)
     */
    public static final int USER_INFO_ENCRYPT_VERSION = 1;
}
