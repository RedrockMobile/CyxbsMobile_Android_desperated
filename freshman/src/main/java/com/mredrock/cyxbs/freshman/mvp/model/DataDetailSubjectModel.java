package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.SubjectProportion;
import com.mredrock.cyxbs.freshman.mvp.contract.DataDetailSubjectContract;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.kt.SpKt;

import kotlin.Unit;

/*
 by Cynthia at 2018/8/17
 description : 
 */
public class DataDetailSubjectModel implements DataDetailSubjectContract.IDataDetailSubjectModel {

    private String name;

    public DataDetailSubjectModel(String name) {
        this.name = name;
    }

    @Override
    public void setSubject(SubjectProportion subject, LoadCallBack loadCallBack) {
        loadCallBack.succeed(subject);
        SPHelper.putBean("subject", name, subject);
    }

    @Override
    public void error(String error, LoadCallBack callBack) {
        callBack.failed(App.getContext().getResources().getString(R.string.freshman_error_soft));
    }

    @Override
    public void loadData(LoadCallBack callBack) {
        SpKt.withSPCache(name, SubjectProportion.class,
                service -> service.getSubjectProportion(name),
                item -> {
                    callBack.succeed(item);
                    return Unit.INSTANCE;
                },
                error -> {
                    callBack.failed(error.getMessage());
                    return Unit.INSTANCE;
                }, "subject");
    }
}
