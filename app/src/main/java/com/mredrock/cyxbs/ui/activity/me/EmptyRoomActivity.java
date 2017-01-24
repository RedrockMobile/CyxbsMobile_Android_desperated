package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.tag.FlowLayout;
import com.mredrock.cyxbs.component.widget.tag.TagAdapter;
import com.mredrock.cyxbs.component.widget.tag.TagFlowLayout;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.BuildingAdapter;
import com.mredrock.cyxbs.ui.adapter.me.EmptyAdapter;
import com.mredrock.cyxbs.util.EmptyConverter;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.mredrock.cyxbs.APP.getContext;

public class EmptyRoomActivity extends BaseActivity
        implements TagFlowLayout.OnSelectListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    /**
     * 请求时传入的教学楼参数
     */
    public static final String[] buildNumApiArray = {"2", "3", "4", "5", "8"};

    /**
     * 请求时传入的课时参数
     */
    public static final String[] sectionNumApiArray = {"0", "1", "2", "3", "4", "5"};

    //    @Bind(R.id.empty_rfab_layout)
//    RapidFloatingActionLayout mEmptyRfabLayout;
//    @Bind(R.id.empty_rfab)
//    RapidFloatingActionButton mEmptyRfabButton;
//    @Bind(R.id.empty_iv_resultIcon)
//    ImageView mIvResultIcon;
//    @Bind(R.id.empty_tv_searchResult)
//    TextView mTvResult;
    @Bind(R.id.empty_rv)
    RecyclerView mEmptyRecyclerView;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.empty_progress)
    ContentLoadingProgressBar emptyProgress;
    @Bind(R.id.empty_fab_sp_buildings)
    Spinner buildingSpinner;
    @Bind(R.id.empty_fab_section_tagLayout)
    TagFlowLayout sectionTagFlowLayout;
    @Bind(R.id.fab_search)
    FloatingActionButton completeButton;

    private int mBuildNumPosition = -1;
    private Set<Integer> mSectionPosSet;

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
        StatusBarUtil.setTranslucent(this, 50);
        initToolbar();
        setup();
        setupAdapter();
    }


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

//        mEmptyRfabLayout.toggleContent();
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("空教室");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(
                    v -> EmptyRoomActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    public void setup() {
        final List<String> buildNumList = Arrays.asList(getResources().getStringArray(R.array.empty_buildings));
        final String[] sectionNumArray = getResources().getStringArray(R.array.empty_sections);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        setupSpBuildings(buildNumList);
        setupSectionTagLayout(inflater, sectionNumArray);
        final FloatingActionButton completeBtn = completeButton;
        completeBtn.setOnClickListener(this);
    }

    private void setupSpBuildings(final List<String> buildNumList) {
        Spinner mSpBuildings = buildingSpinner;
        BuildingAdapter buildingAdapter = new BuildingAdapter(buildNumList);
        mSpBuildings.setAdapter(buildingAdapter);
        mSpBuildings.setOnItemSelectedListener(this);
    }

    private void setupSectionTagLayout(LayoutInflater inflater,
                                       final String[] sectionNumArray) {
        TagFlowLayout mSectionTagLayout = sectionTagFlowLayout;
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
        mEmptyRoomList.clear();
        mEmptyRoomList.addAll(mConverter.convert());
        mEmptyRoomAdapter.notifyDataSetChanged();
//        mIvResultIcon.setVisibility(View.VISIBLE);
//        mTvResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelected(Set<Integer> selectPosSet) {
        mSectionPosSet = selectPosSet;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mBuildNumPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_search:
                onCompleteButtonClickListener(mBuildNumPosition, mSectionPosSet);
                break;
        }
    }
}
