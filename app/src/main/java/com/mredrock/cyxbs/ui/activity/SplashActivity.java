package com.mredrock.cyxbs.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mredrock.cyxbs.APP;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!APP.isLogin() || APP.getUser(this) == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        this.finish();
    }
}
