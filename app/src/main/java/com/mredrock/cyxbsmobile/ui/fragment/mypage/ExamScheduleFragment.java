package com.mredrock.cyxbsmobile.ui.fragment.mypage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.database.DBExamGradeHelper;
import com.mredrock.cyxbsmobile.model.Exam;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.mypage.ExamScheduleAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;
import com.mredrock.cyxbsmobile.util.NetUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * Created by skylineTan on 2016/4/22 22:09.
 */
public class ExamScheduleFragment extends BaseFragment {

    public static final String ARG_EXAM = "arg_exam";
    public static final String TYPE_EXAM = "exam";
    public static final String TYPE_REEXAM = "reexam";

    @Bind(R.id.exam_tv_nothing) TextView examTvNothing;
    @Bind(R.id.exam_recyclerView) RecyclerView examRecyclerView;
    @Bind(R.id.exam_swipe_refresh_layout) SwipeRefreshLayout examSwipeRefreshLayout;

    private List<Exam> mExamList;

    private ExamScheduleAdapter mExamScheduleAdapter;
    private User mUser;
    private String mType;
    private boolean isReExam;
    private boolean mIsVisibleToUser;


    public ExamScheduleFragment() {

    }


    public static ExamScheduleFragment newInstance(boolean isReExam) {
        ExamScheduleFragment examScheduleFragment = new ExamScheduleFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_EXAM, isReExam);
        examScheduleFragment.setArguments(args);
        return examScheduleFragment;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_schedule, container,
                false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        examSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if (mUser != null) {
                    loadExamFromNetWork();
                }
            }
        });
        examSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor
                (getContext(),R.color.colorAccent), ContextCompat.getColor(getContext(),R.color.colorPrimary));

        loadExamFromDB();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        //if (isVisibleToUser && mExamList != null && mExamList.isEmpty()) {
        //    loadExamFromDB();
        //}
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void parseArguments() {
        Bundle args = getArguments();
        isReExam = args.getBoolean(ARG_EXAM);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        examRecyclerView.setLayoutManager(linearLayoutManager);
        mExamList = new ArrayList<>();
        mExamScheduleAdapter = new ExamScheduleAdapter(getActivity(),mExamList);
        examRecyclerView.setAdapter(mExamScheduleAdapter);
    }

    public void loadExamFromNetWork() {
        mUser = new User();
        mUser.stu = "2014213983";
        examSwipeRefreshLayout.setRefreshing(true);
        if (mUser != null) {
            Subscriber<String> subscriber = new SimpleSubscriber<>(
                    getActivity(), new SubscriberListener<String>() {
                @Override public void onError(Throwable e) {
                    super.onError(e);
                    examSwipeRefreshLayout.setRefreshing(false);
                    examTvNothing.setVisibility(View.VISIBLE);
                }


                @Override public void onNext(String s) {
                    super.onNext(s);
                    Exam.ExamWapper exam = new Gson().fromJson(s,Exam.ExamWapper.class);
                    examSwipeRefreshLayout.setRefreshing(false);
                    DBExamGradeHelper.addExam(getActivity(),"2014213983", mType,s);
                    if (exam.data == null || exam.data.size() == 0) {
                        examTvNothing.setVisibility(View.VISIBLE);
                    }else {
                        refresh(exam.data);
                    }
                }

            });
            if (isReExam) {
                RequestManager.INSTANCE.getReExamJson(subscriber,mUser.stu);
            } else {
                RequestManager.INSTANCE.getExamJson(subscriber,mUser.stu);
            }
        } else {
            if (mIsVisibleToUser) {
                Toast.makeText(getActivity(),"请登录后再试",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadExamFromDB() {
        mUser = new User();
        mUser.stu = "2014213983";

        if (mUser != null) {
            if (isReExam) {
                mType = TYPE_REEXAM;
            } else {
                mType = TYPE_EXAM;
            }
            List<String> examJsonList = DBExamGradeHelper.selectExam
                    (getActivity(),mUser.stu,mType);
            if(examJsonList != null && examJsonList.size() > 0){
                Exam.ExamWapper exam = new Gson().fromJson(examJsonList.get
                        (0),Exam.ExamWapper.class);
                refresh(exam.data);
            }else if(NetUtils.isNetWorkAvilable(getActivity())){
                examSwipeRefreshLayout.setRefreshing(true);
                loadExamFromNetWork();
            }else {
                examTvNothing.setVisibility(View.VISIBLE);
            }
        }else {
            if (mIsVisibleToUser) {
                Toast.makeText(getActivity(),"请登录后再试",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void refresh(List<Exam> examList) {
        mExamList.clear();
        mExamList.addAll(examList);
        mExamScheduleAdapter.notifyDataSetChanged();
        examTvNothing.setVisibility(View.GONE);
    }
}
