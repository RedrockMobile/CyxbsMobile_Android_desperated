package com.mredrock.cyxbs.ui.activity.me;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.PickerBottomSheetDialog;
import com.mredrock.cyxbs.util.EmptyConverter;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmptyRoomActivity extends BaseActivity {

    /**
     * 请求时传入的教学楼参数
     */
    public static final String[] buildNumApiArray = {"2", "3", "4", "5", "8"};

    /**
     * 请求时传入的课时参数
     */
    public static final String[] sectionNumApiArray = {"0", "1", "2", "3", "4", "5"};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_building)
    TextView mBuildingTv;
    @Bind(R.id.tv_section)
    TextView mSectioinTv;
    @Bind(R.id.query)
    Button mQuery;

    private int mBuildNumPosition = -1;
    private int mSectionPosition = -1;

    private EmptyConverter mConverter;

    @OnClick(R.id.select_building)
    void selectBuilding() {
        final String[] buildings = getResources().getStringArray(R.array.empty_buildings);
        PickerBottomSheetDialog dialog = new PickerBottomSheetDialog(this);
        dialog.setData(buildings);
        dialog.setOnClickListener(new PickerBottomSheetDialog.OnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String value, int position) {
                mBuildingTv.setText(value);
                mBuildingTv.setTextColor(Color.parseColor("#333333"));
                mBuildNumPosition = position;
            }
        });
        dialog.show();
    }

    @OnClick(R.id.select_section)
    void selectSection() {
        final String[] buildings = getResources().getStringArray(R.array.empty_sections);
        PickerBottomSheetDialog dialog = new PickerBottomSheetDialog(this);
        dialog.setData(buildings);
        dialog.setOnClickListener(new PickerBottomSheetDialog.OnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String value, int position) {
                mSectioinTv.setText(value);
                mSectioinTv.setTextColor(Color.parseColor("#333333"));
                mSectionPosition = position;
            }
        });
        dialog.show();
    }

    @OnClick(R.id.query)
    void query() {
        onCompleteButtonClickListener(mBuildNumPosition, mSectionPosition);
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_room);
        ButterKnife.bind(this);
        initToolbar();
    }


    public void onCompleteButtonClickListener(int buildNumPosition, int sectionPosition) {
        if (buildNumPosition >= 0) {
            if (sectionPosition >= 0) {
                loadingEmptyData(buildNumPosition, sectionPosition);
            } else {
                Toast.makeText(this, "请选择课时", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请选择教学楼", Toast.LENGTH_SHORT).show();
        }

//        mEmptyRfabLayout.toggleContent();
    }


    private void initToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            mToolbarTitle.setText("空教室");
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(
                    v -> EmptyRoomActivity.this.finish());
        }
    }

    /*
    * 加载数据.
    *
    * @param buildNumPosition 教学楼
    * @param sectionPosition    课时
    */
    private void loadingEmptyData(int buildNumPosition, int sectionPosition) {
        String buildingNum = buildNumApiArray[buildNumPosition];
        String sectionNum = sectionNumApiArray[sectionPosition];

        mConverter = new EmptyConverter();

        SchoolCalendar calendar = new SchoolCalendar();
        String week = String.valueOf(calendar.getWeekOfTerm());
        String weekday = String.valueOf(calendar.getDayOfWeek());
        ProgressDialog progressDialog = new ProgressDialog(EmptyRoomActivity.this);
        progressDialog.setMessage("查询中...");
        RequestManager.getInstance().getEmptyRoomList(
                new SimpleSubscriber<>(this,
                        new SubscriberListener<List<String>>() {

                            @Override
                            public void onStart() {
                                super.onStart();
                                progressDialog.show();
                            }


                            @Override
                            public void onNext(List<String> strings) {
                                super.onNext(strings);
                                mConverter.setEmptyData(strings);
                                sendQueryResult();
                                progressDialog.dismiss();
                            }


                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                            }


                            @Override
                            public boolean onError(Throwable e) {
                                super.onError(e);
                                progressDialog.dismiss();
                                return false;
                            }
                        }), buildingNum, week, weekday, sectionNum);
    }

    private void sendQueryResult() {
        EmptyRoom[] emptyRooms = mConverter.convert().toArray(new EmptyRoom[]{});
        Intent intent = new Intent(this, EmptyRoomResultActivity.class);
        intent.putExtra("data", emptyRooms);
        startActivity(intent);
    }
}
