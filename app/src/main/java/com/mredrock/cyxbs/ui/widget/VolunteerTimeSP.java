package com.mredrock.cyxbs.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by glossimar on 2017/9/20.
 */

public class VolunteerTimeSP {
    private Activity mActivity;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public VolunteerTimeSP(Activity activity) {
        mActivity = activity;
        sharedPref = mActivity.getSharedPreferences("VolunteerTimeInfo", MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void bindVolunteerInfo(String account, String password, String uid){
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putString("uid", uid);
        editor.commit();
    }
    public void unBindVolunteerInfo() {
        editor.putString("account", "404");
        editor.putString("password", "404");
        editor.putString("uid", "404");
        editor.commit();
    }

    public String getVolunteerUid() {
        String uid = sharedPref.getString("uid", "404");
        return uid;
    }

    public String getVolunteerAccount() {
        String account = sharedPref.getString("account", "404");
        return account;
    }

    public String getVolunteerPassword() {
        String password = sharedPref.getString("password", "404");
        return password;
    }
}
