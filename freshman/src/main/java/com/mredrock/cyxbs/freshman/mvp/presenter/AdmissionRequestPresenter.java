package com.mredrock.cyxbs.freshman.mvp.presenter;

import android.content.Context;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.mvp.contract.AdmissionRequestContract;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.ui.widget.ARHintDialog;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

public class AdmissionRequestPresenter extends BasePresenter<AdmissionRequestContract.IAdmissionRequestView> {
    private AdmissionRequestContract.IAdmissionRequestModel mModel;

    public AdmissionRequestPresenter(AdmissionRequestContract.IAdmissionRequestModel mModel) {
        this.mModel = mModel;

    }

    public void addItem(String str) {
        if (str.equals("")) {
            getView().returnButton();
        } else {
            Description.DescribeBean temp = new Description.DescribeBean();
            temp.setCheck(false);
            temp.setDelete(false);
            temp.setName(str);
            temp.setContent("暂无对应描述");
            temp.setProperty("用户自定义");
            getView().addData(temp);
        }
    }

    public void showDialog(Context context) {
        ARHintDialog mDialog = new ARHintDialog(context);
        mDialog.show();
    }

    public void start() {
        mModel.loadData(new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                Description temp = (Description) o;
                if (getView() != null) {
                    getView().setRv(temp);
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
