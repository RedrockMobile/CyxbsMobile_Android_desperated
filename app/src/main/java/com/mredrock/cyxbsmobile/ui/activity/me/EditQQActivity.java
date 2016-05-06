package com.mredrock.cyxbsmobile.ui.activity.me;

import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.network.RequestManager;

import rx.Subscriber;

public class EditQQActivity extends EditCommonActivity {

    @Override
    protected void provideData(Subscriber<RedrockApiWrapper<Object>> subscriber, String
            stuNum, String idNum, String info) {
        RequestManager.getInstance().setPersonQQ(subscriber, stuNum, idNum, info);
    }


    @Override
    protected String getExtra() {
        return Const.Extras.EDIT_QQ;
    }
}
