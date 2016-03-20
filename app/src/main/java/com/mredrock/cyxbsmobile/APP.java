package com.mredrock.cyxbsmobile;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;

/**
 * Created by cc on 16/3/18.
 */
public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
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
