package com.mredrock.cyxbs;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.util.SPUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by cc on 16/3/18.
 */
public class APP extends Application {
    private static Context context;
    private static User    mUser;
    private static boolean login;

    public static Context getContext() {
        return context;
    }

    public static void setUser(Context context, User user) {
        String userJson;
        mUser = user;
        if (user == null) {
            APP.setLogin(false);
            userJson = "";
        } else {
            userJson = new Gson().toJson(user);
            APP.setLogin(true);
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
            mUser = new Gson().fromJson(json, User.class);

            if (mUser == null || mUser.stuNum == null || mUser.idNum == null) {
                if (EventBus.getDefault().hasSubscriberForEvent(LoginEvent.class)) {
                    EventBus.getDefault().post(new LoginEvent());
                }
                return null;
            }
        }
        return mUser;
    }

    public static boolean isLogin() {
        if (!login) {
            String json = (String) SPUtils.get(context, Const.SP_KEY_USER, "");
            User user = new Gson().fromJson(json, User.class);
            login = user != null;
        }
        return login;
    }

    public static void setLogin(boolean login) {
        APP.login = login;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        initThemeMode();
        Logger.init("cyxbs_mobile");
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
        boolean isNight = (boolean) SPUtils.get(this, Const.SP_KEY_IS_NIGHT, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
