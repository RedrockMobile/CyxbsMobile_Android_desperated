package com.mredrock.cyxbs.ui.activity.lost;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.google.gson.Gson;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.lost.LostDetail;
import com.mredrock.cyxbs.model.lost.LostStatus;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.LostCircleButton;
import com.mredrock.cyxbs.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.HttpException;

/**
 * Created by wusui on 2017/2/7.
 */


public class ReleaseActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.edit_describe)
    EditText mDescribe;
    @BindView(R.id.lost_place)
    EditText mPlace;
    @BindView(R.id.lost_tel)
    EditText mTel;
    @BindView(R.id.lost_qq_number)
    EditText mQQ;
    @BindView(R.id.lost_type)
    TextView mType;
    @BindView(R.id.lost_choose_time)
    TextView mTime;
    ImageView mAlertImage;
    TextView mAlertText;
    Button mAlertButton;
    TimePickerView timePickerView;
    OptionsPickerView optionsPickerView;
    private ArrayList<String> type = new ArrayList<>();
    private AlertDialog dialog;
    public LostCircleButton mButton;
    public LostCircleButton mButton1;
    String place, time, connectPhone, category, qq;
    int theme;
    LostDetail detail = new LostDetail();
    private CharSequence temp = "1234567890";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        dialog = new AlertDialog.Builder(ReleaseActivity.this).create();
        initToolbar();
        initButton();
        mDescribe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LogUtils.LOGE("ReleaseActivity", hasFocus + "");
                    if (temp.length() < 10) {
                        Toast.makeText(ReleaseActivity.this, "抱歉，描述不得不少于10个字哦~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mDescribe.addTextChangedListener(new EditChangedListener());

    }

    @SuppressWarnings("ConstantConditions")
    public void initToolbar() {
        mToolbar.setTitle("");
        mToolbarTitle.setText("信息发布");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void initButton() {
        mButton = (LostCircleButton) findViewById(R.id.button_find);
        mButton1 = (LostCircleButton) findViewById(R.id.button_lost);
        mButton.setTextColori(Color.WHITE);
        mButton.setShape(GradientDrawable.OVAL);
        mButton.setFillet(true);
        mButton.setBackColor(Color.parseColor("#bfbfbf"));
        mButton.setBackColorSelected(Color.parseColor("#41a3ff"));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = 1;
                mButton1.setBackColor(Color.parseColor("#bfbfbf"));
            }
        });
        mButton1.setTextColori(Color.WHITE);
        mButton1.setShape(GradientDrawable.OVAL);
        mButton1.setFillet(true);
        mButton1.setBackColor(Color.parseColor("#41a3ff"));
        mButton1.setBackColorSelected(Color.parseColor("#41a3ff"));
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = 0;
                mButton.setBackColor(Color.parseColor("#bfbfbf"));
            }
        });

    }

    @OnClick(R.id.rl_activity_release_distinguish_container)
    public void chooseDistinguish() {
        if (optionsPickerView == null) {
            type.add("一卡通");
            type.add("钱包");
            type.add("电子产品");
            type.add("书包");
            type.add("钥匙");
            type.add("雨伞");
            type.add("衣物");
            type.add("其它");
            optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    category = type.get(options1);
                    mType.setText(category);
                }
            }).setDividerType(WheelView.DividerType.WARP)
                    .setCancelText("取消")
                    .setSubmitText("确定")
                    .setContentTextSize(30)
                    .setSelectOptions(1)
                    .build();
        }
        optionsPickerView.setPicker(type);
        optionsPickerView.show();
    }

    @OnClick(R.id.rl_activity_release_choose_time_container)
    public void chooseTime() {
        Calendar calendar = Calendar.getInstance();

        if (timePickerView == null) {
            timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    time = getTime(date);
                    mTime.setText(time);
                }
            }).setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR))
                    .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                    .setContentSize(30)
                    .setCancelText("取消")
                    .setSubmitText("确定")
                    .setLabel("", "", "", "", "", "").build();
        }
        timePickerView.show();
    }

    @OnClick(R.id.release_details)
    public void releaseDetails() {
        detail.time = time;
        place = mPlace.getText().toString();
        qq = mQQ.getText().toString();
        connectPhone = mTel.getText().toString();

        if (category != null && category.length() > 0) {
            detail.category = category;
        } else {
            showAlertDialog(R.drawable.img_lost_require_classification, "请选择分类");
            return;
        }

        if (place != null && place.length() > 0) {
            detail.place = place;
        } else {
            showAlertDialog(R.drawable.img_lost_require_location, "请写明失物地点");
            return;
        }

        if (connectPhone != null && connectPhone.length() > 0) {
            detail.connectPhone = connectPhone;
        } else {
            showAlertDialog(R.drawable.img_lost_require_contact, "请留下您的联系方式");
            return;
        }

        if (qq != null && qq.length() > 0) {
            detail.connectWx = qq;
        } else {
            showAlertDialog(R.drawable.img_lost_require_contact, "请留下您的联系方式");
            return;
        }

        RequestManager.getInstance().createLost(new SimpleObserver<LostStatus>(getBaseContext(), new SubscriberListener<LostStatus>() {
            @Override
            public boolean onError(Throwable e) {
                if (e instanceof HttpException) {
                    LostStatus status;
                    try {
                        status = new Gson().fromJson(((HttpException) e).message(), LostStatus.class);
                    } catch (Exception ex) {
                        return false;
                    }
                    Toast.makeText(ReleaseActivity.this, status.status, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onNext(LostStatus lostStatus) {
                super.onNext(lostStatus);
                Intent intent = new Intent(ReleaseActivity.this, ReleaseSucceedActivity.class);
                startActivity(intent);
                ReleaseActivity.super.finish();
            }
        }), detail, theme);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseActivity.class);
        context.startActivity(intent);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(date);
    }

    public void showAlertDialog(int image, String str) {
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_lost_classfication);
        mAlertImage = (ImageView) window.findViewById(R.id.lost_iv_alert);
        mAlertText = (TextView) window.findViewById(R.id.lost_tx_alert);
        mAlertButton = (Button) window.findViewById(R.id.lost_bt_alert);
        mAlertImage.setImageResource(image);
        mAlertText.setText(str);
        mAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    class EditChangedListener implements TextWatcher {

        private int selectionStart;
        private int selectionEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            selectionStart = mDescribe.getSelectionStart();
            selectionEnd = mDescribe.getSelectionEnd();
            if (temp.length() > 100) {
                s.delete(selectionStart - 1, selectionEnd);
                showAlertDialog(R.drawable.img_lost_overfull, "描述内容过多 请删减");

            } else {
                detail.description = mDescribe.getText().toString();
            }
        }
    }

    @Override
    public void finish() {
        if (!(mDescribe == null || mDescribe.getText().toString().isEmpty()) ||
                !(category == null || category.isEmpty()) ||
                !(time == null || time.isEmpty()) ||
                !(mPlace == null || mPlace.getText().toString().isEmpty()) ||
                !(mTel == null || mTel.getText().toString().isEmpty()) ||
                !(mQQ == null || mQQ.getText().toString().isEmpty())) {
            Handler handler = new Handler(getMainLooper());
            handler.post(() -> new MaterialDialog.Builder(this)
                    .title("退出编辑?")
                    .content("是否放弃编辑内容并且退出?")
                    .positiveText("退出")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            ReleaseActivity.super.finish();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    }).show());
            return;
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (timePickerView != null && timePickerView.isShowing()) {
            timePickerView.dismiss();
        } else if (optionsPickerView != null && optionsPickerView.isShowing()) {
            optionsPickerView.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
