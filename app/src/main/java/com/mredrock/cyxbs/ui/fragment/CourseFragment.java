package com.mredrock.cyxbs.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ScheduleView;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.database.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CourseFragment extends BaseFragment {

    public static final String BUNDLE_KEY = "WEEK_NUM";

    private int[] mTodayWeekIds = {
            R.id.view_course_today_7,
            R.id.view_course_today_1,
            R.id.view_course_today_2,
            R.id.view_course_today_3,
            R.id.view_course_today_4,
            R.id.view_course_today_5,
            R.id.view_course_today_6
    };

    private int mWeek = 0;
    private User mUser;

    @Bind(R.id.course_swipe_refresh_layout)
    SwipeRefreshLayout mCourseSwipeRefreshLayout;
    @Bind(R.id.course_weeks)
    LinearLayout mCourseWeeks;
    @Bind(R.id.course_weekday)
    LinearLayout mCourseWeekday;
    @Bind(R.id.course_time)
    LinearLayout mCourseTime;
    @Bind(R.id.course_schedule_content)
    ScheduleView mCourseScheduleContent;
    @Bind(R.id.course_schedule_holder)
    LinearLayout mCourseScheduleHolder;
    @Bind(R.id.course_month)
    TextView mCourseMonth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeek = getArguments().getInt(BUNDLE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        ButterKnife.bind(this, view);
        initWeekView();
        return view;
    }

    public void initWeekView() {

        String[] date = getResources().getStringArray(R.array.course_weekdays);
        String month = new SchoolCalendar(mWeek, 1).getMonth() + "月";

        int screeHeight = DensityUtils.getScreenHeight(getContext());
        if (DensityUtils.px2dp(getContext(), screeHeight) > 700) {
            mCourseTime.setLayoutParams(new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 40), screeHeight));
            mCourseScheduleContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screeHeight));
        }

        if (mWeek != 0) mCourseMonth.setText(month);
        int today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7;
        for (int i = 0; i < 7; i++) {
            TextView tv = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            tv.setLayoutParams(params);
            SchoolCalendar schoolCalendar = new SchoolCalendar(mWeek, i + 1);
            tv.setText(date[i]);
            tv.setGravity(Gravity.CENTER);
            if (i == today && mWeek == new SchoolCalendar().getWeekOfTerm()) {
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
            mCourseWeeks.addView(tv);
            if (mWeek != 0) {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(params);
                String day = schoolCalendar.getDay() + "";
                textView.setText(day);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(14);
                if (i == today && mWeek == new SchoolCalendar().getWeekOfTerm()) {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                } else {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.data_light_black));
                }
                mCourseWeekday.addView(textView);
            }
        }
        for (int i = 0; i < 12; i++) {
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            String courseNumber = i + 1 + "";
            tv.setText(courseNumber);
            tv.setGravity(Gravity.CENTER);
            mCourseTime.addView(tv);
        }

        mCourseSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mUser != null) {
                loadCourse(mWeek, true);
            }
        });
        mCourseSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mWeek == new SchoolCalendar().getWeekOfTerm()) {
            showTodayWeek();
        }

        loadCourse(mWeek, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void showTodayWeek() {
        if (getView() != null)
            getView().findViewById(mTodayWeekIds[Calendar.getInstance()
                    .get(Calendar.DAY_OF_WEEK) - 1])
                    .setVisibility(View.VISIBLE);
    }

    private void loadCourse(int week, boolean update) {

        if (APP.isLogin()) {
            mUser = APP.getUser(getActivity());
            if (mUser != null) {
//                DBManager dbManager = new DBManager(getActivity());
//                List<String> data = dbManager.query(mUser.stuNum);
//                List<Course> courses1 = new ArrayList<>();
//                Gson s = new Gson();
//
//                for (String a : data){
//                    Affair affair =  s.fromJson(a,Affair.class);
//                    courses1.add(affair);
//                }
//                dbManager.close();

                RequestManager.getInstance()
                        .getCourseList(new SimpleSubscriber<>(getActivity(), false, false, new SubscriberListener<List<Course>>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                mCourseSwipeRefreshLayout.setRefreshing(true);
                            }

                            @Override
                            public void onNext(List<Course> courses) {
                                super.onNext(courses);
//                                courses.addAll(courses1);
                                if (mCourseScheduleContent != null) {
                                    mCourseScheduleContent.addContentView(courses);
                                }
                            }

                            @Override
                            public boolean onError(Throwable e) {
                                super.onError(e);
                                hideRefreshLoading();
                                return false;
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                hideRefreshLoading();
                            }
                        }), mUser.stuNum, mUser.idNum, week, update);
            }
        }
    }

    private void hideRefreshLoading() {
        if (mCourseSwipeRefreshLayout != null) {
            mCourseSwipeRefreshLayout.setRefreshing(false);
        }
    }
}