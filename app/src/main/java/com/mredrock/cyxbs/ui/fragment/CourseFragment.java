package com.mredrock.cyxbs.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.Position;
import com.mredrock.cyxbs.component.widget.ScheduleView;
import com.mredrock.cyxbs.event.AffairAddEvent;
import com.mredrock.cyxbs.event.AffairDeleteEvent;
import com.mredrock.cyxbs.event.AffairModifyEvent;
import com.mredrock.cyxbs.event.AffairShowModeEvent;
import com.mredrock.cyxbs.model.Affair;
import com.mredrock.cyxbs.model.Course;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.affair.EditAffairActivity;
import com.mredrock.cyxbs.ui.activity.me.SettingActivity;
import com.mredrock.cyxbs.ui.widget.CourseListAppWidgetUpdateService;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.database.DBManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CourseFragment extends BaseFragment {
    private static final String TAG = "CourseFragment";
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
        String month = new SchoolCalendar(mWeek, 1).getMonth()+"\n"+ "月";

        int screeHeight = DensityUtils.getScreenHeight(getContext());
        if (DensityUtils.px2dp(getContext(), screeHeight) > 700) {
            mCourseTime.setLayoutParams(new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 40), screeHeight));
            mCourseScheduleContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screeHeight));
        }
        Intent intent = new Intent(getActivity(), EditAffairActivity.class);
        mCourseScheduleContent.setOnImageViewClickListener((x,y)->{
            int day = x;
            int lesson = y / 2;
            Position position = new Position(day , lesson);
            intent.putExtra(EditAffairActivity.BUNDLE_KEY,position);
            intent.putExtra(EditAffairActivity.WEEK_NUMBER,mWeek);
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
            if (i == today && mWeek == new SchoolCalendar().getWeekOfTerm()) {
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
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
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
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
        sharedPreferences = getActivity().getSharedPreferences(SettingActivity.SHOW_MODE,Context.MODE_PRIVATE);
        mCourseScheduleContent.setShowMode(sharedPreferences.getBoolean(SettingActivity.SHOW_MODE,true));

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
                            public void onCompleted() {
                                super.onCompleted();
                                loadAffair(week);
                                hideRefreshLoading();
                            }
                        }), mUser.stuNum, mUser.idNum, week, update);

                RequestManager.getInstance().getAffair(new SimpleSubscriber<List<Affair>>(getActivity(), false, false, new SubscriberListener<List<Affair>>() {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        DBManager.INSTANCE.query(mUser.stuNum,mWeek)
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<List<Course>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<Course> courses) {
                                        affairList.clear();
                                        for (Course c : courses){
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
                        for (Affair a : affairs){
                            if (a.week.contains(mWeek))
                                affairList.add(a);
                        }
                        loadAffair(mWeek);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                }),mUser.stuNum,mUser.idNum);


            }
        }
    }


    private synchronized void loadAffair(int mWeek){

        List<Course> tempCourseList = new ArrayList<>();
        tempCourseList.addAll(courseList);
        tempCourseList.addAll(affairList);
        tempCourseList.addAll(localAffairList);
        if (mCourseScheduleContent != null) {
            mCourseScheduleContent.clearList();
            mCourseScheduleContent.addContentView(tempCourseList);
            Observable<List<Course>> observable = Observable.create(new Observable.OnSubscribe<List<Course>>() {
                @Override
                public void call(Subscriber<? super List<Course>> subscriber) {
                    subscriber.onNext(affairList);
                    subscriber.onCompleted();
                }
            });
            observable.map(courses -> {
                CourseListAppWidgetUpdateService.start(getActivity(), false);
                return courses;
            }).subscribe();
        }

    }

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairDeleteEvent(AffairDeleteEvent event) {
        if (mWeek == 0||event.getCourse().week.contains(mWeek)){
            Affair affair = (Affair) event.getCourse();
            RequestManager.getInstance().deleteAffair(new SimpleSubscriber<Object>(getActivity(), true, true, new SubscriberListener<Object>() {
                @Override
                public void onCompleted() {
                    super.onCompleted();


                }

                @Override
                public boolean onError(Throwable e) {
                    return super.onError(e);
                }

                @Override
                public void onNext(Object object) {
                    super.onNext(object);
                    loadCourse(mWeek,false);
                    DBManager.INSTANCE.deleteAffair(affair.uid)
                            .observeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SimpleSubscriber(getActivity(), new SubscriberListener() {
                                @Override
                                public void onCompleted() {
                                    super.onCompleted();
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

                @Override
                public void onStart() {
                    super.onStart();
                }
            }),mUser.stuNum,mUser.idNum,affair.uid);

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairAddEvent(AffairAddEvent event){
        if (mWeek == 0 || event.getCourse().week.contains(mWeek)){
          //  LogUtils.LOGE("onAffairAddEvent","loadCourse(mWeek,false);");
            loadCourse(mWeek, false);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairModifyEvent(AffairModifyEvent event){
        loadCourse(mWeek, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffairShowModeEvent(AffairShowModeEvent event){
        mCourseScheduleContent.setShowMode(event.showMode);
        loadCourse(mWeek, false);
    }



    private void hideRefreshLoading() {
        if (mCourseSwipeRefreshLayout != null) {
            mCourseSwipeRefreshLayout.setRefreshing(false);
        }
    }
}