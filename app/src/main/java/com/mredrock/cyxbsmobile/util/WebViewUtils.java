package com.mredrock.cyxbsmobile.util;

import android.app.Activity;

import com.mredrock.cyxbsmobile.R;
import com.thefinestartist.finestwebview.FinestWebView;

/**
 * Created by Stormouble on 16/4/7.
 */
public class WebViewUtils {

    private WebViewUtils() {

    }

    public static void showPortalWebView(Activity activity, String url) {
        if (activity != null && url != null) {
            new FinestWebView.Builder(activity)
                    .theme(R.style.FinestWebViewTheme)
                    .titleDefaultRes(R.string.portal_title)
                    .toolbarColorRes(R.color.colorPrimary)
                    .toolbarScrollFlags(0)
                    .statusBarColorRes(R.color.colorPrimary)
                    .toolbarColorRes(R.color.colorPrimary)
                    .titleColorRes(R.color.finestWhite)
                    .urlColorRes(R.color.finestWhite)
                    .iconDefaultColorRes(R.color.finestWhite)
                    .progressBarColorRes(R.color.finestWhite)
                    .showSwipeRefreshLayout(true)
                    .swipeRefreshColorRes(R.color.colorPrimary)
                    .menuSelector(R.drawable.selector_light_theme)
                    .stringResRefresh(R.string.portal_menu_refresh)
                    .stringResShareVia(R.string.portal_menu_share)
                    .stringResCopyLink(R.string.portal_menu_copy)
                    .stringResOpenWith(R.string.portal_open_with)
                    .dividerHeight(0)
                    .gradientDivider(false)
                    .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                    .webViewJavaScriptEnabled(true)
                    .webViewAppCacheEnabled(true)
                    .webViewAllowFileAccess(true)
                    .webViewUseWideViewPort(true)
                    .webViewLoadWithOverviewMode(true)
                    .webViewDomStorageEnabled(true)
                    .webViewBuiltInZoomControls(true)
                    .show(url);
        }
    }
}
