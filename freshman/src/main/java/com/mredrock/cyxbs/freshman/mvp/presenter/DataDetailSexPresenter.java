package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.SexProportion;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.DataDetailSexContract;
import com.mredrock.cyxbs.freshman.mvp.model.DataDetailSexModel;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

/*
 by Cynthia at 2018/8/17
 description : 
 */
public class DataDetailSexPresenter extends BasePresenter<DataDetailSexContract.IDataDetailSexView> {

    private DataDetailSexModel model;
    private SexProportion sex;

    public DataDetailSexPresenter(DataDetailSexModel model) {
        this.model = model;
    }

    public void start() {
        model.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                sex = (SexProportion) o;
                if (getView() != null) {
                    getView().loadSexView(sex);
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.show(App.getContext().getResources().getString(R.string.freshman_error_soft));
            }
        });
    }

}
