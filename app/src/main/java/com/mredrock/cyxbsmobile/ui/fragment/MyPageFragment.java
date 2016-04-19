package com.mredrock.cyxbsmobile.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.activity.mypage.EditInfoActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.EmptyRoomActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.NoCourseActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.RelateMeActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.SettingActivity;

/**
 * 我的页面
 */
public class MyPageFragment extends BaseFragment
        implements View.OnClickListener {

    @Bind(R.id.my_page_edit_layout) LinearLayout myPageEditLayout;
    @Bind(R.id.my_page_relate_layout) RelativeLayout myPageRelateLayout;
    @Bind(R.id.my_page_trend_layout) RelativeLayout myPageTrendLayout;
    @Bind(R.id.my_page_no_course_layout) RelativeLayout myPageNoCourseLayout;
    @Bind(R.id.my_page_empty_layout) RelativeLayout myPageEmptyLayout;
    @Bind(R.id.my_page_grade_layout) RelativeLayout myPageGradeLayout;
    @Bind(R.id.my_page_calendar_layout) RelativeLayout myPageCalendarLayout;
    @Bind(R.id.my_page_night_layout) RelativeLayout myPageNightLayout;
    @Bind(R.id.my_page_setting_layout) RelativeLayout myPageSettingLayout;


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container,
                false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myPageEditLayout.setOnClickListener(this);
        myPageRelateLayout.setOnClickListener(this);
        myPageTrendLayout.setOnClickListener(this);
        myPageNoCourseLayout.setOnClickListener(this);
        myPageEmptyLayout.setOnClickListener(this);
        myPageGradeLayout.setOnClickListener(this);
        myPageCalendarLayout.setOnClickListener(this);
        myPageNightLayout.setOnClickListener(this);
        myPageSettingLayout.setOnClickListener(this);
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_page_edit_layout:
                Intent intent = new Intent(getActivity(),
                        EditInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.my_page_relate_layout:
                startActivity(new Intent(getActivity(), RelateMeActivity.class));
                break;
            case R.id.my_page_trend_layout:
                break;
            case R.id.my_page_no_course_layout:
                startActivity(new Intent(getActivity(), NoCourseActivity.class));
                break;
            case R.id.my_page_empty_layout:
                startActivity(new Intent(getActivity(), EmptyRoomActivity.class));
                break;
            case R.id.my_page_grade_layout:
                break;
            case R.id.my_page_calendar_layout:
                break;
            case R.id.my_page_night_layout:
                break;
            case R.id.my_page_setting_layout:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
}
