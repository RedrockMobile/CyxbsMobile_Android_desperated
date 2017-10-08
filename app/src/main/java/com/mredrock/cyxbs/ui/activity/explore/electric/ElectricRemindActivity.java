package com.mredrock.cyxbs.ui.activity.explore.electric;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.ElectricRemindUtil;
import com.mredrock.cyxbs.util.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ElectricRemindActivity extends AppCompatActivity {

    public final static String ELECTRIC_REMIND_MONEY = "sp_eletric_money";


    @Bind(R.id.toolbar_title)
    TextView titleTextView;
    
    @Bind(R.id.et_electric_remind_money)
    EditText mMoneyEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_remind);
        ButterKnife.bind(this);
        initView();
        titleTextView.setText("设置提醒");
    }

    private void initView() {
        float money = (float) SPUtils.get(BaseAPP.getContext(),ELECTRIC_REMIND_MONEY,-1.0f);
        if (money != -1)
            mMoneyEdit.setText(money+"");
    }

    @OnClick(R.id.toolbar_iv_left)
    public void onBackClick(){
        onBackPressed();
    }

    @OnClick(R.id.iv_electric_remind_confirm)
    public void onConfirmClick(){
        if (mMoneyEdit.getText().toString().isEmpty()){
            Toast.makeText(BaseAPP.getContext(),"要设置电费提醒额度哦",Toast.LENGTH_SHORT).show();
            return;
        }
        SPUtils.set(BaseAPP.getContext(), ElectricRemindUtil.SP_KEY_ELECTRIC_REMIND_TIME, System.currentTimeMillis() / 2);
        SPUtils.set(BaseAPP.getContext(),ELECTRIC_REMIND_MONEY,Float.parseFloat(mMoneyEdit.getText().toString()));
        onBackClick();
    }
}
