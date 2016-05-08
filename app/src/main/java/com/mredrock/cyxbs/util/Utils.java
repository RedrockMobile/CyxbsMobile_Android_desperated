package com.mredrock.cyxbs.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Stormouble on 16/4/16.
 */
public class Utils {

    public static <T> T checkNotNullWithException(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> boolean checkNotNull(T reference) {
        return reference != null;
    }

    public static boolean checkNotNullAndNotEmpty(Collection<?> reference) {
        return reference != null && !reference.isEmpty();
    }

    public static boolean checkNotNullAndNotEmpty(Map<?, ?> reference) {
        return reference != null && !reference.isEmpty();
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static File getInternalCacheDir(Context context) {
        if (isSDCardMounted()) {
            return context.getCacheDir();
        } else {
            return null;
        }
    }

    public static File getExternalCacheDir(Context context) {
        if (isSDCardMounted()) {
            return context.getExternalCacheDir();
        } else {
            return null;
        }
    }

    public static File getDiskCacheDir(Context context) {
        File diskCacheDir = null;
        if (isSDCardMounted() || !Environment.isExternalStorageRemovable()) {
            diskCacheDir = context.getExternalCacheDir();
        } else {
            diskCacheDir = context.getCacheDir();
        }
        return diskCacheDir;
    }
}
