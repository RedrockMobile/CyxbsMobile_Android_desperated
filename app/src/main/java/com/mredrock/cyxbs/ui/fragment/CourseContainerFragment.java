package com.mredrock.cyxbs.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.database.DBEditHelper;
import com.mredrock.cyxbs.event.CourseLoadFinishEvent;
import com.mredrock.cyxbs.event.UpdateCourseEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.MainActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.util.SPUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CourseContainerFragment extends BaseFragment {

    public static final String TAG = "CourseContainerFragment";

    @Bind(R.id.tab_course_tabs)
    TabLayout mTabs;
    @Bind(R.id.tab_course_viewpager)
    ViewPager mPager;

    @OnClick(R.id.course_fab)
    void clickToExchange() {
        changeCurrentItem();
    }

    private TextView mToolbarTitle;

    private TabPagerAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String>   mTitles       = new ArrayList<>();
    private int mNowWeek;

    private ViewPager.OnPageChangeListener mPageListener;
    private ViewPager.OnPageChangeListener mTabListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mTitles = new ArrayList<>();
        mTitles.addAll(Arrays.asList(getResources().getStringArray(R.array.titles_weeks)));

        mNowWeek = new SchoolCalendar().getWeekOfTerm();

        if (mNowWeek <= 18 && mNowWeek >= 1) {
            mTitles.set(mNowWeek, getActivity().getResources().getString(R.string.now_week));
        }

        if (mFragmentList.isEmpty()) {
            for (int i = 0; i < mTitles.size(); i++) {
                CourseFragment temp = new CourseFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(CourseFragment.BUNDLE_KEY, i);
                temp.setArguments(bundle);
                mFragmentList.add(temp);
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_container, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitles);

        mToolbarTitle = ((MainActivity) getActivity()).getToolbarTitle();
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(mTabListener = new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mPager.addOnPageChangeListener(mPageListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mToolbarTitle != null) mToolbarTitle.setText(mTitles.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabs.setupWithViewPager(mPager);
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabs.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mNowWeek <= 18 && mNowWeek >= 1) setCurrentItem(mNowWeek);

        if (mToolbarTitle != null) {
            mToolbarTitle.setOnClickListener(v -> {
                if (isVisible()) {
                    if (mTabs.getVisibility() == View.VISIBLE) {
                        mTabs.setVisibility(View.GONE);
                    } else {
                        mTabs.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        loadNowWeek();
    }

    @Override
    public void onDestroyView() {
        mPager.removeOnPageChangeListener(mTabListener);
        mPager.removeOnPageChangeListener(mPageListener);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setCurrentItem(int position) {
        mPager.setCurrentItem(position, true);
    }

    private void changeCurrentItem() {
        int position = mPager.getCurrentItem();
        if (position != mNowWeek) {
            setCurrentItem(mNowWeek);
        } else if (position != 0) {
            setCurrentItem(0);
        }
    }

    private void loadNowWeek() {
        RequestManager.INSTANCE.getNowWeek(new SimpleSubscriber<>(APP.getContext(), new SubscriberListener<Integer>() {
            @Override
            public void onNext(Integer i) {
                int nowWeek = i;
                updateFirstDay(nowWeek);
                if (mNowWeek <= 18 && mNowWeek >= 1) {
                    setCurrentItem(mNowWeek);
                }
            }
        }), "2013214151", "");
    }

    private void updateFirstDay(int nowWeek) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -((nowWeek - 1) * 7 + (now.get(Calendar.DAY_OF_WEEK) + 5) % 7));
        SPUtils.set(APP.getContext(), "first_day", now.getTimeInMillis());
        mNowWeek = new SchoolCalendar().getWeekOfTerm();
    }
}