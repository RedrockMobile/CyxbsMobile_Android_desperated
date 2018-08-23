package com.mredrock.cyxbs.freshman.mvp.contract;

import com.mredrock.cyxbs.freshman.bean.SexProportion;

/*
 by Cynthia at 2018/8/17
 description : 
 */
public class DataDetailSexContract {
    public interface IDataDetailSexModel extends BaseContract.ISomethingModel {
        void setSex(SexProportion sex, LoadCallBack callBack);

        void error(String error, LoadCallBack callBack);
    }

    public interface IDataDetailSexView extends BaseContract.ISomethingView {
        void showError(String msg);

        void loadSexView(SexProportion sexProportion);
    }
}
