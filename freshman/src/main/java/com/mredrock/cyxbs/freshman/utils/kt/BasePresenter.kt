package com.mredrock.cyxbs.freshman.utils.kt

import com.mredrock.cyxbs.freshman.mvp.presenter.IBasePresenter

abstract class BasePresenter<V, M> : IBasePresenter<V> {
    protected var mvpView: V? = null
        private set

    protected val model by lazy { createModel() }

    override fun attachView(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        mvpView = null
    }

    abstract fun createModel(): M

}