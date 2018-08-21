package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.StrategyData;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.ReportingProcessContract;
import com.mredrock.cyxbs.freshman.mvp.model.ReportingProcessModel;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

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
        model.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                StrategyData data = (StrategyData) o;
                if (getView() != null) {
                    getView().setData(data);
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.show(App.getContext().getResources().getString(R.string.freshman_error_soft));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
    }

}
