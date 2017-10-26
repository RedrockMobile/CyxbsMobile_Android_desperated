package com.mredrock.cyxbs;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.network.encrypt.UserInfoEncryption;
import com.mredrock.cyxbs.ui.activity.exception.ExceptionActivity;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.SPUtils;
import com.mredrock.cyxbs.util.Utils;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by cc on 16/3/18.
 */
public class BaseAPP extends MultiDexApplication {
    private static Context context;
    private static User mUser;
    private static boolean login;

    public static final String TAG = "myAPP";

    public static Context getContext() {
        return context;
    }

    private static UserInfoEncryption userInfoEncryption;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static void setUser(Context context, User user) {
        String userJson;
        mUser = user;
        if (user == null) {
            BaseAPP.setLogin(false);
            userJson = "";
        } else {
            userJson = new Gson().toJson(user);
            BaseAPP.setLogin(true);
        }
        String encryptedJson = userInfoEncryption.encrypt(userJson);
        SPUtils.set(context, Const.SP_KEY_USER, encryptedJson);
    }

    /**
     * @param context context
     * @return mUser with stuNum and idNum
     */
    public static User getUser(Context context) {
        if (mUser == null) {
            String encryptedJson = (String) SPUtils.get(context, Const.SP_KEY_USER, "");
            String json = userInfoEncryption.decrypt(encryptedJson);
            Log.d("userinfo", json);
            mUser = new Gson().fromJson(json, User.class);

            if (mUser == null || mUser.stuNum == null || mUser.idNum == null) {
                initializeFakeUser();
            }
        }
        return mUser;
    }

    public static boolean isLogin() {
        if (!login) {
            String encryptedJson = (String) SPUtils.get(context, Const.SP_KEY_USER, "");
            String json = userInfoEncryption.decrypt(encryptedJson);
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
        return isLogin() && getUser(getContext()).stuNum.substring(0, 4).equals("2017");
    }

    public static void setLogin(boolean login) {
        BaseAPP.login = login;
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
        Config.DEBUG = BuildConfig.DEBUG;
        initPush();
        UMShareAPI.get(this);
        initShareKey();
        context = getApplicationContext();
        initThemeMode();
        //  FIR.init(this);
        Logger.init("cyxbs_mobile");
        ExceptionActivity.install(getApplicationContext(), true);
        // Initialize UserInfoEncrypted
        userInfoEncryption = new UserInfoEncryption();
        // Refresh Course List When Start
        reloadCourseList();
        disableFileUriExposedException();
        initBugly();
    }

    private void initPush() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "123b419248120b9fb91a38260a13e972");
        UMConfigure.setLogEnabled(true);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        MiPushRegistar.register(this,"2882303761517258683","5341725868683");
        HuaWeiRegister.register(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                Log.d(TAG, "友盟注册成功: " + deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "onFailure: 友盟注册失败" + s + s1);
            }
        });
    }

    private boolean onMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private void initShareKey() {
        PlatformConfig.setSinaWeibo("197363903", "7700116c567ab2bb28ffec2dcf67851d", "http://hongyan.cqupt.edu.cn/app/");
        PlatformConfig.setQQZone("1106072365", "v9w1F3OSDhkX14gA");
    }

    public void reloadCourseList() {
        if (isLogin()) {
            User user = getUser(getContext());
            RequestManager.getInstance().getCourseList(new Observer<List<Course>>() {
                @Override
                public void onComplete() {
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("CSET", "reloadCourseList", e);
                }

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<Course> courses) {
                }
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

    private void disableFileUriExposedException() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Method disableDeathOnFileUriExposure = StrictMode.class.getDeclaredMethod("disableDeathOnFileUriExposure");
                disableDeathOnFileUriExposure.setAccessible(true);
                disableDeathOnFileUriExposure.invoke(null);
            } catch (Exception e) {
                LogUtils.LOGE("FileUriExposure", "Can't disable death on file uri exposure", e);
            }
        }
    }

    private void initBugly() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getContext());
        strategy.setAppVersion(Utils.getAppVersionName(getContext()));      //App的版本

        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());

        strategy.setUploadProcess(processName == null || processName.equals(packageName));

        CrashReport.initCrashReport(getApplicationContext(), BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG, strategy);
    }

    @Nullable
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
