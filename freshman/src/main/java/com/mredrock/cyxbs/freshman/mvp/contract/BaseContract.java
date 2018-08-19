package com.mredrock.cyxbs.freshman.mvp.contract;

import android.content.Context;

/**
 * 用Contract类来管理接口
 **/
public class BaseContract {

    //公用View接口
    public interface ISomethingView {
        Context getContext();
    }

    //公用Model接口
    public interface ISomethingModel {

        //根据自己的需求写方法和回调接口
        void loadData(LoadCallBack callBack);

        interface LoadCallBack {
            void succeed(Object o);//这里改成你想要回调的数据类型

            void failed(String msg);
        }
    }

    //Presenter一般不写接口方便操作
}
