package com.mredrock.cyxbs.ui.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Exam;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.adapter.me.ExamScheduleAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.NetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;


/**
 * Created by skylineTan on 2016/4/22 22:09.
 */
public class ExamScheduleFragment extends BaseFragment {

    public static final String ARG_EXAM = "arg_exam";
    public static final String TYPE_EXAM = "exam";
    public static final String TYPE_REEXAM = "reexam";

    @BindView(R.id.no_data)
    ViewGroup mNoDataLayout;
    @BindView(R.id.text)
    TextView mNoDataText;
    @BindView(R.id.exam_recyclerView)
    RecyclerView examRecyclerView;
    @BindView(R.id.exam_swipe_refresh_layout)
    SwipeRefreshLayout examSwipeRefreshLayout;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
    }


    @Nullable
    @Override
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
        examSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mUser != null) {
                mNoDataLayout.setVisibility(View.GONE);
                loadExamList(true);
            }
        });
        examSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor
                (getContext(), R.color.colorAccent), ContextCompat.getColor(getContext(), R.color.colorPrimary));


        mUser = BaseAPP.getUser(getActivity());
        mUser.stu = mUser.stuNum;
        if (mUser != null) {
            if (NetUtils.isNetWorkAvailable(getActivity())) {
                showProgress();
            } else {
                //  examTvNothing.setVisibility(View.VISIBLE);
            }
        } else {
            if (mIsVisibleToUser) {
                Toast.makeText(getActivity(), "请登录后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        //if (isVisibleToUser && mExamList != null && mExamList.isEmpty()) {
        //    loadExamFromDB();
        //}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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
        mExamScheduleAdapter = new ExamScheduleAdapter(getActivity(), mExamList);
        examRecyclerView.setAdapter(mExamScheduleAdapter);
    }

    public void loadExamList(boolean update) {
        if (mUser != null) {
            Observer<List<Exam>> observer = new SimpleObserver<>(
                    getActivity(), new SubscriberListener<List<Exam>>() {
                @Override
                public boolean onError(Throwable e) {
                    super.onError(e);
                    try {
                        examSwipeRefreshLayout.setRefreshing(false);
                        mNoDataLayout.setVisibility(View.VISIBLE);
                        mNoDataText.setText(isReExam ? "暂无补考记录~" : "暂无考试记录~");
                    } catch (NullPointerException ex) {
                        LogUtils.LOGW(getClass().getName(), "Callback after activity destroy", ex);
                    }
                    return false;
                }


                @Override
                public void onNext(List<Exam> examList) {
                    super.onNext(examList);
                    try {

                        examSwipeRefreshLayout.setRefreshing(false);
                        if (examList == null || examList.size() == 0) {
                            mNoDataLayout.setVisibility(View.VISIBLE);
                            mNoDataText.setText(isReExam ? "暂无补考记录~" : "暂无考试记录~");
                        } else {
                            mNoDataLayout.setVisibility(View.GONE);
                            refresh(examList);
                        }
                    } catch (NullPointerException e) {
                        LogUtils.LOGW(getClass().getName(), "Callback after activity destroy", e);
                    }
                }

            });
            if (isReExam) {
                RequestManager.getInstance().getReExamList(observer, mUser.stu, update);
            } else {
                RequestManager.getInstance().getExamList(observer, mUser.stu, update);
            }
        } else {
            if (mIsVisibleToUser) {
                Toast.makeText(getActivity(), "请登录后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void refresh(List<Exam> examList) {
        mExamList.clear();
        mExamList.addAll(examList);
        Collections.sort(mExamList);
        mExamScheduleAdapter.notifyDataSetChanged();
        mNoDataLayout.setVisibility(View.GONE);
    }

    private void showProgress() {
        examSwipeRefreshLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        examSwipeRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        examSwipeRefreshLayout.setRefreshing(true);
                        loadExamList(false);
                    }
                });
    }

    private void dismissProgress() {
        examSwipeRefreshLayout.setRefreshing(false);
    }
}
