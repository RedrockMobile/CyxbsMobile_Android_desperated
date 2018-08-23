package com.mredrock.cyxbs.freshman.mvp.presenter;


import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;

public class BasePresenter<V extends BaseContract.ISomethingView> implements IBasePresenter<V> {
    private V view;

    //关联View
    @Override
    public void attachView(V view) {
        this.view = view;
    }

    //解除View关联
    @Override
    public void detachView() {
        view = null;
    }

    public V getView() {
        return view;
    }
}
