package com.mredrock.cyxbs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.umeng.message.UmengNotifyClickActivity;
import com.umeng.message.common.UmLog;

import org.android.agoo.common.AgooConstants;

/**
 * Created by simonla on 2017/10/8.
 */

public class MipushTestActivity extends UmengNotifyClickActivity {

    private static String TAG = MipushTestActivity.class.getName();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        UmLog.i(TAG, body);
        Message message = Message.obtain();
        message.obj = body;
    }
}