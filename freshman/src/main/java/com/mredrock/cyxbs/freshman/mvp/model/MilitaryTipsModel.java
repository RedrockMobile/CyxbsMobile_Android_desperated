package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.mvp.contract.MilitaryTipsContract;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.net.Const;
import com.mredrock.cyxbs.freshman.utils.net.HttpLoader;

public class MilitaryTipsModel implements MilitaryTipsContract.IMilitaryTipsModel {

    @Override
    public void loadData(LoadCallBack callBack) {
        Description description = SPHelper.getBean(Const.INDEX_MILITARY_TRAINING, Description.class);
        if (description == null) {
            HttpLoader.<Description>get(
                    service -> service.getDescriptions(Const.INDEX_MILITARY_TRAINING),
                    item -> setItem(item, callBack),
                    error -> error(error.toString(), callBack)
            );
        } else {
            setItem(description, callBack);
        }
    }


    @Override
    public void setItem(Description description, LoadCallBack callBack) {
        for (Description.DescribeBean bean : description.getDescribe()) {
            bean.setCheck(false);
            bean.setDelete(false);
        }
        callBack.succeed(description);
        SPHelper.putBean(Const.INDEX_MILITARY_TRAINING, description);
    }

    @Override
    public void error(String str, LoadCallBack callBack) {
        callBack.failed(str);
    }
}
