package com.mredrock.cyxbs.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.event.ExitEvent;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.util.KeyboardUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {
    private static final int HEADER_HIDE_ANIM_DURATION = 300;

    private boolean mActionBarAutoHideEnbale = false;
    private boolean mActionBarShown;
    private int mActionBarAutoHideMinY        = 0;
    private int mActionBarAutoHideSensitivity = 0;
    private int mActionBarAutoHideSingnal     = 0;

    private ArrayList<View> mHideableHeaderViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeRelateEnable(true);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            KeyboardUtils.autoHideInput(v, ev);
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    protected void enbaleActionBarAutoHide(RecyclerView recyclerView) {
        setupActionBarAutoHide();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private Map<Integer, Integer> heights = new HashMap<>();
            private int lastCurrentScrollY = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();
                View firstVisibleItemView = layoutManager.getChildAt(0);
                if (firstVisibleItemView == null) {
                    return;
                }

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                heights.put(firstVisibleItemPosition, firstVisibleItemView.getHeight());

                int previousItemsHeight = 0;
                for (int i = 0; i < firstVisibleItemPosition; i++) {
                    previousItemsHeight += heights.get(i) != null ? heights.get(i) : 0;
                }
                int currentScrollY = previousItemsHeight - firstVisibleItemView.getTop()
                        + firstVisibleItemView.getPaddingTop();
                onHomeContentScrolled(currentScrollY, currentScrollY - lastCurrentScrollY);
                lastCurrentScrollY = currentScrollY;
            }
        });
    }

    private void setupActionBarAutoHide() {
        mActionBarAutoHideEnbale = true;
        mActionBarAutoHideMinY = getResources().getDimensionPixelOffset(R.dimen.action_bar_auto_hide_min_y);
        mActionBarAutoHideSensitivity = getResources().getDimensionPixelOffset(R.dimen.action_bar_auto_hide_sensitivity);
    }

    private void onHomeContentScrolled(int currentY, int deltaY) {
        if (deltaY > mActionBarAutoHideSensitivity) {
            deltaY = mActionBarAutoHideSensitivity;
        } else if (deltaY < -mActionBarAutoHideSensitivity) {
            deltaY = -mActionBarAutoHideSensitivity;
        }

        if (Math.signum(deltaY) * Math.signum(mActionBarAutoHideSensitivity) < 0) {
            mActionBarAutoHideSingnal = deltaY;
        } else {
            mActionBarAutoHideSingnal += deltaY;
        }

        boolean shouldShow = currentY < mActionBarAutoHideMinY ||
                (mActionBarAutoHideSingnal <= -mActionBarAutoHideSensitivity);
        autoShowOrHideActionBar(shouldShow);
    }

    private void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
        onActionBarAutoShowOrHide(show);
    }

    private void onActionBarAutoShowOrHide(boolean shown) {
        updateSwipeRefreshProgressBarTop();
        if (mHideableHeaderViews != null && mHideableHeaderViews.size() > 0) {

            for (final View view : mHideableHeaderViews) {
                if (shown) {
                    ViewCompat.animate(view)
                              .translationY(0)
                              .alpha(1)
                              .setDuration(HEADER_HIDE_ANIM_DURATION)
                              .withLayer();
                } else {
                    ViewCompat.animate(view)
                              .translationY(-view.getBottom())
                              .alpha(1)
                              .setDuration(HEADER_HIDE_ANIM_DURATION)
                              .setInterpolator(new DecelerateInterpolator())
                              .withLayer();
                }
            }
        }
    }

    private void updateSwipeRefreshProgressBarTop() {

    }

    protected void registerHideableHeaderView(View hideableHeaderView) {
        if (mHideableHeaderViews == null) {
            mHideableHeaderViews = new ArrayList<>();
        }
        if (!mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.add(hideableHeaderView);
        }
    }

    protected void unregisterHideableHeaderView(View hideableHeaderView) {
        if (mHideableHeaderViews != null && mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.remove(hideableHeaderView);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAskLoginEvent(AskLoginEvent event) {
        Handler handler = new Handler(getMainLooper());
        handler.post(() -> new MaterialDialog.Builder(this)
                .title("是否登录?")
                .content(event.getMsg())
                .positiveText("马上去登录")
                .negativeText("我再看看")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        onLoginEvent(new LoginEvent());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                }).show());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        // Override this method in sub activity
        // event.getNewState() == true : user login
        // event.getNewState() == false : user logout
        Log.d("LoginStateChangeEvent", "in" + getLocalClassName() + "login state: " + event.getNewState() + "");
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onExitEvent(ExitEvent event) {
       // this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SwipeBackHelper.onDestroy(this);
    }
}
