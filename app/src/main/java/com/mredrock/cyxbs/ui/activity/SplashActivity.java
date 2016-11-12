package com.mredrock.cyxbs.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.mredrock.cyxbs.service.NotificationService;

import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_CLASS;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_DAY;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (!APP.isLogin() || APP.getUser(this) == null) {
//            startActivity(new Intent(this, LoginActivity.class));
//        } else {
        startActivity(new Intent(this, MainActivity.class));
//        }

        //启动用于课前提醒的服务
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(SP_REMIND_EVERY_CLASS, false) || sp.getBoolean(SP_REMIND_EVERY_DAY, false)) {
            Intent service = new Intent(this, NotificationService.class);
            startService(service);
        }
        this.finish();
    }
}
