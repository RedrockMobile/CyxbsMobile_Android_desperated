package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.bean.MienStu;
import com.mredrock.cyxbs.freshman.mvp.contract.CquptMienBaseContract;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.kt.SpKt;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import kotlin.Unit;

public class CquptMienBaseModel implements CquptMienBaseContract.ICquptMienBaseModel {

    @Override
    public void loadData(LoadCallBack callBack) {
        SpKt.withSPCache(Const.INDEX_ORGANIZATION, MienStu.class,
                service -> service.getMienStu(Const.INDEX_ORGANIZATION, "1", "30"),
                mienStu -> {
                    callBack.succeed(mienStu);
                    return Unit.INSTANCE;
                },
                fail -> {
                    callBack.failed(fail.getMessage());
                    return Unit.INSTANCE;
                });
    }

    @Override
    public void setItem(MienStu bean, LoadCallBack callBack) {
        SPHelper.putBean(Const.INDEX_ORGANIZATION, bean);
        callBack.succeed(bean);
    }

    @Override
    public void LoadAnotherData(LoadCallBack callBack) {
        SpKt.withSPCache(Const.INDEX_ACTIVITY, MienStu.class,
                service -> service.getMienStu(Const.INDEX_ACTIVITY, "1", "30"),
                mienStu -> {
                    callBack.succeed(mienStu);
                    return Unit.INSTANCE;
                },
                fail -> {
                    callBack.failed(fail.getMessage());
                    return Unit.INSTANCE;
                });
    }

    @Override
    public void setAnotherItem(MienStu bean, LoadCallBack callBack) {
        SPHelper.putBean(Const.INDEX_ACTIVITY, bean);
        callBack.succeed(bean);
    }

    @Override
    public void error(String str, LoadCallBack callBack) {
        callBack.failed(str);
    }


}
