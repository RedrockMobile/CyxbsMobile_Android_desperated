package com.mredrock.cyxbsmobile.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.mypage.EditInfoActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.EmptyRoomActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.ExamAndGradeActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.NoCourseActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.RelateMeActivity;
import com.mredrock.cyxbsmobile.ui.activity.mypage.SettingActivity;
import com.mredrock.cyxbsmobile.util.ImageLoader;

/**
 * 我的页面
 */
public class MyPageFragment extends BaseFragment
        implements View.OnClickListener {

    public static final int REQUEST_EDIT_INFO = 10;

    @Bind(R.id.my_page_edit_layout) LinearLayout myPageEditLayout;
    @Bind(R.id.my_page_relate_layout) RelativeLayout myPageRelateLayout;
    @Bind(R.id.my_page_trend_layout) RelativeLayout myPageTrendLayout;
    @Bind(R.id.my_page_no_course_layout) RelativeLayout myPageNoCourseLayout;
    @Bind(R.id.my_page_empty_layout) RelativeLayout myPageEmptyLayout;
    @Bind(R.id.my_page_grade_layout) RelativeLayout myPageGradeLayout;
    @Bind(R.id.my_page_calendar_layout) RelativeLayout myPageCalendarLayout;
    @Bind(R.id.my_page_night_layout) RelativeLayout myPageNightLayout;
    @Bind(R.id.my_page_setting_layout) RelativeLayout myPageSettingLayout;
    @Bind(R.id.my_page_avatar) CircleImageView myPageAvatar;
    @Bind(R.id.my_page_nick_name) TextView myPageNickName;
    @Bind(R.id.my_page_gender) ImageView myPageGender;
    @Bind(R.id.my_page_introduce) TextView myPageIntroduce;

    private User mUser;


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

        mUser = new User();
        mUser.stuNum = "2014213983";
        mUser.idNum = "26722X";
        getPersonInfoData();
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_page_edit_layout:
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra(EditInfoActivity.EXTRA_USER,mUser);
                startActivityForResult(intent,REQUEST_EDIT_INFO);
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
                startActivity(new Intent(getActivity(), ExamAndGradeActivity.class));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            mUser = data.getParcelableExtra(EditInfoActivity.EXTRA_USER);
            refreshEditLayout();
        }
    }


    private void getPersonInfoData() {
        RequestManager.getInstance()
                      .getPersonInfo(new SimpleSubscriber<>(getActivity(),
                              new SubscriberListener<User>() {
                                  @Override public void onNext(User user) {
                                      super.onNext(user);
                                      mUser = user;
                                      refreshEditLayout();
                                  }


                                  @Override public void onCompleted() {
                                      super.onCompleted();
                                  }
                              }), mUser.stuNum,mUser.idNum);
    }

    private void refreshEditLayout(){
        ImageLoader.getInstance().loadImageWithTargetView(
                mUser.photo_thumbnail_src, new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        myPageAvatar.setImageBitmap(resource);
                    }
                });
        myPageNickName.setText(mUser.nickname);
        myPageIntroduce.setText(mUser.introduction);
    }
}
