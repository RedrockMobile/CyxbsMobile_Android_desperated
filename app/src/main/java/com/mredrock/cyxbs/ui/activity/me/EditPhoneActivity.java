package com.mredrock.cyxbs.ui.activity.me;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.network.RequestManager;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observer;
import kotlin.Unit;


public class EditPhoneActivity extends EditCommonActivity {


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
    protected void provideData(Observer<Unit> observer, String stuNum, String idNum, String info) {
        RequestManager.getInstance().setPersonPhone(observer, stuNum, idNum, info);
    }


    @Override
    protected String getExtra() {
        return Const.Extras.EDIT_PHONE;
    }
}
