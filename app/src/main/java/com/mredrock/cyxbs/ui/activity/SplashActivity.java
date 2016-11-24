package com.mredrock.cyxbs.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.service.NotificationService;
import com.umeng.analytics.MobclickAgent;

import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_CLASS;
import static com.mredrock.cyxbs.ui.fragment.me.RemindFragment.SP_REMIND_EVERY_DAY;

public class SplashActivity extends Activity {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //启动用于课前提醒的服务
        StatusBarUtil.setTranslucent(this, 50);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(SP_REMIND_EVERY_CLASS, false) || sp.getBoolean(SP_REMIND_EVERY_DAY, false)) {
            Intent service = new Intent(this, NotificationService.class);
            startService(service);
        }
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
