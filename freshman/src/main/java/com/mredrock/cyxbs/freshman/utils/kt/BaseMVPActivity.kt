package com.mredrock.cyxbs.freshman.utils.kt

import android.annotation.SuppressLint
import android.os.Bundle
import com.mredrock.cyxbs.freshman.mvp.presenter.IBasePresenter
import com.mredrock.cyxbs.freshman.ui.activity.BaseActivity

@SuppressLint("Registered")
abstract class BaseMVPActivity<V, P : IBasePresenter<V>> : BaseActivity() {

    protected val persenter: P by lazy { createPersenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        persenter.attachView(getViewAttachPersenter())
    }

    abstract fun getViewAttachPersenter(): V

    override fun onDestroy() {
        super.onDestroy()
        persenter.detachView()
    }

    abstract fun createPersenter(): P
}