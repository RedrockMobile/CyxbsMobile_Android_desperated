package com.mredrock.cyxbs.freshman.mvp.model;


import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;

public class BaseModel implements BaseContract.ISomethingModel {

    @Override
    public void loadData(LoadCallBack callBack) {
        //do something 然后用接口回调
    }
}
