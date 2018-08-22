package com.mredrock.cyxbs.freshman.mvp.contract;

import android.support.v4.app.Fragment;

import com.mredrock.cyxbs.freshman.bean.MienStu;

import java.util.List;

public class CquptMienBaseContract {

    public interface ICquptMienBaseModel extends BaseContract.ISomethingModel {
        void setItem(MienStu bean, LoadCallBack callBack);

        void error(String str, LoadCallBack callBack);

        void LoadAnotherData(LoadCallBack callBack);

        void setAnotherItem(MienStu bean, LoadCallBack callBack);
    }

    public interface ICquptMienBaseView extends BaseContract.ISomethingView {
        void setData(List<Fragment> list, List<String> titles);
    }

    public interface ICquptMienActView extends BaseContract.ISomethingView {
        void setData(MienStu bean);
    }
}
