package com.mredrock.cyxbs.freshman.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mredrock.cyxbs.freshman.utils.DensityUtils
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.freshman_include_toolbar.*

abstract class BaseActivity : AppCompatActivity() {
    abstract fun getLayoutResID(): Int
    abstract fun getToolbarTitle(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())
        DensityUtils.setTransparent(toolbar, this)
        tv_toolbar_title.text = getToolbarTitle()
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    /**
     * 这里是因为应用被切后台，Activity被回收之后会出BUG
     * 因为没想到好的解决方案，就强行这样了
     */
    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
    }
}