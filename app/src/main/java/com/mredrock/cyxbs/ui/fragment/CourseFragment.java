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
import com.mredrock.cyxbs.database.DBEditHelper;
import com.mredrock.cyxbs.event.CourseLoadFinishEvent;
import com.mredrock.cyxbs.event.UpdateCourseEvent;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CourseFragment extends BaseFragment {

    public static final String BUNDLE_KEY    = "WEEK_NUM";
    private             int[]  mTodayWeekIds = {R.id.view_course_today_7, R.id.view_course_today_1, R.id.view_course_today_2,
            R.id.view_course_today_3, R.id.view_course_today_4, R.id.view_course_today_5, R.id.view_course_today_6};
    private             int    mWeek         = 0;
    private User mUser;
    public static final String TAG = "BaseFragment";

    @Bind(R.id.course_swipe_refresh_layout)
    SwipeRefreshLayout mCourseSwipeRefreshLayout;
    @Bind(R.id.course_weeks)
    LinearLayout       mCourseWeeks;
    @Bind(R.id.course_weekday)
    LinearLayout       mCourseWeekday;
    @Bind(R.id.course_time)
    LinearLayout       mCourseTime;
    @Bind(R.id.course_schedule_content)
    ScheduleView       mCourseScheduleContent;
    @Bind(R.id.course_schedule_holder)
    LinearLayout       mCourseScheduleHolder;
    @Bind(R.id.course_month)
    TextView           mCourseMonth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeek = getArguments().getInt(BUNDLE_KEY);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        if (APP.isLogin()) {
            mUser = APP.getUser(getActivity());
        }
        if (mUser != null) loadWeekCourseFromDB(mWeek);
        if (mWeek == new SchoolCalendar().getWeekOfTerm()) showTodayWeek();

        mCourseSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mUser != null) {
                mCourseSwipeRefreshLayout.setRefreshing(true);
                EventBus.getDefault().post(new UpdateCourseEvent());
            }
        });
        mCourseSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCourseLoadFinish(CourseLoadFinishEvent event) {
        mUser = APP.getUser(getActivity());
        loadWeekCourseFromDB(mWeek);
    }

    private void showTodayWeek() {
        if (getView() != null)
            getView().findViewById(mTodayWeekIds[Calendar.getInstance()
                                                         .get(Calendar.DAY_OF_WEEK) - 1])
                     .setVisibility(View.VISIBLE);
    }

    private void loadWeekCourseFromDB(int mWeek) {
        if (mUser != null) {
            List<String> courseJson = DBEditHelper.selectCourse(APP.getContext(), mUser.stuNum);
            if (courseJson.size() > 0) {
                Course.CourseWrapper courseWrapper = new Gson().fromJson(courseJson.get(0), Course.CourseWrapper.class);
                if (isAdded()) {
                    List<Course> allCourses = new ArrayList<>();
                    allCourses.addAll(courseWrapper.data);
                    if (mWeek != 0) {
                        for (int i = 0; i < allCourses.size(); i++) {
                            if (!allCourses.get(i).week.contains(mWeek)) {
                                allCourses.remove(i);
                                i--;
                            }
                        }
                    }
                    List<Course> courses = new ArrayList<>();
                    courses.addAll(allCourses);
                    if (mCourseScheduleContent != null) {
                        mCourseSwipeRefreshLayout.setRefreshing(false);
                        mCourseScheduleContent.addContentView(courses);
                    }
                }
            }
        }
    }
}