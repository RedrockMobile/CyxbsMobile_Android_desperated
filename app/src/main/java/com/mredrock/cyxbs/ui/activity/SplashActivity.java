package com.mredrock.cyxbs.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.StartPage;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.service.NotificationService;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class SplashActivity extends Activity {

    public static final String TAG = "SplashActivity";

    @Bind(R.id.iv_splash)
    ImageView mIvSplash;

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
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 2000);

        //启动用于课前提醒的服务
        NotificationService.startNotificationService(this);

        RequestManager.getInstance().getStartPage(new Subscriber<StartPage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(StartPage startPages) {
                if (startPages != null) {
                    Glide.with(SplashActivity.this).load(startPages.getPhoto_src()).into(mIvSplash);
                }
            }
        });
    }
}
