package com.mredrock.cyxbs.freshman.mvp.contract;

import android.view.View;

import com.mredrock.cyxbs.freshman.bean.Description;

public class AdmissionRequestContract {
    public interface IAdmissionRequestModel extends BaseContract.ISomethingModel {
        void setItem(Description description, LoadCallBack callBack);

        void error(String str, LoadCallBack callBack);
    }

    public interface IAdmissionRequestView extends BaseContract.ISomethingView {
        void showError();

        void initWindow(View.OnClickListener listener);

        void setRv(Description description);

        void prepareAddData();

        void addData(Description.DescribeBean temp);

        void returnButton();

        void scrollToPos(int pos);
    }
}
