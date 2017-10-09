package com.mredrock.cyxbs.ui.activity.explore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.LogUtils;

public class RollerViewActivity extends BaseActivity {
    private static final String TAG = LogUtils.makeLogTag(RollerViewActivity.class);

    public static void startRollerViewActivity(String webUrl, Context context) {
        Intent intent = new Intent(context, RollerViewActivity.class);
        intent.putExtra("URL", webUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roller_view);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("加载中...");
        dialog.show();

        String url = getIntent().getStringExtra("URL");
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) {
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
