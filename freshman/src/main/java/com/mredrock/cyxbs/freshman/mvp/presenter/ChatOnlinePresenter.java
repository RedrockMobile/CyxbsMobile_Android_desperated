package com.mredrock.cyxbs.freshman.mvp.presenter;

import com.mredrock.cyxbs.freshman.bean.ChatOnline;
import com.mredrock.cyxbs.freshman.mvp.contract.BaseContract;
import com.mredrock.cyxbs.freshman.mvp.contract.ChatOnlineContract;


public class ChatOnlinePresenter extends BasePresenter<ChatOnlineContract.IChatOnlineView> {
    private ChatOnlineContract.IChatOnlineModel model;

    public ChatOnlinePresenter(ChatOnlineContract.IChatOnlineModel model) {
        this.model = model;
    }

    public void search(String index, String key) {
        model.LoadData(index, key, new BaseContract.ISomethingModel.LoadCallBack() {
            @Override
            public void succeed(Object o) {
                getView().setData((ChatOnline) o);
            }

            @Override
            public void failed(String msg) {

            }
        });
    }
}
