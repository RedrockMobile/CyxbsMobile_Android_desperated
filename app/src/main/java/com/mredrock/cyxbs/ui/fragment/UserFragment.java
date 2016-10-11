package com.mredrock.cyxbs.ui.fragment;

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

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.me.AboutMeActivity;
import com.mredrock.cyxbs.ui.activity.me.EditInfoActivity;
import com.mredrock.cyxbs.ui.activity.me.EmptyRoomActivity;
import com.mredrock.cyxbs.ui.activity.me.ExamAndGradeActivity;
import com.mredrock.cyxbs.ui.activity.me.MyTrendActivity;
import com.mredrock.cyxbs.ui.activity.me.NoCourseActivity;
import com.mredrock.cyxbs.ui.activity.me.RemindActivity;
import com.mredrock.cyxbs.ui.activity.me.SchoolCalendarActivity;
import com.mredrock.cyxbs.ui.activity.me.SettingActivity;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.SPUtils;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的页面
 */
public class UserFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    public static final int REQUEST_EDIT_INFO = 10;

    @Bind(R.id.my_page_edit_layout)
    LinearLayout myPageEditLayout;
    @Bind(R.id.my_page_relate_layout)
    RelativeLayout myPageRelateLayout;
    @Bind(R.id.my_page_trend_layout)
    RelativeLayout myPageTrendLayout;
    @Bind(R.id.my_page_no_course_layout)
    RelativeLayout myPageNoCourseLayout;
    @Bind(R.id.my_page_empty_layout)
    RelativeLayout myPageEmptyLayout;
    @Bind(R.id.my_page_grade_layout)
    RelativeLayout myPageGradeLayout;
    @Bind(R.id.my_page_calendar_layout)
    RelativeLayout myPageCalendarLayout;
    @Bind(R.id.my_page_night_layout)
    RelativeLayout myPageNightLayout;
    @Bind(R.id.my_page_setting_layout)
    RelativeLayout myPageSettingLayout;
    @Bind(R.id.my_page_avatar)
    ImageView myPageAvatar;
    @Bind(R.id.my_page_nick_name)
    TextView myPageNickName;
    @Bind(R.id.my_page_gender)
    TextView myPageGender;
    @Bind(R.id.my_page_introduce)
    TextView myPageIntroduce;
    @Bind(R.id.my_page_switch_compat)
    SwitchCompat myPageSwitchCompat;
    @Bind(R.id.my_page_iv_relate)
    ImageView mMyPageIvRelate;
    @Bind(R.id.my_page_iv_trend)
    ImageView mMyPageIvTrend;
    @Bind(R.id.my_page_iv_no_course)
    ImageView mMyPageIvNoCourse;
    @Bind(R.id.my_page_iv_empty)
    ImageView mMyPageIvEmpty;
    @Bind(R.id.my_page_iv_grade)
    ImageView mMyPageIvGrade;
    @Bind(R.id.my_page_iv_calendar)
    ImageView mMyPageIvCalendar;
    @Bind(R.id.my_page_iv_remind)
    ImageView mMyPageIvRemind;
    @Bind(R.id.my_page_remind_layout)
    RelativeLayout mMyPageRemindLayout;
    @Bind(R.id.my_page_iv_night)
    ImageView mMyPageIvNight;
    @Bind(R.id.my_page_iv_setting)
    ImageView mMyPageIvSetting;

    private User mUser;

    @OnClick(R.id.my_page_edit_layout)
    void clickToEdit() {
        if (APP.isLogin()) {
            startActivity(new Intent(getActivity(), EditInfoActivity.class));
        } else {
            EventBus.getDefault().post(new LoginEvent());
        }
    }

    @OnClick(R.id.my_page_relate_layout)
    void clickToRelate() {
        if (APP.isLogin()) {
            startActivity(new Intent(getActivity(), AboutMeActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能查看与我相关哦"));
        }
    }

    @OnClick(R.id.my_page_trend_layout)
    void clickToLatest() {
        if (APP.isLogin()) {
            startActivity(new Intent(getActivity(), MyTrendActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能查看我的动态哦"));
        }
    }

    @OnClick(R.id.my_page_no_course_layout)
    void clickToNoCourse() {
        if (APP.isLogin()) {
            startActivity(new Intent(getActivity(), NoCourseActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能使用没课约哦"));
        }
    }

    @OnClick(R.id.my_page_empty_layout)
    void clickToEmpty() {
        startActivity(new Intent(getActivity(), EmptyRoomActivity.class));
    }

    @OnClick(R.id.my_page_grade_layout)
    void clickToGrade() {
        if (APP.isLogin()) {
            startActivity(new Intent(getActivity(), ExamAndGradeActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能查看考试成绩哦"));
        }
    }

    @OnClick(R.id.my_page_calendar_layout)
    void clickToCalendar() {
        startActivity(new Intent(getActivity(), SchoolCalendarActivity.class));
    }

    @OnClick(R.id.my_page_remind_layout)
    public void onClick() {
        if (APP.isLogin()) {
            startActivity(new Intent(getActivity(), RemindActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能使用课前提醒哟"));
        }
    }

    @OnClick(R.id.my_page_night_layout)
    void clickToNight() {
        if (myPageSwitchCompat.isChecked()) {
            SPUtils.set(getContext(), Const.SP_KEY_IS_NIGHT, false);
            myPageSwitchCompat.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            SPUtils.set(getContext(), Const.SP_KEY_IS_NIGHT, true);
            myPageSwitchCompat.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @OnClick(R.id.my_page_setting_layout)
    void clickToSetting() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
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
            //getActivity().recreate();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isNight = (boolean) SPUtils.get(getContext(), Const.SP_KEY_IS_NIGHT, false);
        myPageSwitchCompat.setChecked(isNight);
        myPageSwitchCompat.setOnCheckedChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPersonInfoData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getPersonInfoData() {
        if (!APP.isLogin()) {
            myPageNickName.setText("点我登录");
            myPageAvatar.setImageResource(R.drawable.ic_default_avatar);
            myPageIntroduce.setText("");
            myPageGender.setText("");
            return;
        }
        mUser = APP.getUser(getActivity());
        if (mUser != null) {
            RequestManager.getInstance().getPersonInfo(new SimpleSubscriber<>(getActivity(),
                    new SubscriberListener<User>() {
                        @Override
                        public void onNext(User user) {
                            super.onNext(user);
                            if (user != null) {
                                mUser = User.cloneFromUserInfo(mUser, user);
                                APP.setUser(getActivity(), mUser);
                                refreshEditLayout();
                            }
                        }

                    }), mUser.stuNum, mUser.idNum);
        }
    }

    private void refreshEditLayout() {
        if (APP.isLogin()) {
            mUser = APP.getUser(getActivity());
            ImageLoader.getInstance().loadAvatar(mUser.photo_thumbnail_src, myPageAvatar);
            myPageNickName.setText(StringUtils.isBlank(mUser.nickname) ? "点我完善个人信息" : mUser.nickname);
            myPageIntroduce.setText(mUser.introduction);
            if (mUser.gender.trim().equals("男")) {
                myPageGender.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                myPageGender.setText("♂");
            } else {
                myPageGender.setTextColor(ContextCompat.getColor(getContext(), R.color.pink));
                myPageGender.setText("♀");
            }
        } else {
            myPageNickName.setText("点我登录");
            myPageAvatar.setImageResource(R.drawable.ic_default_avatar);
            myPageIntroduce.setText("");
            myPageGender.setText("");
        }
    }


    @Override
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        super.onLoginStateChangeEvent(event);
        refreshEditLayout();
    }

}
