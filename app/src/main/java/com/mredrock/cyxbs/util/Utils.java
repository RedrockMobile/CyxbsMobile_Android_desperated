package com.mredrock.cyxbs.util;

import android.os.Process;

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

    public static class WorkerThread extends Thread {

        public WorkerThread(Runnable runnable) {
            super(runnable);
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            super.run();
        }
    }
}
