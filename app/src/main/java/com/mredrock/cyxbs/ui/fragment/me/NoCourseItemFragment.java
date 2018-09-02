package com.mredrock.cyxbs.ui.fragment.me;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.NoScheduleView;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.NoCourse;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.util.SchoolCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skylineTan on 2016/4/13 20:39.
 */
public class NoCourseItemFragment extends BaseFragment {

    public static final String ARG_WEEK = "arg_week";
    public static final String EXTRA_STU_NUM_LIST = "extra_stu_num_list";
    public static final String EXTRA_NAME_LIST = "extra_name_list";

    @BindView(R.id.no_course_week)
    LinearLayout noCourseWeek;
    @BindView(R.id.no_course_time)
    LinearLayout noCourseTime;
    @BindView(R.id.no_course_schedule_content)
    NoScheduleView noCourseScheduleContent;
    @BindView(R.id.no_course_swipe_refresh_layout)
    SwipeRefreshLayout noCourseSwipeRefreshLayout;

    private Map<String, List<Course>> mCourseMap;
    private ArrayList<String> mStuNumList;
    private ArrayList<String> mNameList;
    private List<NoCourse> mNoCourseList;
    private boolean blocked = false;

    private int mWeek;
    private int count;
    private int[] mTodayWeekIds = {R.id.view_course_today_7,
            R.id.view_course_today_1, R.id.view_course_today_2,
            R.id.view_course_today_3, R.id.view_course_today_4,
            R.id.view_course_today_5, R.id.view_course_today_6};

    public NoCourseItemFragment() {

    }

    public static NoCourseItemFragment newInstance(int week) {
        NoCourseItemFragment noCourseItemFragment = new NoCourseItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WEEK, week);
        noCourseItemFragment.setArguments(args);
        return noCourseItemFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
        mStuNumList = getActivity().getIntent()
                .getStringArrayListExtra(EXTRA_STU_NUM_LIST);
        mNameList = getActivity().getIntent()
                .getStringArrayListExtra(EXTRA_NAME_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_no_course_item,
                        container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    private void parseArguments() {
        Bundle args = getArguments();
        mWeek = args.getInt(ARG_WEEK);
    }


    private void initView() {
        String[] data = getResources().getStringArray(R.array.no_schedule_week);
        for (int i = 0; i < 7; i++) {
            TextView tv = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            tv.setLayoutParams(params);
            tv.setText(data[i]);
            tv.setTextColor(Color.parseColor("#7097FA"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv.setGravity(Gravity.CENTER);
            noCourseWeek.addView(tv);
        }
        for (int i = 0; i < 12; i++) {
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            tv.setText(i + 1 + "");
            tv.setTextColor(Color.parseColor("#7097FA"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            tv.setGravity(Gravity.CENTER);
            noCourseTime.addView(tv);
        }
        if (mWeek == new SchoolCalendar().getWeekOfTerm())
            showTodayWeek();

        noCourseSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getContext(), R.color.colorPrimary));
        noCourseSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mStuNumList.size() != 0) {
                clearnUp();
                loadWeekNoCourse();
            }
        });

        mCourseMap = new LinkedHashMap<>();
        mNoCourseList = new ArrayList<>();
        if (mStuNumList.size() != 0 && mCourseMap.size() == 0) {
            clearnUp();
            showProgress();
        }
    }

    private void clearnUp() {
        mCourseMap.clear();
        count = 0;
    }


    private void loadWeekNoCourse() {
        if (blocked) return;
        RequestManager.getInstance().getPublicCourse(new
                SimpleObserver<>(getActivity(), new SubscriberListener<List<Course>>() {

            @Override
            public void onStart() {
                blocked = true;
                super.onStart();
                count = 0;
            }


            @Override
            public void onNext(List<Course> courses) {
                super.onNext(courses);
                mCourseMap.put(String.valueOf(count), courses);
                count++;
            }

            @Override
            public boolean onError(Throwable e) {
                blocked = false;
                return super.onError(e);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                dismissProgress();
                getNoCourseTable();
                blocked = false;
            }
        }), mStuNumList, String.valueOf(mWeek));
    }


    private void showTodayWeek() {
        if (getView() != null) {
            getView().findViewById(mTodayWeekIds[
                    Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1])
                    .setVisibility(View.VISIBLE);
        }
    }


    public void getNoCourseTable() {
        for (int i = 0; i < 7; i++) {
            //3或者4节连上标记
            List<Boolean> booleanList = new ArrayList<>();
            for (int k = 0; k < mCourseMap.size(); k++) {
                booleanList.add(false);
            }

            for (int j = 0; j < 6; j++) {
                List<String> nameList = new ArrayList<>();
                for (Map.Entry<String, List<Course>> entry : mCourseMap.entrySet()) {
                    //得到课表
                    List<Course> courseList = entry.getValue();
                    boolean isNoCourse = true; //是否没有课
                    boolean isAllSemester = true; //该课程是否是整学期
                    String rawWeek = "";
                    for (Course course : courseList) {
                        if ((course.hash_day == i && course.hash_lesson == j)) {
                            //当前得到的这节课匹配有课
                            if (course.period == 3 || course.period == 4) {
                                booleanList.set(Integer.parseInt(entry.getKey()), true);
                            }
                            if (course.week.size() < 15) {
                                isAllSemester = false;
                                rawWeek = convert(course.rawWeek);
                            }
                            isNoCourse = false;
                        }
                    }
                    if ((booleanList.size() == 0 ||
                            !booleanList.get(Integer.parseInt(entry.getKey()))) && isNoCourse) {
                        nameList.add(mNameList.get(Integer.parseInt(entry.getKey())));
                    } else if (mWeek == 0 && !isAllSemester) {
                        String name = mNameList.get(
                                Integer.parseInt(entry.getKey())) + "\n" +
                                rawWeek;
                        nameList.add(name);
                    }
                }

                if (nameList.size() != 0) {
                    NoCourse noCourse = new NoCourse();
                    noCourse.names = nameList;
                    noCourse.hash_day = i;
                    noCourse.hash_lesson = j;
                    mNoCourseList.add(noCourse);
                }
            }
        }
        if (noCourseScheduleContent != null) {
            noCourseScheduleContent.addContentView(mNoCourseList, mWeek);
        }
    }

    private String convert(String rawWeek) {
        switch (rawWeek) {
            case "单周":
                return "(双周)";
            case "双周":
                return "(单周)";
            case "7-14周":
                return "(1-6周)";
            case "1-8周":
                return "(9-16周)";
            case "9-16周":
                return "(1-8周)";
            case "1-10周":
                return "(11-16周)";
            default:
                return "(除" + rawWeek + ")";
        }
    }

    private void showProgress() {
        noCourseSwipeRefreshLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                noCourseSwipeRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                noCourseSwipeRefreshLayout.setRefreshing(true);
                                loadWeekNoCourse();
                            }
                        });
    }

    private void dismissProgress() {
        if (noCourseSwipeRefreshLayout != null &&
                noCourseSwipeRefreshLayout.isRefreshing()) {
            noCourseSwipeRefreshLayout.setRefreshing(
                    false);
        }
    }
}