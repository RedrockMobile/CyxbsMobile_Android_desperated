package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.RapidFloatingContentListView;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.EmptyAdapter;
import com.mredrock.cyxbs.util.EmptyConverter;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EmptyRoomActivity extends BaseActivity
        implements RapidFloatingContentListView.OnCompleteButtonClickListener {

    /**
     * 请求时传入的教学楼参数
     */
    public static final String[] buildNumApiArray = {"2", "3", "4", "5", "8"};

    /**
     * 请求时传入的课时参数
     */
    public static final String[] sectionNumApiArray = {"0", "1", "2", "3", "4", "5"};

    @Bind(R.id.empty_rfab_layout)
    RapidFloatingActionLayout mEmptyRfabLayout;
    @Bind(R.id.empty_rfab)
    RapidFloatingActionButton mEmptyRfabButton;
    @Bind(R.id.empty_iv_resultIcon)
    ImageView mIvResultIcon;
    @Bind(R.id.empty_tv_searchResult)
    TextView mTvResult;
    @Bind(R.id.empty_rv)
    RecyclerView mEmptyRecyclerView;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.empty_progress)
    ContentLoadingProgressBar emptyProgress;

    /**
     * 需要请求的次数
     */
    private int mNeedReqNum;

    /**
     * 成功请求的次数
     */

    private int mSuccessReqNum;

    private List<EmptyRoom> mEmptyRoomList;
    private EmptyAdapter mEmptyRoomAdapter;

    private EmptyConverter mConverter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_room);
        ButterKnife.bind(this);
        initToolbar();
        setupRFAB();
        setupAdapter();
        if (mEmptyRoomList != null && mEmptyRoomList.isEmpty()) {
            Toast.makeText(this, "右下角，选择教室和教学楼", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCompleteButtonClickListener(int buildNumPosition, Set<Integer> sectionPosSet) {
        if (buildNumPosition != -1 && buildNumPosition != 0) {
            if (sectionPosSet != null && !sectionPosSet.isEmpty()) {
                loadingEmptyData(buildNumPosition, sectionPosSet);
            } else {
                Toast.makeText(this, "请选择课时", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请选择教学楼", Toast.LENGTH_SHORT).show();
        }

        mEmptyRfabLayout.toggleContent();
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("空教室");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> EmptyRoomActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }


    private void setupRFAB() {
        mEmptyRfabLayout.setIsContentAboveLayout(false);
        mEmptyRfabLayout.setDisableContentDefaultAnimation(true);

        RapidFloatingContentListView content = new RapidFloatingContentListView(
                this);
        content.setOnCompleteButtonClickListener(this);

        RapidFloatingActionHelper rfabHelper = new RapidFloatingActionHelper(
                this, mEmptyRfabLayout, mEmptyRfabButton, content);
        rfabHelper.build();
    }


    private void setupAdapter() {
        mEmptyRoomList = new ArrayList<>();
        mEmptyRoomAdapter = new EmptyAdapter(mEmptyRoomList, this);
        mEmptyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEmptyRecyclerView.setAdapter(mEmptyRoomAdapter);
    }


    /**
     * 加载数据.
     *
     * @param buildNumPosition 教学楼
     * @param sectionPosSet    课时
     */
    private void loadingEmptyData(int buildNumPosition, Set<Integer> sectionPosSet) {
        String buildingNum = buildNumApiArray[buildNumPosition - 1];
        List<String> courseTimeList = new ArrayList<>();
        for (Integer pos : sectionPosSet) {
            courseTimeList.add(sectionNumApiArray[pos]);
        }

        initialise(courseTimeList);

        SchoolCalendar calendar = new SchoolCalendar();
        String week = String.valueOf(calendar.getWeekOfTerm());
        String weekday = String.valueOf(calendar.getDayOfWeek());
        for (String courseTime : courseTimeList) {
            RequestManager.getInstance().getEmptyRoomList(
                    new SimpleSubscriber<>(this,
                            new SubscriberListener<List<String>>() {

                                @Override
                                public void onStart() {
                                    super.onStart();
                                    emptyProgress.setVisibility(View.VISIBLE);
                                }


                                @Override
                                public void onNext(List<String> strings) {
                                    super.onNext(strings);
                                    mSuccessReqNum++;
                                    mConverter.setEmptyData(strings);
                                    if (mSuccessReqNum == mNeedReqNum) {
                                        mEmptyRoomList.clear();
                                        updateEmptyAdapter();
                                    }
                                    emptyProgress.setVisibility(View.GONE);
                                }


                                @Override
                                public void onCompleted() {
                                    super.onCompleted();
                                }


                                @Override
                                public boolean onError(Throwable e) {
                                    super.onError(e);
                                    emptyProgress.setVisibility(View.GONE);
                                    return false;
                                }
                            }), buildingNum, week, weekday, courseTime);
        }
    }


    /**
     * 请求时，初始化变量.
     */
    private void initialise(List<String> courseTimeList) {
        mSuccessReqNum = 0;
        mConverter = new EmptyConverter();
        mNeedReqNum = courseTimeList.size();
    }


    private void updateEmptyAdapter() {
        mEmptyRoomList.addAll(mConverter.convert());
        mEmptyRoomAdapter.notifyDataSetChanged();
        mIvResultIcon.setVisibility(View.VISIBLE);
        mTvResult.setVisibility(View.VISIBLE);
    }

}
