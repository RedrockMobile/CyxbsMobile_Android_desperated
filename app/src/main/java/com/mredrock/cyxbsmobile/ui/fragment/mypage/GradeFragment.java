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

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.Grade;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.adapter.me.GradeAdapter;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;
import com.mredrock.cyxbsmobile.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/21 19:08.
 */
public class GradeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.grade_tv_nothing)
    TextView gradeTvNothing;
    @Bind(R.id.grade_recyclerView)
    RecyclerView mGradeRecyclerView;
    @Bind(R.id.grade_swipe_refresh_layout)
    SwipeRefreshLayout
            mGradeRefreshLayout;

    private List<Grade> mGradeList;

    private GradeAdapter mGradeAdapter;
    private User mUser;
    private boolean mIsVisibleToUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        mGradeRecyclerView.setLayoutManager(linearLayoutManager);
        mGradeList = new ArrayList<>();
        mGradeAdapter = new GradeAdapter(mGradeList, getActivity());
        mGradeRecyclerView.setAdapter(mGradeAdapter);

        mGradeRefreshLayout.setOnRefreshListener(this);
        mGradeRefreshLayout.setColorSchemeColors(ContextCompat.getColor
                (getContext(), R.color.colorAccent), ContextCompat.getColor
                (getContext(), R.color.colorPrimary));

        mUser = new User();
        mUser.stuNum = "2014213983";
        mUser.idNum = "26722X";
        if (mUser != null) {
            if (NetUtils.isNetWorkAvailable(getActivity())) {
                showProgress();
            } else {
                gradeTvNothing.setVisibility(View.VISIBLE);
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
        if (isVisibleToUser && mGradeList != null && mGradeList.isEmpty()) {
            loadGradeList(true);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        if (mUser != null) {
            loadGradeList(true);
        }
    }

    private void loadGradeList(boolean update) {
        RequestManager.getInstance().getGradeList(new SimpleSubscriber<List<Grade>>(
                getActivity(), new SubscriberListener<List<Grade>>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dismissProgress();
                gradeTvNothing.setVisibility(View.VISIBLE);

            }


            @Override
            public void onNext(List<Grade> gradeList) {
                super.onNext(gradeList);
                dismissProgress();
                refresh(gradeList);
                if (mGradeList.size() == 0) {
                    gradeTvNothing.setVisibility(View.VISIBLE);
                }
            }
        }), mUser.stuNum, mUser.idNum, update);
    }

    private void refresh(List<Grade> gradeList) {
        mGradeList.clear();
        addTitleToList();
        mGradeList.addAll(gradeList);
        mGradeAdapter.notifyDataSetChanged();
        gradeTvNothing.setVisibility(View.GONE);
    }

    private void addTitleToList() {
        Grade grade = new Grade();
        grade.course = getActivity().getResources().getString(R.string.grade_title_name);
        grade.property = getActivity().getResources().getString(R.string
                .grade_title_property);
        grade.grade = getActivity().getResources().getString(R.string.grade_title_score);
        mGradeList.add(grade);
    }

    private void showProgress() {
        mGradeRefreshLayout.post(() -> mGradeRefreshLayout.setRefreshing(true));
        loadGradeList(false);
        onRefresh();
    }

    private void dismissProgress() {
        if (mGradeRefreshLayout != null && mGradeRefreshLayout.isRefreshing()) {
            mGradeRefreshLayout.setRefreshing(false);
        }
    }
}
