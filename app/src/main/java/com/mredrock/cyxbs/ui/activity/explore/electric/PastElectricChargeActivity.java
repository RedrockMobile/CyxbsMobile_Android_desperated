package com.mredrock.cyxbs.ui.activity.explore.electric;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.PastElectricChartView;
import com.mredrock.cyxbs.model.ElectricCharge;
import com.mredrock.cyxbs.model.PastElectric;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PastElectricChargeActivity extends BaseActivity {


    PastElectricChartView chartView;
    private List<Double> electricSpends = new ArrayList<>();


    private User mUser;
    private List<PastElectric> pastElectrics = new ArrayList<>();
    private List<String> months = new ArrayList<>();

    @BindView(R.id.tv_past_electric_end)
    TextView mEndTextView;
    @BindView(R.id.tv_past_electric_start)
    TextView mStartTextView;
    @BindView(R.id.tv_past_electric_spend)
    TextView mSpendTextView;

    @BindView(R.id.toolbar_title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_electric_charge);
        ButterKnife.bind(this);
        initView();

        mUser = BaseAPP.getUser(this);
        RequestManager.INSTANCE.queryPastElectricCharge(mUser.stuNum, mUser.idNum,
                new SimpleObserver<PastElectric.PastElectricResultWrapper>(this, true, new SubscriberListener<PastElectric.PastElectricResultWrapper>() {
                    @Override
                    public void onNext(PastElectric.PastElectricResultWrapper pastElectricResultWrapper) {
                        super.onNext(pastElectricResultWrapper);
                        pastElectrics.clear();

                        ElectricCharge electricCharge = pastElectricResultWrapper.getResult().getCurrent();
                        if (electricCharge == null)
                            Toast.makeText(BaseAPP.getContext(),"没有获取到数据，请检查设置的寝室号",Toast.LENGTH_SHORT).show();
                        else {
                            for (int i = pastElectricResultWrapper.getResult().getTrend().size(); i > 0; i--) {
                                electricSpends.add((double) pastElectricResultWrapper.getResult().getTrend().get(i - 1).getSpend());
                                pastElectrics.add(pastElectricResultWrapper.getResult().getTrend().get(i - 1));
                                months.add(pastElectricResultWrapper.getResult().getTrend().get(i - 1).getTime() + "月");
                            }
                            electricSpends.add(Double.parseDouble(electricCharge.getElectricSpend()));
                            PastElectric pastElectric = new PastElectric();
                            pastElectric.setElectricEnd(electricCharge.getElectricEnd());
                            pastElectric.setSpend(Integer.parseInt(electricCharge.getElectricSpend()));
                            pastElectric.setElectricStart(electricCharge.getElectricStart());
                            pastElectrics.add(pastElectric);
                            months.add(electricCharge.getElectricMonth() +"月");
                            chartView.setxValue(months);
                            chartView.setyValue(electricSpends);
                        }

                  }
                }));


    }

    @OnClick(R.id.toolbar_iv_left)
    public void onBackClick() {
        onBackPressed();
    }

    private void initView() {
        title.setText("往期电费");
        chartView = (PastElectricChartView) findViewById(R.id.past_electric_chart_view);
        chartView.setCallBack(new PastElectricChartView.ItemClickCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(int position) {
                mEndTextView.setText(pastElectrics.get(position).getElectricEnd() + "度");
                mStartTextView.setText(pastElectrics.get(position).getElectricStart() + "度");
                mSpendTextView.setText(pastElectrics.get(position).getSpend() + "度");

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // chartView.requestLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        chartView.setNeedDraw(false);
    }
}
