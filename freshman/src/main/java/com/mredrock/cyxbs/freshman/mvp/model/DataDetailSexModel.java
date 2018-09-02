package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.SexProportion;
import com.mredrock.cyxbs.freshman.mvp.contract.DataDetailSexContract;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.kt.SpKt;

import kotlin.Unit;

/*
 by Cynthia at 2018/8/17
 description : 
 */
public class DataDetailSexModel implements DataDetailSexContract.IDataDetailSexModel {

    private String name;

    public DataDetailSexModel(String name) {
        this.name = name;
    }

    @Override
    public void setSex(SexProportion sex, LoadCallBack callBack) {
        callBack.succeed(sex);
        SPHelper.putBean("sex", name, sex);
    }

    @Override
    public void error(String error, LoadCallBack callBack) {
        callBack.failed(App.getContext().getResources().getString(R.string.freshman_error_soft));
    }

    @Override
    public void loadData(LoadCallBack callBack) {
        SpKt.withSPCache(name, SexProportion.class,
                service -> service.getSexProportion(name),
                item -> {
                    callBack.succeed(item);
                    return Unit.INSTANCE;
                },
                error -> {
                    callBack.failed(error.getMessage());
                    return Unit.INSTANCE;
                }, "sex");
    }
}
