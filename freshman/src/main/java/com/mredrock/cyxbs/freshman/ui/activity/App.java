package com.mredrock.cyxbs.freshman.ui.activity;

import android.content.Context;

public class App {
    private static Context appContext;

    public static void setAppContext(Context appContext) {
        App.appContext = appContext;
    }

    public static Context getContext() {
        return appContext;
    }
}
