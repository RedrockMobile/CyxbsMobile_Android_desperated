package com.mredrock.cyxbsmobile;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by cc on 16/3/18.
 */
public class APP extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
