package com.mredrock.cyxbs.freshman.mvp.contract;

import com.mredrock.cyxbs.freshman.bean.Description;

public class MilitaryTipsContract {

    public interface IMilitaryTipsModel extends BaseContract.ISomethingModel {
        void setItem(Description description, LoadCallBack callBack);

        void error(String str, LoadCallBack callBack);
    }

    public interface IMilitaryTipsView extends BaseContract.ISomethingView {
        void setData(Description data);
    }
}
