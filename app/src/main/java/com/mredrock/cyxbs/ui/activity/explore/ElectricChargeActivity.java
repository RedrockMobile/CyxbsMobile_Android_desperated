package com.mredrock.cyxbs.ui.activity.explore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.ElectricCharge;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;

public class ElectricChargeActivity extends AppCompatActivity {
    private static final String TAG = "ElectricChargeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_charge);
        RequestManager.INSTANCE.queryElectricCharge(new SimpleSubscriber<ElectricCharge>(this, new SubscriberListener<ElectricCharge>() {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public boolean onError(Throwable e) {
                return super.onError(e);
            }

            @Override
            public void onNext(ElectricCharge electricCharge) {
                super.onNext(electricCharge);
                Log.d(TAG, "onNext: " + electricCharge.getRecordTime() +"   " + electricCharge.getElectricSpend());
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        }),"28","209");
    }
}
