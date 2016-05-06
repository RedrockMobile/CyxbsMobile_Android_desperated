package com.mredrock.cyxbsmobile.ui.fragment;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.MainActivity;
import com.mredrock.cyxbsmobile.ui.activity.me.MyTrendActivity;
import com.mredrock.cyxbsmobile.ui.activity.me.AboutMeActivity;
import com.mredrock.cyxbsmobile.ui.activity.me.ExamAndGradeActivity;
import com.mredrock.cyxbsmobile.ui.activity.me.SchoolCalendarActivity;
import com.mredrock.cyxbsmobile.util.ImageLoader;

import com.mredrock.cyxbsmobile.ui.activity.me.EditInfoActivity;
import com.mredrock.cyxbsmobile.ui.activity.me.EmptyRoomActivity;
import com.mredrock.cyxbsmobile.ui.activity.me.NoCourseActivity;
import com.mredrock.cyxbsmobile.ui.activity.me.SettingActivity;
import com.mredrock.cyxbsmobile.util.SPUtils;

/**
 * 我的页面
 */
public class UserFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    public static final int REQUEST_EDIT_INFO = 10;

    @Bind(R.id.my_page_edit_layout)
    LinearLayout    myPageEditLayout;
    @Bind(R.id.my_page_relate_layout)
    RelativeLayout  myPageRelateLayout;
    @Bind(R.id.my_page_trend_layout)
    RelativeLayout  myPageTrendLayout;
    @Bind(R.id.my_page_no_course_layout)
    RelativeLayout  myPageNoCourseLayout;
    @Bind(R.id.my_page_empty_layout)
    RelativeLayout  myPageEmptyLayout;
    @Bind(R.id.my_page_grade_layout)
    RelativeLayout  myPageGradeLayout;
    @Bind(R.id.my_page_calendar_layout)
    RelativeLayout  myPageCalendarLayout;
    @Bind(R.id.my_page_night_layout)
    RelativeLayout  myPageNightLayout;
    @Bind(R.id.my_page_setting_layout)
    RelativeLayout  myPageSettingLayout;
    @Bind(R.id.my_page_avatar)
    CircleImageView myPageAvatar;
    @Bind(R.id.my_page_nick_name)
    TextView        myPageNickName;
    @Bind(R.id.my_page_gender)
    TextView        myPageGender;
    @Bind(R.id.my_page_introduce)
    TextView        myPageIntroduce;
    @Bind(R.id.my_page_switch_compat)
    SwitchCompat    myPageSwitchCompat;

    private User mUser;


    @Nullable
    @Override
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
        boolean isNight = (boolean) SPUtils.get(getContext(), Const.SP_KEY_IS_NIGHT, false);
        myPageSwitchCompat.setChecked(isNight);
        myPageSwitchCompat.setOnCheckedChangeListener(this);

        mUser = new User();
        mUser.stuNum = "2014213983";
        mUser.idNum = "26722X";
        getPersonInfoData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.my_page_edit_layout)
    void clickToEdit() {
        Intent intent = new Intent(getActivity(),
                EditInfoActivity.class);
        intent.putExtra(Const.Extras.EDIT_USER, mUser);
        startActivityForResult(intent, REQUEST_EDIT_INFO);
    }

    @OnClick(R.id.my_page_relate_layout)
    void clickToRelate() {
        startActivity(new Intent(getActivity(),
                AboutMeActivity.class).putExtra(
                Const.Extras.EDIT_USER, mUser));
    }

    @OnClick(R.id.my_page_trend_layout)
    void clickToLatest() {
        startActivity(new Intent(getActivity(),
                MyTrendActivity.class).putExtra(
                Const.Extras.EDIT_USER, mUser));
    }

    @OnClick(R.id.my_page_no_course_layout)
    void clickToNoCourse() {
        startActivity(new Intent(getActivity(), NoCourseActivity.class));
    }

    @OnClick(R.id.my_page_empty_layout)
    void clickToEmpty() {
        startActivity(new Intent(getActivity(), EmptyRoomActivity.class));
    }

    @OnClick(R.id.my_page_grade_layout)
    void clickToGrade() {
        startActivity(new Intent(getActivity(), ExamAndGradeActivity.class));
    }

    @OnClick(R.id.my_page_calendar_layout)
    void clickToCalendar() {
        startActivity(new Intent(getActivity(), SchoolCalendarActivity.class));
    }

    @OnClick(R.id.my_page_night_layout)
    void clickToNight() {
        if (myPageSwitchCompat.isChecked()) {
            SPUtils.set(getContext(), Const.SP_KEY_IS_NIGHT, false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            SPUtils.set(getContext(), Const.SP_KEY_IS_NIGHT, true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @OnClick(R.id.my_page_setting_layout)
    void clickToSetting() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mUser = data.getParcelableExtra(Const.Extras.EDIT_USER);
            refreshEditLayout();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.my_page_switch_compat) {
            if (isChecked) {
                SPUtils.set(getContext(), Const.SP_KEY_IS_NIGHT, true);
                UiModeManager uiModeManager = (UiModeManager) getActivity().getSystemService(Context.UI_MODE_SERVICE);
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //mMainActivity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                SPUtils.set(getContext(), Const.SP_KEY_IS_NIGHT, false);
                UiModeManager uiModeManager = (UiModeManager) getActivity().getSystemService(Context.UI_MODE_SERVICE);
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                //mMainActivity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            //mMainActivity.recreate();
        }
    }

    private void getPersonInfoData() {
        RequestManager.getInstance()
                      .getPersonInfo(new SimpleSubscriber<>(getActivity(),
                              new SubscriberListener<User>() {
                                  @Override
                                  public void onNext(User user) {
                                      super.onNext(user);
                                      mUser = user;
                                      refreshEditLayout();
                                  }


                                  @Override
                                  public void onCompleted() {
                                      super.onCompleted();
                                  }
                              }), mUser.stuNum, mUser.idNum);
    }


    private void refreshEditLayout() {
        ImageLoader.getInstance()
                   .loadAvatar(mUser.photo_thumbnail_src, myPageAvatar);
        myPageNickName.setText(mUser.nickname);
        myPageIntroduce.setText(mUser.introduction);
        if (mUser.gender.equals("男")) {
            myPageGender.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            myPageGender.setText("♂");
        } else {
            myPageGender.setTextColor(ContextCompat.getColor(getContext(), R.color.pink));
            myPageGender.setText("♀");
        }
    }
}
