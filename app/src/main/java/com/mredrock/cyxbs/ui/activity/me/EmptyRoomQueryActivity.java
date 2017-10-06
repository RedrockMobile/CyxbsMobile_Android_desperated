package com.mredrock.cyxbs.ui.activity.me;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.selector.MultiSelector;
import com.mredrock.cyxbs.component.widget.selector.StringAdapter;
import com.mredrock.cyxbs.component.widget.selector.ViewInitializer;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.EmptyRoomResultAdapter;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmptyRoomQueryActivity extends BaseActivity implements MultiSelector.OnItemSelectedChangeListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Bind(R.id.selector_container)
    ViewGroup mSelectorContainer;
    @Bind(R.id.week_selector)
    MultiSelector mWeekSelector;
    @Bind(R.id.weekday_selector)
    MultiSelector mWeekdaySelector;
    @Bind(R.id.building_selector)
    MultiSelector mBuildingSelector;
    @Bind(R.id.section_selector)
    MultiSelector mSectionSelector;
    @Bind(R.id.arrow)
    ImageView mArrow;
    @Bind(R.id.result)
    RecyclerView mRecyclerView;
    @Bind(R.id.querying)
    ImageView mQuerying;

    private EmptyRoomResultAdapter mResultAdapter;

    private ObjectAnimator mQueryAnimator;
    private ValueAnimator mExpandedAnimator;
    private boolean mExpanded = true;

    private SchoolCalendar mSchoolCalendar;

    private int[] mWeekApi;
    private static final int[] sWeekdayApi = {1, 2, 3, 4, 5, 6, 7};
    private static final int[] sBuildingApi = {2, 3, 4, 5, 8};
    private static final int[] sSectionApi = {0, 1, 2, 3, 4, 5};

    private float mY = -1;

    @OnClick(R.id.arrow)
    void onArrowClick() {
        if (disallowExpandedAnimator()) return;
        if (mExpanded) mExpandedAnimator.start();
        else mExpandedAnimator.reverse();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_room_query);
        ButterKnife.bind(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        init();
    }

    private void init() {
        initToolbar();
        initData();
        initSelectors();
        initExpandedAnimator();
        initQueryingAnimator();
        initRv();
    }

    private void initData() {
        mSchoolCalendar = new SchoolCalendar();
        int week = mSchoolCalendar.getWeekOfTerm();
        List<String> list = new ArrayList<>(mWeekSelector.getDisplayValues());
        //删除第一行的“整学期”
        list.remove(0);
        //对week的值进行修正
        week = week > list.size() ? 0 : week - 1;
        //删除本周之前的周数
        for (int i = 0; i < week; i++) {
            list.remove(0);
        }
        mWeekSelector.setDisplayValues(list);
        mWeekApi = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            mWeekApi[i] = week++;
        }
    }

    private void initSelectors() {
        initSelector(mWeekSelector, mWeekApi, 0, 0);
        initSelector(mWeekdaySelector, sWeekdayApi, mSchoolCalendar.getDayOfWeek() - 1, 1);
        initSelector(mBuildingSelector, sBuildingApi, -1, 2);
        initSelector(mSectionSelector, sSectionApi, -1, 3);
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
                    v -> EmptyRoomQueryActivity.this.finish());
        }
    }

    private void initSelector(MultiSelector selector, int[] values, int defaultSelected, int tag) {
        final int dp_15 = DensityUtils.dp2px(this, 12);
        final int dp_3 = DensityUtils.dp2px(this, 3);
        ViewInitializer initializer = new ViewInitializer.Builder(this)
                .horizontalLinearLayoutManager()
                .gap(dp_15, dp_3, dp_15)
                .stringAdapter(selector, new StringAdapter.LayoutWrapper() {
                    @Override
                    public int getLayoutId() {
                        return R.layout.item_empty_room_query_option;
                    }

                    @Override
                    public int getTextViewId() {
                        return R.id.text;
                    }

                    @Override
                    public void onBindView(TextView textView, String displayValue, boolean selected, int position) {
                        super.onBindView(textView, displayValue, selected, position);
                        Drawable drawable = selected ? getResources().getDrawable(R.drawable.shape_empty_room_query_item) : null;
                        int color = selected ? Color.parseColor("#FFFFFF") : Color.parseColor("#393939");
                        textView.setBackgroundDrawable(drawable);
                        textView.setTextColor(color);
                    }
                }).build();
        selector.setValues(values);
        if (defaultSelected >= 0) {
            selector.setSelected(defaultSelected, true);
        }
        selector.setViewInitializer(initializer);
        selector.setOnItemSelectedChangeListener(this);
        selector.setTag(tag);
    }

    private void initExpandedAnimator() {
        mSelectorContainer.post(() -> {
            ViewGroup.LayoutParams layoutParams = mSelectorContainer.getLayoutParams();
            mExpandedAnimator = ValueAnimator.ofInt(mSelectorContainer.getHeight(), DensityUtils.dp2px(this, 6));
            mExpandedAnimator.setDuration(500);
            mExpandedAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mExpandedAnimator.addUpdateListener(animation -> {
                layoutParams.height = (int) animation.getAnimatedValue();
                mSelectorContainer.requestLayout();
            });
            mExpandedAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mArrow.setRotation(mExpanded ? 180 : 0);
                    mExpanded = !mExpanded;
                }
            });
        });
    }

    private void initQueryingAnimator() {
        mQueryAnimator = ObjectAnimator.ofFloat(mQuerying, "rotation", 0, 360);
        mQueryAnimator.setDuration(500).setRepeatCount(ObjectAnimator.INFINITE);
        mQueryAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void query() {
        int week = mWeekSelector.getSelectedValues()[0];
        int weekday = mWeekdaySelector.getSelectedValues()[0];
        int building = mBuildingSelector.getSelectedValues()[0];
        int[] sections = mSectionSelector.getSelectedValues();
        RequestManager.getInstance().queryEmptyRoomList(new SimpleSubscriber<>(this,
                new SubscriberListener<List<EmptyRoom>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mQuerying.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mQueryAnimator.start();
                    }

                    @Override
                    public void onNext(List<EmptyRoom> emptyRooms) {
                        super.onNext(emptyRooms);
                        refreshResult(emptyRooms);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mQuerying.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mQueryAnimator.cancel();
                    }
                }), week, weekday, building, sections);
    }

    private void refreshResult(List<EmptyRoom> emptyRooms) {
        if (mResultAdapter == null) {
            mResultAdapter = new EmptyRoomResultAdapter(emptyRooms, this);
            mRecyclerView.setAdapter(mResultAdapter);
        } else {
            mResultAdapter.updateData(emptyRooms);
        }
    }

    private boolean disallowExpandedAnimator() {
        if (mExpandedAnimator == null || mExpandedAnimator.isRunning()) {
            return true;
        }
        if (!mExpanded) {
            return false;
        } else if (mResultAdapter == null || mResultAdapter.getDataSource().isEmpty()) {
            return true;
        } else if (mQueryAnimator != null && mQueryAnimator.isRunning()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemClickListener(MultiSelector selector, RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onItemSelectedChange(MultiSelector selector, RecyclerView.ViewHolder viewHolder, int value, boolean checked, int position) {
        if (mBuildingSelector.selectedSize() > 0 && mSectionSelector.selectedSize() > 0) {
            query();
        } else if (mResultAdapter != null) {
            mResultAdapter.getDataSource().clear();
            mResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        float y = e.getY();
        if (y < mArrow.getBottom()) {
            return super.dispatchTouchEvent(e);
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                if (!disallowExpandedAnimator()) {
                    if (mExpanded && mY - y > 0) {
                        //向上滑动时若选择器展开则先收起
                        mExpandedAnimator.start();
                        return true;
                    } else if (!mExpanded && mY - y < 0) {
                        //向下滑动时若选择器收起则先展开
                        mExpandedAnimator.reverse();
                        return true;
                    }
                }
                mY = y;
                break;
        }
        return super.dispatchTouchEvent(e);
    }
}
