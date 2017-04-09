package com.mredrock.cyxbs.ui.activity.explore.electric;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ElectricRemindActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_title)
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_remind);
        ButterKnife.bind(this);
        titleTextView.setText("设置提醒");
    }

    @OnClick(R.id.toolbar_iv_left)
    public void onBackClick(){
        onBackPressed();
    }
}
