package com.mredrock.cyxbs.ui.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.Toolbar
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView

import com.mredrock.cyxbs.APP
import com.mredrock.cyxbs.R
import com.mredrock.cyxbs.event.LoginStateChangeEvent
import com.mredrock.cyxbs.model.User
import com.mredrock.cyxbs.network.RequestManager
import com.mredrock.cyxbs.subscriber.SimpleSubscriber
import com.mredrock.cyxbs.subscriber.SubscriberListener
import com.mredrock.cyxbs.ui.activity.me.EditNickNameActivity
import com.mredrock.cyxbs.util.Utils
import com.umeng.analytics.MobclickAgent

import org.greenrobot.eventbus.EventBus

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class LoginActivity : BaseActivity() {
    @BindView(R.id.login_stu_num_edit)
    internal var stuNumEdit: AppCompatEditText? = null
    @BindView(R.id.login_id_num_edit)
    internal var idNumEdit: AppCompatEditText? = null
    @BindView(R.id.login_submit_button)
    internal var submitButton: Button? = null
    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null
    @BindView(R.id.toolbar_title)
    internal var toolbarTitle: TextView? = null

    @OnClick(R.id.login_submit_button)
    internal fun clickToLogin() {
        attemptLogin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        initUser()
        autoSendFn()
        initToolbar()
    }

    private fun initToolbar() {
        if (toolbar != null) {
            toolbar!!.title = ""
            toolbarTitle!!.text = "登 录"
            setSupportActionBar(toolbar)
            toolbar!!.setNavigationIcon(R.drawable.ic_back)
            toolbar!!.setNavigationOnClickListener { v -> this@LoginActivity.finish() }
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeButtonEnabled(true)
            }
        }
    }

    /**
     * 软键盘登录
     */
    private fun autoSendFn() {
        idNumEdit!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                attemptLogin()
            }
            false
        }
    }

    private fun initUser() {
        APP.setUser(this, null)
    }

    fun attemptLogin() {
        val stuNum = stuNumEdit!!.text.toString()
        val idNum = idNumEdit!!.text.toString()
        if ("" == idNum) {
            Utils.toast(this, "请输入密码")
        }
        RequestManager.getInstance()
                .login(SimpleSubscriber(this, true, false, object : SubscriberListener<User>() {
                    override fun onNext(user: User?) {
                        super.onNext(user)
                        if (user != null) {
                            APP.setUser(this@LoginActivity, user)
                            MobclickAgent.onProfileSignIn(stuNum)
                        } else {
                            Utils.toast(this@LoginActivity, "登录失败, 返回了信息为空")
                        }
                    }

                    override fun onCompleted() {
                        super.onCompleted()
                        EventBus.getDefault().post(LoginStateChangeEvent(true))
                        finish()
                        if (!APP.hasNickName()) {
                            EditNickNameActivity.start(APP.getContext())
                        }
                    }
                }), stuNum, idNum)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
