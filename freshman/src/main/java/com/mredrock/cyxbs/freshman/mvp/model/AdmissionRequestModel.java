package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.mvp.contract.AdmissionRequestContract;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.kt.Level;
import com.mredrock.cyxbs.freshman.utils.kt.LogBuilder;
import com.mredrock.cyxbs.freshman.utils.net.Const;
import com.mredrock.cyxbs.freshman.utils.net.HttpLoader;

public class AdmissionRequestModel implements AdmissionRequestContract.IAdmissionRequestModel {

    private LogBuilder log = new LogBuilder("AdmissionRequest", Level.ALL);

    @Override
    public void loadData(LoadCallBack callBack) {
//      先从缓存中读取，加入没有再从网络中获取
        Description mDatas = SPHelper.getBean("admission", "admission", Description.class);
        if (mDatas == null) {
            HttpLoader.<Description>get(
                    service -> service.getDescriptions(Const.INDEX_REQUIRED),
                    item -> setItem(item, callBack),
                    error -> error(error.toString(), callBack)
            );
        } else {
            callBack.succeed(mDatas);
        }

    }

    @Override
    public void setItem(Description description, LoadCallBack callBack) {
        log.i(description.toString());
        for (Description.DescribeBean m : description.getDescribe()) {
            m.setCheck(false);
            m.setDelete(false);
            m.setOpen(false);
        }
        callBack.succeed(description);
    }

    @Override
    public void error(String str, LoadCallBack callBack) {
        callBack.failed(str);
        log.e(str);
    }
}
