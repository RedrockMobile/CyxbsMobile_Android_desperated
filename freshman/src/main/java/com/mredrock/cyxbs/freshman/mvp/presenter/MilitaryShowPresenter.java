package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.MilitaryShow;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryShowContract;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

public class MilitaryShowPresenter extends BasePresenter<MilitaryShowContract.IMilitaryShowView> {
    private MilitaryShowContract.IMilitaryShowModel model;

    public static final String TAG = "MilitaryShowPresenter";

    public MilitaryShowPresenter(MilitaryShowContract.IMilitaryShowModel model) {
        this.model = model;
    }

    public void start() {
        checkIsAttach();
        model.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                MilitaryShow bean = (MilitaryShow) o;
                getView().setData(bean);
            }

            @Override
            public void failed(String msg) {
                ToastUtils.show(App.getContext().getResources().getString(R.string.freshman_error_soft));
            }
        });
    }
}
