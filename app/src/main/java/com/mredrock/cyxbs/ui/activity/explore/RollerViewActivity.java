package com.mredrock.cyxbs.ui.activity.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RollerViewActivity extends BaseExploreActivity {
    private static final String TAG = LogUtils.makeLogTag(RollerViewActivity.class);

    @Bind(R.id.web_view)
    WebView webView;

    public static void startRollerViewActivity(String webUrl, Context context) {
        Intent intent = new Intent(context, RollerViewActivity.class);
        intent.putExtra("URL", webUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roller_view);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("URL");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
