package com.mredrock.cyxbs.ui.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.CourseDialog;
import com.mredrock.cyxbs.component.widget.ScheduleView;
import com.mredrock.cyxbs.event.LoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.affair.EditAffairActivity;
import com.mredrock.cyxbs.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbs.ui.activity.explore.electric.DormitorySettingActivity;
import com.mredrock.cyxbs.ui.activity.me.NewsRemindActivity;
import com.mredrock.cyxbs.ui.activity.me.NoCourseActivity;
import com.mredrock.cyxbs.ui.activity.social.PostNewsActivity;
import com.mredrock.cyxbs.ui.adapter.TabPagerAdapter;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.ui.fragment.CourseContainerFragment;
import com.mredrock.cyxbs.ui.fragment.UnLoginFragment;
import com.mredrock.cyxbs.ui.fragment.UserFragment;
import com.mredrock.cyxbs.ui.fragment.explore.ExploreFragment;
import com.mredrock.cyxbs.ui.fragment.social.SocialContainerFragment;
import com.mredrock.cyxbs.ui.widget.BottomNavigationViewHelper;
import com.mredrock.cyxbs.ui.widget.JToolbar;
import com.mredrock.cyxbs.util.DensityUtils;
import com.mredrock.cyxbs.util.ElectricRemindUtil;
import com.mredrock.cyxbs.util.SPUtils;
import com.mredrock.cyxbs.util.SchoolCalendar;
import com.mredrock.cyxbs.util.UpdateUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_toolbar)
    JToolbar mToolbar;
    @BindView(R.id.main_coordinator_layout)
    LinearLayout mCoordinatorLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;

    @BindString(R.string.community)
    String mStringCommunity;
    @BindString(R.string.course)
    String mStringCourse;
    @BindString(R.string.explore)
    String mStringExplore;
    @BindString(R.string.my_page)
    String mStringMyPage;

    BaseFragment socialContainerFragment;
    BaseFragment courseContainerFragment;
    BaseFragment exploreFragment;
    BaseFragment userFragment;
    BaseFragment unLoginFragment;
    /*@BindView(R.id.main_toolbar_face)
    CircleImageView mMainToolbarFace;*/
    @BindView(R.id.main_bnv)
    BottomNavigationView mMainBottomNavView;

    private Menu mMenu;
    private ArrayList<Fragment> mFragments;
    private TabPagerAdapter mAdapter;
    private boolean mUnfold;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        ButterKnife.bind(this);
        initView();
        UpdateUtil.checkUpdate(this, false, new RxPermissions(this));
        ElectricRemindUtil.check(this);
        setCourseUnfold(true, false);
        // FIXME: 2016/10/23 won't be call when resume, such as start by press app widget after dismiss this activity by press HOME button, set launchMode to normal may fix it but will launch MainActivity many times.
        // TODO: Filter these intents in another activity (such as LaunchActivity), not here, to fix the fixme above
        intentFilterFor3DTouch();
        BottomNavigationViewHelper btNavViewHelper = new BottomNavigationViewHelper(mMainBottomNavView);
        btNavViewHelper.enableBottomNavAnim(false);
        btNavViewHelper.setBottomNavTextSize(10);
    }

    /**
     * 适配魅族 3D TOUCH
     */
    private void intentFilterFor3DTouch() {
        Uri data = getIntent().getData();
        if (data != null && TextUtils.equals("forcetouch", data.getScheme())) {
            Log.d(TAG, "InterFilter: ");
            if (TextUtils.equals("/schedule", data.getPath())) {
                Log.d(TAG, "InterFilter: 进入主页");
            }
            if (TextUtils.equals("/new", data.getPath())) {
                Intent intent = new Intent(this, NewsRemindActivity.class);
                startActivity(intent);
            }
            if (TextUtils.equals("/foods", data.getPath())) {
                Intent intent = new Intent(this, SurroundingFoodActivity.class);
                startActivity(intent);
            }
            if (TextUtils.equals("/date", data.getPath())) {
                Intent intent = new Intent(this, NoCourseActivity.class);
                startActivity(intent);
            }
        }
    }

    private void checkCourseListToShow() {
        ScheduleView.CourseList courseList = ActionActivity.getCourseListToShow();
        if (courseList != null) {
            CourseDialog.show(this, courseList);
        }
    }

    private void initView() {
        initToolbar();
        socialContainerFragment = new SocialContainerFragment();
        courseContainerFragment = new CourseContainerFragment();
        exploreFragment = new ExploreFragment();
        userFragment = new UserFragment();
        unLoginFragment = new UnLoginFragment();

        mFragments = new ArrayList<>();
        //判断是否登陆
        if (!BaseAPP.isLogin()) {
            mFragments.add(unLoginFragment);
//            unLoginFace();
        } else {
            mFragments.add(courseContainerFragment);
//            loginFace();
        }
        mFragments.add(socialContainerFragment);
        mFragments.add(exploreFragment);
        mFragments.add(userFragment);

        ArrayList<String> titles = new ArrayList<>();
        titles.add(mStringCourse);
        titles.add(mStringCommunity);
        titles.add(mStringExplore);
        titles.add(mStringMyPage);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, titles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPagerChangedListener());
        mMainBottomNavView.setOnNavigationItemSelectedListener(new BottomSelectedListener());
    }
/*

    private void unLoginFace() {
        Glide.with(this).load(R.drawable.ic_default_avatar).into(mMainToolbarFace);
        mMainToolbarFace.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class)));
    }

    private void loginFace() {
        ImageLoader.getInstance().loadAvatar(BaseAPP.getUser(this).photo_thumbnail_src, mMainToolbarFace);
        mMainToolbarFace.setOnClickListener(view ->
                startActivity(new Intent(this, EditInfoActivity.class)));
    }
*/

    @Override
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        super.onLoginStateChangeEvent(event);
        boolean isLogin = event.getNewState();
        Log.d(TAG, "onLoginStateChangeEvent: " + BaseAPP.isFresh());
        if (!isLogin) {
            mFragments.remove(0);
            mFragments.add(0, new UnLoginFragment());
            mAdapter.notifyDataSetChanged();
            SPUtils.set(BaseAPP.getContext(), DormitorySettingActivity.BUILDING_KEY, -1);
            SPUtils.set(BaseAPP.getContext(), ElectricRemindUtil.SP_KEY_ELECTRIC_REMIND_TIME, System.currentTimeMillis() / 2);
//            unLoginFace();
        } else {
            mFragments.remove(0);
            mFragments.add(0, new CourseContainerFragment());
            //mBottomBar.setCurrentView(0);
            mAdapter.notifyDataSetChanged();
//            loginFace();
        }
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setTitle("课 表");
            mToolbar.getTitleTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            mToolbar.getTitleTextView().setCompoundDrawablePadding(
                    DensityUtils.dp2px(this, 5)
            );
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_news:
                if (BaseAPP.isLogin()) {
                    if (mViewPager.getCurrentItem() == 1) {
                        if (BaseAPP.getUser(this).id == null || BaseAPP.getUser(this).id.equals("0")) {
                            RequestManager.getInstance().checkWithUserId("还没有完善信息，不能发动态哟！");
                            mViewPager.setCurrentItem(3);
                            //mBottomBar.setCurrentView(3);
                            return super.onOptionsItemSelected(item);
                        } else
                            PostNewsActivity.startActivity(this);
                    } else {
                        EditAffairActivity.editAffairActivityStart(this, new SchoolCalendar().getWeekOfTerm());
                    }
                } else {
                    // Utils.toast(getApplicationContext(), "尚未登录");
                    EventBus.getDefault().post(new LoginEvent());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

/*
    public void showPopupWindow() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int xOffset = frame.top + mToolbar.getHeight() - 60;//减去阴影宽度，适配UI.
        int yOffset = Utils.dip2px(this, 15f); //设置x方向offset为5dp
        View parentView = getLayoutInflater().inflate(R.layout.activity_main, null);
        View popView = getLayoutInflater().inflate(
                R.layout.popup_window_add_remind, null);
        PopupWindow popWind = new PopupWindow(popView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);//popView即popupWindow的布局，ture设置focusAble.

        //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效。这里在XML中定义背景，所以这里设置为null;
        popWind.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popWind.setOutsideTouchable(true); //点击外部关闭。
        popWind.setAnimationStyle(R.style.PopupAnimation);    //设置一个动画。
        //设置Gravity，让它显示在右上角。
        if (popWind.getContentView() != null) {
            popWind.getContentView().findViewById(R.id.tv_popup_window_add_affair).setOnClickListener((v -> {
                EditAffairActivity.editAffairActivityStart(this, new SchoolCalendar().getWeekOfTerm());
                popWind.dismiss();
            }));
            popWind.getContentView().findViewById(R.id.tv_popup_window_fetch_course).setOnClickListener((v -> {
                if (courseContainerFragment != null) {
                    ((CourseContainerFragment) courseContainerFragment).forceFetchCourse();
                }
                popWind.dismiss();

            }));
        }

        popWind.showAtLocation(parentView, Gravity.RIGHT | Gravity.TOP,
                yOffset, xOffset);
    }
*/


    private void hiddenMenu() {
        if (null != mMenu) {
            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(false);
            }
        }
    }

    private void showMenu() {
        if (null != mMenu) {
            for (int i = 0; i < mMenu.size(); i++) {
                mMenu.getItem(i).setVisible(true);
            }
        }
    }

    public TextView getToolbarTitle() {
        return mToolbar.getTitleTextView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

    private class ViewPagerChangedListener implements ViewPager.OnPageChangeListener {

        float preOffset = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            switch (position) {
                case 0:
                    break;
                case 1:
//                    if (positionOffset > preOffset) {
//                        toolbarStepByStepClose(positionOffset, true);
//                    } else {
//                        toolbarStepByStepClose(positionOffset, false);
//                    }
//                    preOffset = positionOffset;
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mMainBottomNavView.setSelectedItemId(R.id.item1);
                    mToolbar.setVisibility(View.VISIBLE);
                    showMenu();
                    setTitle(((CourseContainerFragment) courseContainerFragment).getTitle());
//                    mMainToolbarFace.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mMainBottomNavView.setSelectedItemId(R.id.item2);
                    hiddenMenu();
//                    mMainToolbarFace.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.GONE);
                    break;
                case 2:
                    hiddenMenu();
                    mMainBottomNavView.setSelectedItemId(R.id.item3);
//                    mMainToolbarFace.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                    setTitle("发 现");
                    break;
                case 3:
                    hiddenMenu();
                    mMainBottomNavView.setSelectedItemId(R.id.item4);
                    mToolbar.setVisibility(View.VISIBLE);
//                    mMainToolbarFace.setVisibility(View.GONE);
                    setTitle("我 的");
                    if (!BaseAPP.isLogin()) {
                        EventBus.getDefault().post(new LoginEvent());
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class BottomSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getOrder()) {
                case 0:
                    mViewPager.setCurrentItem(0);
                    mToolbar.setVisibility(View.VISIBLE);
                    setCourseUnfold(true, mUnfold);
                    showMenu();
                    setTitle(((CourseContainerFragment) courseContainerFragment).getTitle());
//                    mMainToolbarFace.setVisibility(View.GONE);
                    break;
                case 1:
                    mViewPager.setCurrentItem(1);
//                    mMainToolbarFace.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                    setCourseUnfold(false, mUnfold);
                    setTitle("社 区");
                    hiddenMenu();
                    break;
                case 2:
                    hiddenMenu();
                    mViewPager.setCurrentItem(2);
//                    mMainToolbarFace.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                    setCourseUnfold(false, mUnfold);
                    setTitle("发 现");
                    break;
                case 3:
                    hiddenMenu();
                    mViewPager.setCurrentItem(3);
                    mToolbar.setVisibility(View.GONE);
//                    mMainToolbarFace.setVisibility(View.GONE);
                    setCourseUnfold(false, mUnfold);
                    if (!BaseAPP.isLogin()) {
                        EventBus.getDefault().post(new LoginEvent());
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCourseListToShow();
    }

    public void setCourseUnfold(boolean isShow, boolean unFold) {
        if (!isShow) {
            mToolbar.getTitleTextView().setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null
            );
            return;
        }
        mUnfold = unFold;
        int id = unFold ? R.drawable.ic_course_fold : R.drawable.ic_course_expand;
        Drawable drawable = getResources().getDrawable(id);
        mToolbar.getTitleTextView().setCompoundDrawablesWithIntrinsicBounds(
                null, null, drawable, null
        );
    }
}