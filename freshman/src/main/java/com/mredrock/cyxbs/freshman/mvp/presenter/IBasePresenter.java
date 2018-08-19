package com.mredrock.cyxbs.freshman.mvp.presenter;

public interface IBasePresenter<V> {
    void attachView(V view);//绑定View

    void detachView();//解绑View
}
