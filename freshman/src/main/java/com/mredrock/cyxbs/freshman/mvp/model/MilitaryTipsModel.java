package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryTipsContract;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.kt.SpKt;
import com.mredrock.cyxbs.freshman.utils.net.Const;

import kotlin.Unit;

public class MilitaryTipsModel implements MilitaryTipsContract.IMilitaryTipsModel {

    @Override
    public void loadData(LoadCallBack callBack) {
        SpKt.withSPCache(Const.INDEX_MILITARY_TRAINING, Description.class, service -> service.getDescriptions(Const.INDEX_MILITARY_TRAINING),
                item -> {
                    callBack.succeed(item);
                    return Unit.INSTANCE;
                },
                error -> {
                    callBack.failed(error.getMessage());
                    return Unit.INSTANCE;
                });
    }


    @Override
    public void setItem(Description description, LoadCallBack callBack) {
        for (Description.DescribeBean bean : description.getDescribe()) {
            bean.setCheck(false);
            bean.setDelete(false);
        }
        callBack.succeed(description);
        SPHelper.putBean(Const.INDEX_MILITARY_TRAINING, description);
    }

    @Override
    public void error(String str, LoadCallBack callBack) {
        callBack.failed(str);
    }
}
