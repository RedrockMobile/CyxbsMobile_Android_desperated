package com.mredrock.cyxbsmobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPUtils {

    public static final String DEFAULT_FILE_NAME = "share_data";

    private SPUtils() {
        throw new UnsupportedOperationException("SPUtils can't be instantiated");
    }

    public static void set(Context context, String key, Object object) {
        set(context, key, object, DEFAULT_FILE_NAME);
    }

    public static void set(Context context, String key, Object object, String fileName) {

        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        editor.apply();
    }

    public static Object get(Context context, String key) {
        return get(context, key, 0);
    }

    public static Object get(Context context, String key, Object defaultObject) {
        return get(context, key, defaultObject, DEFAULT_FILE_NAME);
    }

    public static Object get(Context context, String key, Object defaultObject, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public static SharedPreferences getPreference(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
}
