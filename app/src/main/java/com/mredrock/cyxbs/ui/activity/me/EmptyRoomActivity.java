package com.mredrock.cyxbs.ui.activity.me;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.tag.FlowLayout;
import com.mredrock.cyxbs.component.widget.tag.TagAdapter;
import com.mredrock.cyxbs.component.widget.tag.TagFlowLayout;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.ListBottomSheetDialog;
import com.mredrock.cyxbs.util.EmptyConverter;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmptyRoomActivity extends BaseActivity implements TagFlowLayout.OnSelectListener {

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
    @Bind(R.id.tv_building)
    TextView mBuildingTv;
    @Bind(R.id.tv_section)
    TextView mSectioinTv;
    @Bind(R.id.query)
    Button mQuery;
    @Bind(R.id.section_tag_layout)
    TagFlowLayout mSectionTagLayout;
    @Bind(R.id.arrow)
    ImageView mArrow;

    private int mBuildNumPosition = -1;
    private Set<Integer> mSectionPosSet;

    /*
     * 需要请求的次数
     */
    private int mNeedReqNum;

    /*
     * 成功请求的次数
     */

    private int mSuccessReqNum;

    private EmptyConverter mConverter;

    @OnClick(R.id.select_building)
    void selectBuilding() {
        final String[] buildings = getResources().getStringArray(R.array.empty_buildings);
//        AlertDialog alertDialog = new AlertDialog.Builder(EmptyRoomActivity.this)
//                .setTitle("选择教学楼")
//                .setItems(buildings, (dialog, which) -> {
//                    mBuildNumPosition = which;
//                    mBuildingTv.setText(buildings[which]);
//                    mBuildingTv.setTextColor(Color.parseColor("#333333"));
//                }).create();
//        alertDialog.show();
        ListBottomSheetDialog dialog = new ListBottomSheetDialog(this);
        dialog.setData(Arrays.asList(buildings));
        NumberPicker
        dialog.show();
    }

    @OnClick(R.id.select_section)
    void selectSection() {
        if (mSectionTagLayout.getVisibility() == View.VISIBLE) {
            mSectionTagLayout.setVisibility(View.GONE);
            mArrow.setRotation(0);
        } else {
            mSectionTagLayout.setVisibility(View.VISIBLE);
            mArrow.setRotation(90);
        }
    }

    @OnClick(R.id.query)
    void query() {
        onCompleteButtonClickListener(mBuildNumPosition, mSectionPosSet);
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
        setupSectionTagLayout(LayoutInflater.from(this), getResources().getStringArray(R.array.empty_sections));
    }


    public void onCompleteButtonClickListener(int buildNumPosition, Set<Integer> sectionPosSet) {
        if (buildNumPosition >= 0) {
            if (sectionPosSet != null && !sectionPosSet.isEmpty()) {
                loadingEmptyData(buildNumPosition, sectionPosSet);
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
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(
                    v -> EmptyRoomActivity.this.finish());
        }
    }

    private void setupSectionTagLayout(LayoutInflater inflater,
                                       final String[] sectionNumArray) {
        mSectionTagLayout.setAdapter(new TagAdapter<String>(sectionNumArray) {
            @Override
            public View getView(FlowLayout parent, int position, String section) {
                TextView tv = (TextView) inflater.inflate(R.layout.item_empty_fab_tag_item,
                        mSectionTagLayout, false);
                tv.setText(section);
                return tv;
            }
        });
        mSectionTagLayout.setOnSelectListener(this);
    }

    /*
    * 加载数据.
    *
    * @param buildNumPosition 教学楼
    * @param sectionPosSet    课时
    */
    private void loadingEmptyData(int buildNumPosition, Set<Integer> sectionPosSet) {
        String buildingNum = buildNumApiArray[buildNumPosition];
        List<String> courseTimeList = new ArrayList<>();
        for (Integer pos : sectionPosSet) {
            courseTimeList.add(sectionNumApiArray[pos]);
        }

        initialise(courseTimeList);

        SchoolCalendar calendar = new SchoolCalendar();
        String week = String.valueOf(calendar.getWeekOfTerm());
        String weekday = String.valueOf(calendar.getDayOfWeek());
        ProgressDialog progressDialog = new ProgressDialog(EmptyRoomActivity.this);
        progressDialog.setMessage("查询中...");
        for (String courseTime : courseTimeList) {
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
                                    mSuccessReqNum++;
                                    mConverter.setEmptyData(strings);
                                    if (mSuccessReqNum == mNeedReqNum) {
                                        sendQueryResult();
                                    }
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
                            }), buildingNum, week, weekday, courseTime);
        }
    }

    private void sendQueryResult() {
        EmptyRoom[] emptyRooms = mConverter.convert().toArray(new EmptyRoom[]{});
        Intent intent = new Intent(this, EmptyRoomResultActivity.class);
        intent.putExtra("data", emptyRooms);
        startActivity(intent);
    }

    /*
     * 请求时，初始化变量.
     */
    private void initialise(List<String> courseTimeList) {
        mSuccessReqNum = 0;
        mConverter = new EmptyConverter();
        mNeedReqNum = courseTimeList.size();
    }

    @Override
    public void onSelected(Set<Integer> selectPosSet) {
        mSectionPosSet = selectPosSet;
        String[] sections = getResources().getStringArray(R.array.empty_sections);
        String str = "";
        if (mSectionPosSet == null || mSectionPosSet.isEmpty()) {
            str = "、请选择时间";
            mSectioinTv.setTextColor(Color.parseColor("#999999"));
        } else {
            for (int i : mSectionPosSet) {
                str += "、" + sections[i];
            }
            mSectioinTv.setTextColor(Color.parseColor("#333333"));
        }
        mSectioinTv.setText(str.substring(1));
    }
}
