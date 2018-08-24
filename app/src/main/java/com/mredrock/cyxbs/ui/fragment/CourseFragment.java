package com.mredrock.cyxbs.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Position;
import com.mredrock.cyxbs.component.widget.ScheduleView;
import com.mredrock.cyxbs.event.AffairAddEvent;
import com.mredrock.cyxbs.event.AffairDeleteEvent;
import com.mredrock.cyxbs.event.AffairModifyEvent;
import com.mredrock.cyxbs.event.AffairShowModeEvent;
import com.mredrock.cyxbs.event.ForceFetchCourseEvent;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.affair.EditAffairActivity;
import com.mredrock.cyxbs.ui.activity.me.SettingActivity;
import com.mredrock.cyxbs.ui.widget.CourseListAppWidgetUpdateWorker;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.database.DBManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;


public class CourseFragment extends BaseFragment {
    private static final String TAG = "CourseFragment";
    public static final String BUNDLE_KEY = "WEEK_NUM";
    public static final String BUNDLE_KEY_REAL_WEEK = "REAL_WEEK_NUM";


    private int[] mTodayWeekIds = {
            R.id.view_course_today_7,
            R.id.view_course_today_1,
            R.id.view_course_today_2,
            R.id.view_course_today_3,
            R.id.view_course_today_4,
            R.id.view_course_today_5,
            R.id.view_course_today_6
    };

    //当前显示的周数
    private int mWeek = 0;
    private User mUser;
    //当前实际的周数

    @BindView(R.id.course_swipe_refresh_layout)
    SwipeRefreshLayout mCourseSwipeRefreshLayout;
    @BindView(R.id.course_weeks)
    LinearLayout mCourseWeeks;
    @BindView(R.id.course_weekday)
    LinearLayout mCourseWeekday;
    @BindView(R.id.course_time)
    LinearLayout mCourseTime;
    @BindView(R.id.course_schedule_content)
    ScheduleView mCourseScheduleContent;
    @BindView(R.id.course_schedule_holder)
    LinearLayout mCourseScheduleHolder;
    @BindView(R.id.course_month)
    TextView mCourseMonth;
    @BindView(R.id.no_course_holder)
    View mNoCourseHolder;
    @BindView(R.id.course_holder)
    View mCourseHolder;

    // private boolean showAffairContent = true;
    private SharedPreferences sharedPreferences;

    private List<Course> courseList = new ArrayList<>();
    private List<Course> affairList = new ArrayList<>();
    private List<Course> localAffairList = new ArrayList<>();

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
        String month = new SchoolCalendar(mWeek, 1).getString("MM月");

        int screeHeight = DensityUtils.getScreenHeight(getContext());
        if (DensityUtils.px2dp(getContext(), screeHeight) > 700) {
            mCourseHolder.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, screeHeight));
            mCourseTime.setLayoutParams(new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 40), screeHeight));
            mCourseScheduleContent.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, screeHeight));
        }
        Intent intent = new Intent(getActivity(), EditAffairActivity.class);
        mCourseScheduleContent.setOnImageViewClickListener((x, y) -> {
            int day = x;
            int lesson = y / 2;
            Position position = new Position(day, lesson);
            intent.putExtra(EditAffairActivity.BUNDLE_KEY, position);
            intent.putExtra(EditAffairActivity.WEEK_NUMBER, mWeek);
            startActivity(intent);
        });
        if (mWeek != 0) mCourseMonth.setText(month);
        int today = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7;
        for (int i = 0; i < 7; i++) {
            TextView tv = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            tv.setLayoutParams(params);
            SchoolCalendar schoolCalendar = new SchoolCalendar(mWeek, i + 1);
            tv.setText(date[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv.setTextColor(Color.parseColor("#7097FA"));
            /*if (i == today && mWeek == new SchoolCalendar().getWeekOfTerm()) {
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            }*/
            mCourseWeeks.addView(tv);
            if (mWeek != 0) {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(params);
                String day = schoolCalendar.getDay() + "日";
                textView.setText(day);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                textView.setTextColor(Color.parseColor("#8395A4"));
                /*
                if (i == today && mWeek == new SchoolCalendar().getWeekOfTerm()) {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                } else {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.data_light_black));
                }*/
                mCourseWeekday.addView(textView);
            }
        }
        for (int i = 0; i < 12; i++) {
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            String courseNumber = i + 1 + "";
            tv.setText(courseNumber);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            tv.setTextColor(Color.parseColor("#7097FA"));
            mCourseTime.addView(tv);
        }

        mCourseSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mUser != null) {
                loadCourse(mWeek, true, false);
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
        sharedPreferences = getActivity().getSharedPreferences(SettingActivity.SHOW_MODE, Context.MODE_PRIVATE);
        mCourseScheduleContent.setShowMode(sharedPreferences.getBoolean(SettingActivity.SHOW_MODE, true));

        loadCourse(mWeek, false, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void showTodayWeek() {
        if (getView() != null)
            getView().findViewById(mTodayWeekIds[Calendar.getInstance()
                    .get(Calendar.DAY_OF_WEEK) - 1])
                    .setVisibility(View.VISIBLE);
    }

    private void loadCourse(int week, boolean update, boolean forceFetch) {
        showNoCourseFrame(true);

        if (BaseAPP.isLogin()) {
            mUser = BaseAPP.getUser(getActivity());
            if (mUser != null) {
                //强制从教务在线抓取课表时，当前显示的周数与实际周数相同就展示ProgressDialog
                RequestManager.getInstance()
                        .getCourseList(new SimpleObserver<>(getActivity(), false, false, new SubscriberListener<List<Course>>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                mCourseSwipeRefreshLayout.setRefreshing(true);
                            }

                            @Override
                            public void onNext(List<Course> courses) {
                                super.onNext(courses);
                                courseList.clear();
                                courseList.addAll(courses);
                                loadAffair(mWeek);
                            }

                            @Override
                            public boolean onError(Throwable e) {
                                super.onError(e);
                                hideRefreshLoading();
                                return false;
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                                loadAffair(week);
                                hideRefreshLoading();
                            }
                        }), mUser.stuNum, mUser.idNum, week, update, forceFetch);

                RequestManager.getInstance().getAffair(new SimpleObserver<>(getActivity(), false, false, new SubscriberListener<List<Affair>>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        DBManager.INSTANCE.query(mUser.stuNum, mWeek)
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<Course>>() {
                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(List<Course> courses) {
                                        affairList.clear();
                                        for (Course c : courses) {
                                            //Log.d(TAG, "onNext: " + c.course);
                                            if (c.week.contains(mWeek))
                                                affairList.add(c);
                                        }
                                        loadAffair(mWeek);
                                    }
                                });

                        return true;
                    }

                    @Override
                    public void onNext(List<Affair> affairs) {
                        super.onNext(affairs);
                        affairList.clear();
                        for (Affair a : affairs) {
                            if (a.week.contains(mWeek))
                                affairList.add(a);
                        }
                        loadAffair(mWeek);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                }), mUser.stuNum, mUser.idNum);


            }
        }
    }

    private void showNoCourseFrame(boolean show) {
        if (show) {
            mNoCourseHolder.setVisibility(View.VISIBLE);
            mNoCourseHolder.setTranslationY(DensityUtils.getScreenHeight(getContext()) * 0.13f);
            mCourseScheduleContent.setVisibility(View.GONE);
        } else {
            mNoCourseHolder.setVisibility(View.GONE);
            mCourseScheduleContent.setVisibility(View.VISIBLE);
        }
    }


    private synchronized void loadAffair(int mWeek) {
        List<Course> tempCourseList = new ArrayList<>();
        tempCourseList.addAll(courseList);
        tempCourseList.addAll(affairList);
        tempCourseList.addAll(localAffairList);
        showNoCourseFrame(tempCourseList.isEmpty());
        if (mCourseScheduleContent != null) {
            mCourseScheduleContent.clearList();
            mCourseScheduleContent.addContentView(tempCourseList);
            User user = BaseAPP.getUser(getContext());
            if (user == null) return;
            CourseListAppWidgetUpdateWorker.startSingleWork(user.stuNum, user.idNum, false, 0);
        }

    }

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairDeleteEvent(AffairDeleteEvent event) {
        if (event.getCourse().week.contains(mWeek)) {
            Affair affair = (Affair) event.getCourse();
            RequestManager.getInstance().deleteAffair(new SimpleObserver<>(getActivity(), true, true, new SubscriberListener<Unit>() {

                @Override
                public void onNext(Unit object) {
                    super.onNext(object);
                    loadCourse(mWeek, false, false);
                    DBManager.INSTANCE.deleteAffair(affair.uid)
                            .observeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SimpleObserver(getActivity(), new SubscriberListener() {
                                @Override
                                public void onComplete() {
                                    super.onComplete();
                                }

                                @Override
                                public boolean onError(Throwable e) {
                                    return super.onError(e);
                                }

                                @Override
                                public void onNext(Object o) {
                                    super.onNext(o);
                                }

                                @Override
                                public void onStart() {
                                    super.onStart();
                                }
                            }));
                }
            }), mUser.stuNum, mUser.idNum, affair.uid);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairAddEvent(AffairAddEvent event) {
        if (mWeek == 0 || event.getCourse().week.contains(mWeek)) {
            loadCourse(mWeek, false, false);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onForceFetchCourseEvent(ForceFetchCourseEvent event) {
        LogUtils.LOGI(TAG, "event.getCurrentWeek()=" + event.getCurrentWeek() + "mWeek: " + mWeek);
        if (event.getCurrentWeek() == mWeek) {
            //  LogUtils.LOGE("onAffairAddEvent","loadCourse(mWeek,false);");
            loadCourse(mWeek, true, true);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairModifyEvent(AffairModifyEvent event) {
        loadCourse(mWeek, false, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairShowModeEvent(AffairShowModeEvent event) {
        mCourseScheduleContent.setShowMode(event.showMode);
        loadCourse(mWeek, false, false);
    }

    private void hideRefreshLoading() {
        if (mCourseSwipeRefreshLayout != null) {
            mCourseSwipeRefreshLayout.setRefreshing(false);
        }
    }
}