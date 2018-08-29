package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.bean.MilitaryShow;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryShowContract;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.kt.SpKt;
import com.mredrock.cyxbs.freshman.utils.net.APIService;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import kotlin.Unit;

public class MilitaryShowModel implements MilitaryShowContract.IMilitaryShowModel {

    @Override
    public void loadData(LoadCallBack callBack) {
        SpKt.withSPCache(Const.INDEX_MILITARY_TRAINING_TITLE, MilitaryShow.class, APIService::getMilitaryShow,
                item -> {
                    callBack.succeed(item);
                    return Unit.INSTANCE;
                }, error -> {
                    callBack.failed(error.getMessage());
                    return Unit.INSTANCE;
                });
    }

    @Override
    public void setItem(MilitaryShow bean, LoadCallBack callBack) {
        SPHelper.putBean("军训特辑", bean);
        callBack.succeed(bean);
    }

    @Override
    public void error(String str, LoadCallBack callBack) {
        callBack.failed(str);
    }


}
