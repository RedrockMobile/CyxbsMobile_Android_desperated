package com.mredrock.cyxbs.freshman.mvp.contract;

import com.mredrock.cyxbs.freshman.bean.StrategyData;

/*
 by Cynthia at 2018/8/16
 description : 
 */
public class ReportingProcessContract {
    public interface IReportingProcessView extends BaseContract.ISomethingView {
        void showError(String msg);

        void setData(StrategyData data);

        void expandItem(StrategyData.DetailData detailData);
    }

    public interface IReportingProcessModel extends BaseContract.ISomethingModel {
        void setData(StrategyData data, LoadCallBack callBack);

        void showError(String msg, LoadCallBack callBack);
    }
}
