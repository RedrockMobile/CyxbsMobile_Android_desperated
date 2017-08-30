package com.mredrock.cyxbs.ui.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import butterknife.ButterKnife
import com.mredrock.cyxbs.APP
import com.mredrock.cyxbs.R
import com.mredrock.cyxbs.event.LoginStateChangeEvent
import com.mredrock.cyxbs.model.User
import com.mredrock.cyxbs.network.RequestManager
import com.mredrock.cyxbs.subscriber.SimpleObserver
import com.mredrock.cyxbs.subscriber.SubscriberListener
import com.mredrock.cyxbs.ui.activity.me.EditNickNameActivity
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        initUser()
        autoSendFn()
        initToolbar()
        login_submit_button.setOnClickListener { attemptLogin() }
    }

    private fun initToolbar() {
        toolbar.title = ""
        toolbar_title.text = "登 录"
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { this@LoginActivity.finish() }
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }
    }

    /**
     * 软键盘登录
     */
    private fun autoSendFn() {
        login_id_num_edit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                attemptLogin()
            }
            false
        }
    }

    private fun initUser() {
        APP.setUser(this, null)
    }

    private fun attemptLogin() {
        val stuNum = login_stu_num_edit.text.toString()
        val idNum = login_id_num_edit.text.toString()
        if (idNum.isBlank()) toast("请输入密码")
        RequestManager.getInstance()
                .login(SimpleObserver(this, true, false, object : SubscriberListener<User>() {
                    override fun onNext(user: User?) {
                        super.onNext(user)
                        if (user != null) {
                            APP.setUser(this@LoginActivity, user)
                            MobclickAgent.onProfileSignIn(stuNum)
                        } else {
                            toast("登录失败, 返回了信息为空")
                        }
                    }

                    override fun onComplete() {
                        super.onComplete()
                        EventBus.getDefault().post(LoginStateChangeEvent(true))
                        finish()
                        if (!APP.hasNickName()) {
                            EditNickNameActivity.start(APP.getContext())
                        }
                    }
                }), stuNum, idNum)
    }
}
