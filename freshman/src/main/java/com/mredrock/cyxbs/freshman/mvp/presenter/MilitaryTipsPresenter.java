package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryTipsContract;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

public class MilitaryTipsPresenter extends BasePresenter<MilitaryTipsContract.IMilitaryTipsView> {
    private MilitaryTipsContract.IMilitaryTipsModel model;

    public MilitaryTipsPresenter(MilitaryTipsContract.IMilitaryTipsModel model) {
        this.model = model;
    }

    public void start() {
        checkIsAttach();
        model.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                Description description = (Description) o;
                getView().setData(description);
            }

            @Override
            public void failed(String msg) {
                ToastUtils.show(App.getContext().getResources().getString(R.string.freshman_error_soft));
            }
        });
    }
}
