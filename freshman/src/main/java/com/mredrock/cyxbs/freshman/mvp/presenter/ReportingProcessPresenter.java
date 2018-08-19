package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.bean.StrategyData;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.ReportingProcessContract;
import com.mredrock.cyxbs.freshman.mvp.model.ReportingProcessModel;

/*
 by Cynthia at 2018/8/16
 description : 
 */
public class ReportingProcessPresenter extends BasePresenter<ReportingProcessContract.IReportingProcessView> {

    private ReportingProcessModel model;

    public ReportingProcessPresenter(ReportingProcessModel model) {
        this.model = model;
    }

    public void start() {
        checkIsAttach();//检查是否绑定
        model.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                StrategyData data = (StrategyData) o;
                getView().setData(data);
            }

            @Override
            public void failed(String msg) {
                getView().showError(msg);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
    }

}
