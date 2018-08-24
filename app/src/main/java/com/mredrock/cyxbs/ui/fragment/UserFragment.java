package com.mredrock.cyxbs.ui.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.me.AboutMeActivity;
import com.mredrock.cyxbs.ui.activity.me.EditInfoActivity;
import com.mredrock.cyxbs.ui.activity.me.EmptyRoomQueryActivity;
import com.mredrock.cyxbs.ui.activity.me.ExamAndGradeActivity;
import com.mredrock.cyxbs.ui.activity.me.MyTrendActivity;
import com.mredrock.cyxbs.ui.activity.me.NoCourseActivity;
import com.mredrock.cyxbs.ui.activity.me.SchoolCalendarActivity;
import com.mredrock.cyxbs.ui.activity.me.SettingActivity;
import com.mredrock.cyxbs.ui.activity.me.VolunteerTimeLoginActivity;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.SPUtils;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的页面
 */
public class UserFragment extends BaseFragment /*implements CompoundButton.OnCheckedChangeListener*/ {

    public static final int REQUEST_EDIT_INFO = 10;
    private Context mContext;

    @BindView(R.id.relate)
    LinearLayout myPageRelateLayout;
    @BindView(R.id.trend)
    LinearLayout myPageTrendLayout;
    @BindView(R.id.no_course)
    RelativeLayout myPageNoCourseLayout;
    @BindView(R.id.empty_classroom)
    RelativeLayout myPageEmptyLayout;
    @BindView(R.id.grade)
    RelativeLayout myPageGradeLayout;
    @BindView(R.id.calendar)
    RelativeLayout myPageCalendarLayout;
    @BindView(R.id.volunteer_time)
    RelativeLayout myVolunteerTime;
    @BindView(R.id.option)
    RelativeLayout myPageSettingLayout;
    @BindView(R.id.avatar)
    ImageView myPageAvatar;
    @BindView(R.id.name)
    TextView myPageNickName;
    @BindView(R.id.introduce)
    TextView myPageIntroduce;

    @BindView(R.id.new_function)
    ImageView mNewFunction;

    private User mUser;

    @OnClick({R.id.name, R.id.introduce, R.id.avatar})
    void clickToEdit() {
        if (BaseAPP.isLogin()) {
            startActivity(new Intent(getActivity(), EditInfoActivity.class));
        } else {
            EventBus.getDefault().post(new LoginEvent());
        }
    }

    @OnClick(R.id.relate)
    void clickToRelate() {
        if (BaseAPP.isLogin()) {
            startActivity(new Intent(getActivity(), AboutMeActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能查看与我相关哦"));
        }
    }

    @OnClick(R.id.trend)
    void clickToLatest() {
        if (BaseAPP.isLogin()) {
            startActivity(new Intent(getActivity(), MyTrendActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能查看我的动态哦"));
        }
    }

    @OnClick(R.id.no_course)
    void clickToNoCourse() {
        if (BaseAPP.isLogin()) {
            startActivity(new Intent(getActivity(), NoCourseActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能使用没课约哦"));
        }
    }

    @OnClick(R.id.empty_classroom)
    void clickToEmpty() {
        startActivity(new Intent(getActivity(), EmptyRoomQueryActivity.class));
    }

    @OnClick(R.id.grade)
    void clickToGrade() {
        if (BaseAPP.isLogin()) {
            startActivity(new Intent(getActivity(), ExamAndGradeActivity.class));
        } else {
            EventBus.getDefault().post(new AskLoginEvent("登录后才能查看考试成绩哦"));
        }
    }

    @OnClick(R.id.calendar)
    void clickToCalendar() {
        startActivity(new Intent(getActivity(), SchoolCalendarActivity.class));
    }

    @OnClick(R.id.volunteer_time)
    void clickToVolunteerTime() {
        SPUtils.set(getContext(), "showNewFunctionIcon", false);
        mNewFunction.setVisibility(View.GONE);
        startActivity(new Intent(getActivity(), VolunteerTimeLoginActivity.class));
    }
/*

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
*/

    @OnClick(R.id.option)
    void clickToSetting() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }
/*

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
*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        getPersonInfoData();
        boolean showNewFunctionIcon = (boolean) SPUtils.get(getContext(), "showNewFunctionIcon", true);
        mNewFunction.setVisibility(showNewFunctionIcon ? View.VISIBLE : View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshEditLayout();
    }

    /*

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isNight = (boolean) SPUtils.get(getContext(), Const.SP_KEY_IS_NIGHT, false);
        myPageSwitchCompat.setChecked(isNight);
        myPageSwitchCompat.setOnCheckedChangeListener(this);
    }
*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void getPersonInfoData() {
        if (!BaseAPP.isLogin()) {
            myPageNickName.setText("点我登录");
            myPageAvatar.setImageResource(R.drawable.default_avatar);
            myPageIntroduce.setText("");
            //myPageGender.setText("");
            return;
        }
        mUser = BaseAPP.getUser(getActivity());
        if (mUser != null) {
            RequestManager.getInstance().getPersonInfo(new SimpleObserver<>(getActivity(),
                    new SubscriberListener<User>() {
                        @Override
                        public void onNext(User user) {
                            super.onNext(user);
                            if (user != null) {
                                Log.d("zzxzzx", "onNext: "+mContext);
                                mUser = User.cloneFromUserInfo(mUser, user);
                                BaseAPP.setUser(mContext, mUser);
                                refreshEditLayout();
                            }
                        }

                    }), mUser.stuNum, mUser.idNum);
        }
    }

    private void refreshEditLayout() {
        if (BaseAPP.isLogin()) {
            mUser = BaseAPP.getUser(getActivity());
            ImageLoader.getInstance().loadAvatar(mUser.photo_thumbnail_src, myPageAvatar);
            myPageNickName.setText(StringUtils.isBlank(mUser.nickname) ? "点我完善个人信息" : mUser.nickname);
            myPageIntroduce.setText(mUser.introduction);
            /*if (mUser.gender.trim().equals("男")) {
                myPageGender.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                myPageGender.setText("♂");
            } else {
                myPageGender.setTextColor(ContextCompat.getColor(getContext(), R.color.pink));
                myPageGender.setText("♀");
            }*/
        } else {
            myPageNickName.setText("点我登录");
            myPageAvatar.setImageResource(R.drawable.default_avatar);
            myPageIntroduce.setText("");
            //myPageGender.setText("");
        }
    }


    @Override
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        super.onLoginStateChangeEvent(event);
        refreshEditLayout();
    }

}
