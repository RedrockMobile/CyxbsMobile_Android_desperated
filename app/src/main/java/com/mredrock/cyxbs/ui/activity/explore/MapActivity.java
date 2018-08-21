package com.mredrock.cyxbs.ui.activity.explore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;

public class MapActivity extends BaseExploreActivity {
    public static void startMapActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, MapActivity.class);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("加载中...");
        dialog.show();

        WebView webView = findViewById(R.id.map);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        webView.loadUrl("https://720yun.com/t/ae8jtpsnOn3?scene_id=13628522&from=singlemessage&isappinstalled=0");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    dialog.dismiss();
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
