package com.mredrock.cyxbs.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

import com.afollestad.materialdialogs.MaterialDialog
import com.jude.swipbackhelper.SwipeBackHelper
import com.mredrock.cyxbs.event.AskLoginEvent
import com.mredrock.cyxbs.event.LoginEvent
import com.mredrock.cyxbs.event.LoginStateChangeEvent
import com.mredrock.cyxbs.util.KeyboardUtils
import com.umeng.analytics.MobclickAgent

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoLogger

open class BaseActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        SwipeBackHelper.onCreate(this)
        SwipeBackHelper.getCurrentPage(this).setSwipeRelateEnable(true)

        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SwipeBackHelper.onPostCreate(this)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            KeyboardUtils.autoHideInput(v, ev)
            return super.dispatchTouchEvent(ev)
        }
        return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    open fun onAskLoginEvent(event: AskLoginEvent) {
        val handler = Handler(mainLooper)
        handler.post {
            MaterialDialog.Builder(this)
                    .title("是否登录?")
                    .content(event.msg)
                    .positiveText("马上去登录")
                    .negativeText("我再看看")
                    .callback(object : MaterialDialog.ButtonCallback() {
                        override fun onPositive(dialog: MaterialDialog?) {
                            super.onPositive(dialog)
                            onLoginEvent(LoginEvent())
                        }

                        override fun onNegative(dialog: MaterialDialog?) {
                            super.onNegative(dialog)
                            dialog!!.dismiss()
                        }
                    }).show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onLoginStateChangeEvent(event: LoginStateChangeEvent) {
        Log.d("LoginStateChangeEvent", "in $localClassName login state: ${event.newState}")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        SwipeBackHelper.onDestroy(this)
    }
}
