package com.mredrock.cyxbs;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.excitingboat.freshmanspecial.App;
import com.google.gson.Gson;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.util.SPUtils;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import rx.Subscriber;


/**
 * Created by cc on 16/3/18.
 */
public class APP extends Application {
    private static Context context;
    private static User mUser;
    private static boolean login;

    public static final String TAG = "myAPP";

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
        return isLogin() && getUser(getContext()).stuNum.substring(0, 4).equals("2016");
    }

    public static void setLogin(boolean login) {
        APP.login = login;
    }

    public static boolean hasSetInfo() {
        User user = getUser(getContext());
        return user != null && StringUtils.isNotBlank(user.id);
    }

    public static boolean hasNickName() {
        return getUser(getContext()).nickname != null && !getUser(getContext()).nickname.equals("");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        initThemeMode();
        //  FIR.init(this);
        Logger.init("cyxbs_mobile");
        // Initialize FreshSpecial As library
        App.initializeLibrary(getContext());
        // Refresh Course List When Start
        reloadCourseList();
    }

    public void reloadCourseList() {
        if (isLogin()) {
            User user = getUser(getContext());
            RequestManager.getInstance().getCourseList(new Subscriber<List<Course>>() {
                                                           @Override
                                                           public void onCompleted() {}

                                                           @Override
                                                           public void onError(Throwable e) {}

                                                           @Override
                                                           public void onNext(List<Course> courses) {}
                                                       }, user.stuNum, user.idNum, 0, true);
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

    private void initThemeMode() {
        boolean isNight = (boolean) SPUtils.get(this, Const.SP_KEY_IS_NIGHT, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
