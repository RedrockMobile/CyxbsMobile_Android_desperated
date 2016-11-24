package com.mredrock.cyxbs.ui.activity.me;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.RedrockApiWrapper;
import com.mredrock.cyxbs.network.RequestManager;
import com.umeng.analytics.MobclickAgent;

import rx.Subscriber;

public class EditQQActivity extends EditCommonActivity {


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


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
