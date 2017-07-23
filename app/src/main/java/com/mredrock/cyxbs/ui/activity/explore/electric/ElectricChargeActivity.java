package com.mredrock.cyxbs.ui.activity.explore.electric;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ElectricCircleView;
import com.mredrock.cyxbs.model.ElectricCharge;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.util.SPUtils;
import com.mredrock.cyxbs.util.Utils;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ElectricChargeActivity extends BaseActivity {

    public static final int REQUEST_SET_CODE = 1;
    public static final int REQUEST_NOT_SET_CODE = -1;

    private static final String TAG = "ElectricChargeActivity";
    private String buildingNum;
    private String dormitoryNum;
    private ElectricCharge mElectricCharge;

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
    @Bind(R.id.tool_iv_right)
    ImageView mToolbarRightImage;
    @Bind(R.id.electric_query_toolbar)
    View toolbar;

    private PopupWindow popupWindow;

    private String noticeInfo;

    //是否是设置寝室保存信息之后返回回来的
    private boolean onActivityResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_charge);
        ButterKnife.bind(this);
        initView();
        initData();
        queryElectricCharge();

    }


    private void initView() {
        mToolbarText.setText("查电费");
        mToolbarRightImage.setBackgroundResource(R.drawable.ic_more_horiz_black);
    }

    private void initData() {
        buildingNum = (String) SPUtils.get(APP.getContext(), DormitorySettingActivity.BUILDING_KEY, String.valueOf("null"));
        dormitoryNum = (String) SPUtils.get(APP.getContext(), DormitorySettingActivity.DORMITORY_KEY, String.valueOf("null"));
        noticeInfo = getResources().getString(R.string.electric_notice_info);
    }

    private void queryElectricCharge() {
        if (dormitoryNum.equals("null")){
            startActivityForResult(new Intent(this,DormitorySettingActivity.class),1);
            return;
        }
        RequestManager.INSTANCE.queryElectricCharge(new SimpleSubscriber<ElectricCharge>(this,true, new SubscriberListener<ElectricCharge>() {
            @Override
            public void onNext(ElectricCharge electricCharge) {
                super.onNext(electricCharge);
                mElectricCharge = electricCharge;
                if (mElectricCharge != null)
                    updateView();
            }
        }), buildingNum, dormitoryNum);
    }



    private void updateView() {
        String data = mElectricCharge.getElectricCost().get(0) + "." + mElectricCharge.getElectricCost().get(1);
        electricCircleView.drawWithData(Float.parseFloat(data), data, mElectricCharge.getElectricSpend());
        String recordTime = mElectricCharge.getRecordTime();

        if (recordTime != null && mElectricCharge.getElectricSpend() != null) {
            mNoticeText.setText(noticeInfo + "\t\t" + recordTime);
            int beginIndex = recordTime.indexOf("月");
            int endIndex = recordTime.indexOf("日");
            float days = Float.parseFloat(recordTime.substring(beginIndex + 1,endIndex));
            float average = Float.parseFloat(mElectricCharge.getElectricSpend())  / days;
            DecimalFormat df = new DecimalFormat("#.00");
            mAverageText.setText(df.format(average));

        }

        if (mElectricCharge.getElectricSpend() != null && mElectricCharge.getElectricEnd() != null && mElectricCharge.getElectricFree() != null) {
            mBeginText.setText(mElectricCharge.getElectricStart());
            mEndText.setText(mElectricCharge.getElectricEnd());
            mFreeText.setText(mElectricCharge.getElectricFree());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            Log.i(TAG,"onActivityResult");
            if (resultCode == REQUEST_SET_CODE){
                onActivityResult = true;
                buildingNum = (String) SPUtils.get(APP.getContext(), DormitorySettingActivity.BUILDING_KEY, String.valueOf("null"));
                dormitoryNum = (String) SPUtils.get(APP.getContext(), DormitorySettingActivity.DORMITORY_KEY, String.valueOf("null"));
                queryElectricCharge();
            }else
                onActivityResult = false;
        }

    }

    @OnClick(R.id.toolbar_iv_left)
    public void onBackClick(){
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"omResume");
        if (mElectricCharge != null && !onActivityResult) {
            updateView();
        }
        onActivityResult = false;

    }

    @OnClick(R.id.tool_iv_right)
    public void onMenuClick(){
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int xOffset = frame.top + toolbar.getHeight() - 60;//减去阴影宽度，适配UI.
        int yOffset = Utils.dip2px(this, 15f); //设置x方向offset为5dp
        View parentView = getLayoutInflater().inflate(R.layout.activity_electric_charge, null);
        View popView = getLayoutInflater().inflate(
                R.layout.popup_window_eletric_query, null);
        popupWindow= new PopupWindow(popView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);//popView即popupWindow的布局，ture设置focusAble.

        //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效。这里在XML中定义背景，所以这里设置为null;
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setOutsideTouchable(true); //点击外部关闭。
        popupWindow.setAnimationStyle(R.style.PopupAnimation);    //设置一个动画。
        //设置Gravity，让它显示在右上角。
        if (popupWindow.getContentView() != null) {
            popupWindow.getContentView().findViewById(R.id.tv_popup_window_set_room).setOnClickListener((v) ->{
                startActivityForResult(new Intent(this,DormitorySettingActivity.class),1);
                popupWindow.dismiss();
            });

            popupWindow.getContentView().findViewById(R.id.tv_popup_window_past_electric).setOnClickListener((v) ->{
                startActivity(new Intent(this,PastElectricChargeActivity.class));
                popupWindow.dismiss();
            });
            popupWindow.getContentView().findViewById(R.id.tv_popup_window_set_remind).setOnClickListener((v) ->{
                startActivity(new Intent(this,ElectricRemindActivity.class));
                popupWindow.dismiss();
            });
        }
        popupWindow.showAtLocation(parentView, Gravity.RIGHT | Gravity.TOP,
                yOffset, xOffset);

    }

}
