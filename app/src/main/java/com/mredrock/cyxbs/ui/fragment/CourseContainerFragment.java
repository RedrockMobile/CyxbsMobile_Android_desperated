package com.mredrock.cyxbs.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.MainActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.util.SPUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CourseContainerFragment extends BaseFragment {

    public static final String TAG = "CourseContainerFragment";

    public static final String SP_COLUMN_LAUNCH = "CourseContainerFragment_first_time_launch";

    private boolean mIsFirstLaunch;

    @Bind(R.id.tab_course_tabs)
    TabLayout mTabs;
    @Bind(R.id.tab_course_viewpager)
    ViewPager mPager;

    @OnClick(R.id.course_fab)
    void clickToExchange() {
        changeCurrentItem();
    }

    private TextView mToolbarTitle;
    private String title;

    private TabPagerAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private int mNowWeek;

    private ViewPager.OnPageChangeListener mPageListener;
    private ViewPager.OnPageChangeListener mTabListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getInfoFromSP();
    }

    private void getInfoFromSP() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        mIsFirstLaunch = sp.getBoolean(SP_COLUMN_LAUNCH, true);
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

    private void saveInfoToSP() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putBoolean(SP_COLUMN_LAUNCH, false);
        editor.apply();
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
                title = mTitles.get(position);
                if (((MainActivity) getActivity()).getCurrentPosition() == 0) {
                    if (mToolbarTitle != null) mToolbarTitle.setText(title);
                }

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
                        mTabs.setScrollPosition(mPager.getCurrentItem(), 0, true);
                    }
                }
            });
        }
        loadNowWeek();
        //remindFn(view);
    }

    private void remindFn(View view) {
        if (APP.isLogin() && mIsFirstLaunch) {
            Snackbar.make(view, "点击标题栏可以打开隐藏关卡", Snackbar.LENGTH_LONG).setAction("试试看", v -> {
                view.postDelayed(() -> mToolbarTitle.performClick(), 300);
                saveInfoToSP();
            }).show();
        }
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
        if (mPager != null) {
            mPager.setCurrentItem(position, true);
        }
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
                Log.d(TAG, "onNext: now week: " + i);
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

    public String getTitle() {
        return title == null ? "课表" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}