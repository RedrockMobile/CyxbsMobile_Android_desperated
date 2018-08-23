package com.mredrock.cyxbs.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.remind_service.RemindManager;
import com.mredrock.cyxbs.model.StartPage;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.adapter.SplashGuidePageVpAdapter;
import com.mredrock.cyxbs.util.SPUtils;
import com.mredrock.cyxbs.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends Activity {

    public static final String TAG = "SplashActivity";

    public static final String VERSION = "app_version";

    private ImageView mIvSplash;
    private ViewPager mGuideViewPager;

    private boolean mainActivityIsStarted = false;
    private ViewPropertyAnimator mViewPropertyAnimator;

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
        setFullScreen();

        int version = (Integer) SPUtils.get(this, VERSION, -1);
        int curVersion = Utils.getAppVersionCode(this);
        if (version != Utils.getAppVersionCode(this)) {
            showGuidePage();
            SPUtils.set(this, VERSION, curVersion);
        } else {
            showSplashImage();
        }

        RemindManager.getInstance().pushAll(this);
    }

    private void setFullScreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void startMainActivity() {
        //防止多次点击使activity被多次加载
        if (mainActivityIsStarted) return;
        mainActivityIsStarted = true;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        SplashActivity.this.finish();
    }

    private void showSplashImage() {
        ((ViewStub) findViewById(R.id.view_stub_splash)).inflate();
        mIvSplash = findViewById(R.id.iv_splash);
        mIvSplash.postDelayed(this::startMainActivity, 1000);

        RequestManager.getInstance().getStartPage(new SimpleObserver<>(this, new SubscriberListener<StartPage>() {
            @Override
            public boolean onError(Throwable e) {
                return true;
            }

            @Override
            public void onNext(StartPage startPage) {
                if (startPage != null) {
                    Glide.with(SplashActivity.this).load(startPage.getPhoto_src()).centerCrop().into(mIvSplash);
                }
            }
        }));
    }

    private void showGuidePage() {
        ((ViewStub) findViewById(R.id.view_stub_guide)).inflate();
        mGuideViewPager = findViewById(R.id.view_pager);
        final int[] guideImages = {R.drawable.bg_splash_guide_1, R.drawable.bg_splash_guide_2,
                R.drawable.bg_splash_guide_3, R.drawable.bg_splash_guide_4};

        final ImageView[] imageViews = new ImageView[guideImages.length];
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < guideImages.length; i++) {
            imageViews[i] = new ImageView(this);
            imageViews[i].setLayoutParams(lp);
            imageViews[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews[i].setImageResource(guideImages[i]);
        }
        mGuideViewPager.setAdapter(new SplashGuidePageVpAdapter(imageViews));
        bindIndicator(guideImages.length);
    }

    private void bindIndicator(int length) {
        final int size = getResources().getDimensionPixelSize(R.dimen.splash_indicator_size);
        final int margin = getResources().getDimensionPixelSize(R.dimen.splash_indicator_margin);

        Button skipButton = findViewById(R.id.btn_skip);
        Button nextButton = findViewById(R.id.btn_next);
        skipButton.setOnClickListener(view -> startMainActivity());
        nextButton.setOnClickListener(view -> startMainActivity());

        //初始化指示器
        LinearLayout container = findViewById(R.id.ll_indicator_container);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
        lp.setMargins(margin, 0, margin, 0);
        for (int i = 0; i < length; i++) {
            View temp = new View(this);
            temp.setLayoutParams(lp);
            temp.setBackgroundResource(R.drawable.bg_splash_guide_indicator_unselected);
            container.addView(temp);
        }

        //指示器选中动画
        View indicator = findViewById(R.id.selected_indicator);
        final int gap = margin * 2 + size;
        mGuideViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                indicator.setTranslationX((position + positionOffset) * gap);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == length - 1) {
                    showButtonWithAnimation(skipButton, false);
                    showButtonWithAnimation(nextButton, true);
                } else {
                    showButtonWithAnimation(skipButton, true);
                    showButtonWithAnimation(nextButton, false);
                }
            }
        });
    }

    private void showButtonWithAnimation(Button button, boolean show) {
        if (show && button.getVisibility() != View.VISIBLE) {
            mViewPropertyAnimator = button.animate().alpha(1).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    button.setVisibility(View.VISIBLE);
                    button.setAlpha(0);
                }
            });
        } else if (!show && button.getVisibility() == View.VISIBLE) {
            mViewPropertyAnimator = button.animate().alpha(0).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    button.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
