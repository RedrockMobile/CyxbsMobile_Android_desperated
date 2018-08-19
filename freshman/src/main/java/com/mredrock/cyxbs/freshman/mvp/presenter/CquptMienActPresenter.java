package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.MienStu;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.CquptMienBaseContract;
import com.mredrock.cyxbs.freshman.mvp.model.CquptMienBaseModel;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

public class CquptMienActPresenter extends BasePresenter<CquptMienBaseContract.ICquptMienActView> {
    private CquptMienBaseModel model;

    public CquptMienActPresenter(CquptMienBaseModel model) {
        this.model = model;
    }

    public void start() {
        checkIsAttach();
        model.LoadAnotherData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                MienStu stu = (MienStu) o;
                if (stu != null) {
                    getView().setData(stu);
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtils.show(App.getContext().getResources().getString(R.string.freshman_error_soft));
            }
        });
    }

}
