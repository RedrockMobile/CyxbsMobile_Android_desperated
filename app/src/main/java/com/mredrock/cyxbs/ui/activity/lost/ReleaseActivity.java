package com.mredrock.cyxbs.ui.activity.lost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.lost.Lost;
import com.mredrock.cyxbs.model.lost.LostDetail;
import com.mredrock.cyxbs.model.lost.LostStatus;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wusui on 2017/2/7.
 */

public class ReleaseActivity extends AppCompatActivity {
    @Bind(R.id.lost_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_describe)
    EditText mDescribe;
    @Bind(R.id.lost_place)
    EditText mPlace;
    @Bind(R.id.lost_tel)
    EditText mTel;
    @Bind(R.id.lost_qq_number)
    EditText mQQ;
    @Bind(R.id.lost_type)
    TextView mType;
    @Bind(R.id.lost_choose_time)
    TextView mTime;
    @Bind(R.id.lost_iv_alert)
    ImageView mAlertImage;
    @Bind(R.id.lost_tx_alert)
    TextView mAlertText;
    @Bind(R.id.lost_bt_alert)
    Button mAlertButton;
    TimePickerView timePickerView;
    OptionsPickerView optionsPickerView;
    private ArrayList<String> type = new ArrayList<>();
    private AlertDialog dialog = new AlertDialog.Builder(ReleaseActivity.this).create();

    String place,time,connectPhone,category,qq;
    int theme;
    LostDetail detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

    }
    @SuppressWarnings("ResourceType")
    @OnClick({R.id.button_lost,R.id.button_find})
    public void chooseType(View view){
        switch (view.getId()){
            case R.id.button_find:
                Button mButton = (Button) findViewById(R.id.button_find);
                mButton.setBackgroundColor(getResources().getColor(R.drawable.circle_bg_blue));
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        theme = 1;
                    }
                });


            default:
                Button mButton1 = (Button) findViewById(R.id.button_lost);
                mButton1.setBackgroundColor(getResources().getColor(R.drawable.circle_bg_blue));
                mButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        theme = 0;
                    }
                });
        }
    }
    @OnClick(R.id.image_distinguish)
    public void chooseDistinguish(){
        type.add("一卡通");
        type.add("钱包");
        type.add("电子产品");
        type.add("书包");
        type.add("钥匙");
        type.add("雨伞");
        type.add("衣物");
        type.add("其它");
        optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(type);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setSelectOptions(1);
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                category = type.get(options1);
                mType.setText(category);
            }
        });
        optionsPickerView.show();
    }
    @OnClick(R.id.choose_time)
    public void chooseTime(){
        timePickerView = new TimePickerView(this,TimePickerView.Type.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 20,calendar.get(Calendar.YEAR));
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener( new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                time = getTime(date);
                mTime.setText(time);
            }
        });
        timePickerView.show();//s == null || s.length() <= 0
    }
    @OnClick(R.id.release_details)
    public void releaseDetails(){
        detail.time = time;
        place = mPlace.getText().toString();
        qq =  mQQ.getText().toString();
        connectPhone = mTel.getText().toString();

        if ( place!= null && place.length() > 0)detail.place = place;
        else showAlertDialog(R.drawable.img_lost_require_location,"请写明失物地点");

        if ( connectPhone!= null && connectPhone.length() > 0)detail.connectPhone = connectPhone;
        else showAlertDialog(R.drawable.img_lost_require_contact,"请留下您的联系方式");

        if (category != null && category.length() >0)detail.category = category;
        else showAlertDialog(R.drawable.img_lost_require_classification,"请选择分类");

        if (qq != null && qq.length() > 0)detail.connectWx = qq;
        else showAlertDialog(R.drawable.img_lost_require_contact,"请留下您的联系方式");

        mDescribe.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart ;
            private int selectionEnd ;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                selectionStart = mDescribe.getSelectionStart();
                selectionEnd = mDescribe.getSelectionEnd();
                if (temp.length() > 100) {

                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    mDescribe.setText(editable);
                    mDescribe.setSelection(tempSelection);

                    showAlertDialog(R.drawable.img_lost_overfull,"描述内容过多 请删减");
                }else {
                    detail.description = mDescribe.getText().toString();
                }
            }
        });
        RequestManager.getInstance().createLost(new SimpleSubscriber<LostStatus>(getBaseContext(), new SubscriberListener<LostStatus>() {
            @Override
            public boolean onError(Throwable e) {
                Toast.makeText(ReleaseActivity.this, "抱歉，您的网络似乎不太好哦~再试一遍怎么样╮(￣▽￣)╭", Toast.LENGTH_SHORT).show();
                return super.onError(e);
            }

            @Override
            public void onNext(LostStatus lostStatus) {
                super.onNext(lostStatus);
                Intent intent = new Intent(ReleaseActivity.this,ReleaseSucceedActivity.class);
                startActivity(intent);
            }
        }),detail,theme);
    }
    public static void startActivity(Context context){
        Intent intent = new Intent(context,ReleaseActivity.class);
        context.startActivity(intent);
    }
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
    public void showAlertDialog(int image,String str){
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_lost_classfication);
        mAlertImage.setImageResource(image);
        mAlertText.setText(str);
        mAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
