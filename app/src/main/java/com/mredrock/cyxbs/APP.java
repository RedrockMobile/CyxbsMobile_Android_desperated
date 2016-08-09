package com.mredrock.cyxbs;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.text.format.DateUtils;

import com.google.gson.Gson;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.util.SPUtils;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import im.fir.sdk.FIR;


/**
 * Created by cc on 16/3/18.
 */
public class APP extends Application {
    private static Context context;
    private static User mUser;
    private static boolean login;

    public static Context getContext() {
        return context;
    }

    // TODO: isLogin getUser setUser 使用的位置,逻辑

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
                initializeFakeUser();
            }
        }
        return mUser;
    }

    public static boolean isLogin() {
        if (!login) {
            String json = (String) SPUtils.get(context, Const.SP_KEY_USER, "");
            User user = new Gson().fromJson(json, User.class);
            if (user != null && !user.stuNum.equals("0")) {
                return true;
            } else {
                initializeFakeUser();
            }
        }
        return login;
    }

    private static void initializeFakeUser() {
        mUser = new User();
      //  mUser.id = "0";
        mUser.idNum = "0";
        mUser.stuNum = "0";
    }

    public static boolean isFresh() {
        return !isLogin() && mUser.stuNum.substring(0, 3).equals(DateUtils.YEAR_FORMAT);
    }

    public static void setLogin(boolean login) {
        APP.login = login;
    }

    public static boolean hasSetInfo() {
        User user = getUser(getContext());
        return user != null && StringUtils.isNotBlank(user.id);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        initThemeMode();
        FIR.init(this);
        Logger.init("cyxbs_mobile");

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate(){
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
