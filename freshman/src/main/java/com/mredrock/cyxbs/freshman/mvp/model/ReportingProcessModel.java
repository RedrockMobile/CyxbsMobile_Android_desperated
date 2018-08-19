package com.mredrock.cyxbs.freshman.mvp.model;

import android.util.Log;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.StrategyData;
import com.mredrock.cyxbs.freshman.mvp.contract.ReportingProcessContract;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.net.Const;
import com.mredrock.cyxbs.freshman.utils.net.HttpLoader;

/*
 by Cynthia at 2018/8/16
 description : 
 */
public class ReportingProcessModel implements ReportingProcessContract.IReportingProcessModel {

    @Override
    public void setData(StrategyData data, LoadCallBack callBack) {
        callBack.succeed(data);
        SPHelper.putBean(Const.INDEX_REGISTRATION, data);
    }

    @Override
    public void showError(String msg, LoadCallBack callBack) {
        String TAG = "ReportingProcessModel";
        Log.i(TAG, msg);
        callBack.failed(App.getContext().getResources().getString(R.string.freshman_error_soft));
    }

    @Override
    public void loadData(LoadCallBack callBack) {
        StrategyData data = SPHelper.getBean(Const.INDEX_REGISTRATION, StrategyData.class);
        if (data == null) {
            HttpLoader.<StrategyData>get(
                    service -> service.getStrategyData(Const.INDEX_REGISTRATION, Const.STRATEGY_PAGE_NUM, Const.STRATEGY_PAGE_SIZE),
                    item -> setData(item, callBack),
                    error -> showError(error.toString(), callBack)
            );
        } else {
            callBack.succeed(data);
        }
    }
}
