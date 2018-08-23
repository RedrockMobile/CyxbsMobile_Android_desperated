package com.mredrock.cyxbs.freshman.mvp.contract;

import com.mredrock.cyxbs.freshman.bean.ChatOnline;

public class ChatOnlineContract {
    public interface IChatOnlineModel extends BaseContract.ISomethingModel {
        void setItem(ChatOnline bean, LoadCallBack callBack);

        void error(String str, LoadCallBack callBack);

        void LoadData(String index, String key, LoadCallBack callBack);
    }

    public interface IChatOnlineView extends BaseContract.ISomethingView {
        void setData(ChatOnline bean);
    }
}
