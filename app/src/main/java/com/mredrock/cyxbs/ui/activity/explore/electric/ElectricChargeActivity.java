package com.mredrock.cyxbs.ui.activity.explore.electric;

import android.os.Bundle;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ElectricCircleView;
import com.mredrock.cyxbs.model.ElectricCharge;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.SPUtils;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ElectricChargeActivity extends BaseActivity {
    private static final String TAG = "ElectricChargeActivity";
    private String buildingNum;
    private String dormitoryNum;
    private ElectricCharge electricCharge;

    @Bind(R.id.ecv_electric_circle_view)
    ElectricCircleView electricCircleView;
    @Bind(R.id.toolbar_title)
    TextView mToolbarText;
    @Bind(R.id.tv_electric_query_notice)
    TextView mNoticeText;
    @Bind(R.id.tv_electric_query_begin)
    TextView mBeginText;
    @Bind(R.id.tv_electric_query_free)
    TextView mFreeText;
    @Bind(R.id.tv_electric_query_end)
    TextView mEndText;
    @Bind(R.id.tv_electric_query_average)
    TextView mAverageText;


    private String noticeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_charge);
        StatusBarUtil.setTranslucent(this, 50);
        ButterKnife.bind(this);
        initView();
        initData();
        LogUtils.LOGE(TAG, buildingNum + ": " + dormitoryNum);
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
                ElectricChargeActivity.this.electricCharge = electricCharge;
                if (electricCharge != null)
                    updateView();

            }


            @Override
            public void onStart() {
                super.onStart();
            }
        }), buildingNum, dormitoryNum);
    }

    private void initView() {
        mToolbarText.setText("查电费");
    }

    private void updateView() {
        String data = electricCharge.getElectricCost().get(0) + "." + electricCharge.getElectricCost().get(1);
        electricCircleView.drawWithData(Float.parseFloat(data), data, electricCharge.getElectricSpend());
        String recordTime = electricCharge.getRecordTime();

        if (recordTime != null && electricCharge.getElectricSpend() != null) {
            mNoticeText.setText(noticeInfo + "" + recordTime);
            int beginIndex = recordTime.indexOf("月");
            int endIndex = recordTime.indexOf("日");
            float days = Float.parseFloat(recordTime.substring(beginIndex + 1,endIndex));
            float average = Float.parseFloat(electricCharge.getElectricSpend())  / days;
            DecimalFormat df = new DecimalFormat("#.00");
            mAverageText.setText(df.format(average));

        }

        if (electricCharge.getElectricSpend() != null && electricCharge.getElectricEnd() != null && electricCharge.getElectricFree() != null) {
            mBeginText.setText(electricCharge.getElectricStart());
            mEndText.setText(electricCharge.getElectricEnd());
            mFreeText.setText(electricCharge.getElectricFree());
        }


    }

    private void initData() {
        buildingNum = (String) SPUtils.get(APP.getContext(), DormitorySettingActivity.BUILDING_KEY, String.valueOf(28));
        dormitoryNum = (String) SPUtils.get(APP.getContext(), DormitorySettingActivity.DORMITORY_KEY, String.valueOf(999));
        noticeInfo = getResources().getString(R.string.electric_notice_info);
    }
}
