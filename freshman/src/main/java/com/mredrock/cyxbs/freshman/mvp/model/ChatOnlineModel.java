package com.mredrock.cyxbs.freshman.mvp.model;

import com.mredrock.cyxbs.freshman.bean.ChatOnline;
import com.mredrock.cyxbs.freshman.mvp.contract.ChatOnlineContract;
import com.mredrock.cyxbs.freshman.utils.net.HttpLoader;

public class ChatOnlineModel implements ChatOnlineContract.IChatOnlineModel {

    @Override
    public void setItem(ChatOnline bean, LoadCallBack callBack) {
        callBack.succeed(bean);
    }

    @Override
    public void error(String str, LoadCallBack callBack) {
        callBack.failed(str);
    }

    @Override
    public void LoadData(String index, String key, LoadCallBack callBack) {
        HttpLoader.<ChatOnline>get(
                service -> service.getChatOnline(index, key),
                item -> setItem(item, callBack),
                error -> error(error.getMessage(), callBack)
        );
    }

    @Override
    public void loadData(LoadCallBack callBack) {
        //NO USE
    }
}
