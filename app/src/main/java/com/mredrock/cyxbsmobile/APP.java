package com.mredrock.cyxbsmobile;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.event.LoginEvent;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.util.SPUtils;

import org.greenrobot.eventbus.EventBus;



/**
 * Created by cc on 16/3/18.
 */
public class APP extends Application {
    private static Context context;
    private boolean isNight;
    private static User mUser;

    public static Context getContext() {
        return context;
    }

    public static void setUser(Context context, User user) {
        String userJson;
        //Logger.d("set user" + (user == null ? "" : user.toString()));
        mUser = user;
        if (user == null) {
            userJson = "";
        } else {
            userJson = new Gson().toJson(user);
        }
        SPUtils.set(context, Const.SP_KEY_USER, userJson);
    }

    /**
     * @param context context
     * @return mUser with stuNum and idNum
     */
    public static User getUser(Context context) {
        if (mUser == null) {
            String json = (String) SPUtils.get(context, Const.SP_KEY_USER, "");
            if (json == null || json.length() == 0) {
                //Logger.d("get user null");
                EventBus.getDefault().post(new LoginEvent());
                return null;
            }
            mUser = new Gson().fromJson(json, User.class);
        }
        if (mUser == null || mUser.stuNum == null || mUser.idNum == null) {
            //Logger.d("get mUser null");
            EventBus.getDefault().post(new LoginEvent());
            return null;
        }
        return mUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            //Timber.plant(new Timber.DebugTree());
            //Logger.init();
        }
        //Logger.init("cyxbs_mobile");
        context = getApplicationContext();
        initThemeMode();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initThemeMode() {
        isNight = (boolean) SPUtils.get(this, Const.SP_KEY_IS_NIGHT, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
