package com.mredrock.cyxbs.freshman.mvp.model;

import android.util.Log;

import com.mredrock.cyxbs.freshman.bean.MilitaryShow;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryShowContract;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.net.APIService;
import com.mredrock.cyxbs.freshman.utils.net.HttpLoader;

public class MilitaryShowModel implements MilitaryShowContract.IMilitaryShowModel {


    @Override
    public void loadData(LoadCallBack callBack) {
        MilitaryShow show = SPHelper.getBean("军训特辑", MilitaryShow.class);
        if (show == null) {
            Log.d("fxy", "loadData: 网络");
            HttpLoader.<MilitaryShow>get(
                    APIService::getMilitaryShow,
                    item -> setItem(item, callBack),
                    error -> error(error.toString(), callBack)
            );
        } else {
            Log.d("fxy", "loadData: 缓存");
            setItem(show, callBack);
        }
    }

    @Override
    public void setItem(MilitaryShow bean, LoadCallBack callBack) {
        Log.d("fxy", "setItem: " + bean.getPicture().size());
        SPHelper.putBean("军训特辑", bean);
        callBack.succeed(bean);
    }

    @Override
    public void error(String str, LoadCallBack callBack) {
        callBack.failed(str);
    }


}
