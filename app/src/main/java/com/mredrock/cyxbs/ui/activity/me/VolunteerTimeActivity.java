package com.mredrock.cyxbs.ui.activity.me;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;
import com.mredrock.cyxbs.model.social.Image;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.VolunteerFragmentAdapter;
import com.mredrock.cyxbs.ui.fragment.me.AllVolunteerFragment;
import com.mredrock.cyxbs.ui.fragment.me.FirstVolunteerTimeFragment;
import com.mredrock.cyxbs.ui.fragment.me.NoTimeVolunteerFragment;
import com.mredrock.cyxbs.ui.widget.VolunteerTimeSP;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by glossimarsun on 2017/10/2.
 */


public class VolunteerTimeActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener{
    private static final String TAG = "VolunteerTimeActivity";

    private String uid;
    private String account;
    private String password;
    private List<String> yearList;
    private List<String> allYearList;
    private List<Fragment> fragmentList;
    private VolunteerTimeSP volunteerSP;
    private AnimationDrawable animationDrawable;
    private List<VolunteerTime.DataBean.RecordBean> firstYear;
    private List<VolunteerTime.DataBean.RecordBean> secondYear;
    private List<VolunteerTime.DataBean.RecordBean> thirdYear;
    private List<VolunteerTime.DataBean.RecordBean> lastYear;
    private TreeMap<Integer, List<VolunteerTime.DataBean.RecordBean>> yearMap;

    @BindView(R.id.volunteer_time_toolbar)
    Toolbar toolbar;
    @BindView(R.id.volunteer_time_back)
    ImageView backIcon;
    @BindView(R.id.volunteer_unbind)
    TextView unbindInfo;
    @BindView(R.id.volunteer_view_pager)
    ViewPager viewPager;
    @BindView(R.id.volunteer_time_tab)
    TabLayout tabLayout;
    @BindView(R.id.volunteer_show_image)
    ImageView showTab;
    @BindView(R.id.volunteer_unshow_image)
    ImageView unshowTab;
    @BindView(R.id.volunteer_time_title)
    TextView toolbarTittle;
    @BindView(R.id.volunteer_refresh)
    ImageView refreshView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_time);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }

    public void initData() {
        volunteerSP = new VolunteerTimeSP(this);
        uid = volunteerSP.getVolunteerUid();
        account = volunteerSP.getVolunteerAccount();
        password = volunteerSP.getVolunteerPassword();
        animationDrawable = (AnimationDrawable)refreshView.getDrawable();
        if (uid.equals("404") || volunteerSP.getVolunteerAccount().equals("404") ||
                    volunteerSP.getVolunteerPassword().equals("404")){
            Toast.makeText(this, "请先登录绑定账号哦", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, VolunteerTimeLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            animationDrawable.start();
            loadVolunteerTime(account, password);
        }
    }
//    private void loadVolunteerTime(String uid) {
//        RequestManager.INSTANCE.getVolunteerTime(new Observer<VolunteerTime.DataBean>() {
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(VolunteerTime.DataBean dataBean) {
//                fragmentList = new ArrayList<>();
//                allYearList = new ArrayList<>();
//                animationDrawable.stop();
//                refreshView.setVisibility(View.GONE);
//
//                initializeYears();
//                initFragmentList(dataBean);
//                setTabLayout();
//            }
//        }, uid);
//    }


    private void loadVolunteerTime(String account, String password) {
        RequestManager.INSTANCE.getVolunteer(new Observer<VolunteerTime>() {

            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(VolunteerTime dataBean) {
                fragmentList = new ArrayList<>();
                allYearList = new ArrayList<>();
                animationDrawable.stop();
                refreshView.setVisibility(View.GONE);

                initializeYears();
                initFragmentList(dataBean.getData());
                setTabLayout();
                }
        }, account, password);
    }

    private void setTabLayout(){
        for (String tabTitles: yearList){
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles));
        }

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new VolunteerFragmentAdapter(getFragmentManager(), fragmentList, yearList));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void initializeYears(){
        yearList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        yearList.add("全部");
        for (int i = 0; i < 4; i ++) {
            yearList.add(year - i + "");
        }
    }

    private void initFragmentList(VolunteerTime.DataBean dataBean){
        if (dataBean == null || dataBean.getRecord() == null || dataBean.getRecord().size() == 0) {
            for (int i = 0; i < 5; i++) {
                fragmentList.add(new NoTimeVolunteerFragment());
            }
        } else {
            List<VolunteerTime.DataBean.RecordBean> recordBeen;
            List<List<VolunteerTime.DataBean.RecordBean>> allRecordList = new ArrayList<>();
            AllVolunteerFragment allFragment = new AllVolunteerFragment();
            allFragment.setContext(VolunteerTimeActivity.this);
            allFragment.setAllHour(dataBean.getHours() + "");

            for (int i = 1; i < yearList.size(); i++) {
                dealYear(dataBean, i - 1);
                recordBeen = yearMap.get(Integer.parseInt(yearList.get(i)));
                allRecordList.add(recordBeen);
                if (recordBeen == null || recordBeen.size() == 0) {
                    fragmentList.add(new NoTimeVolunteerFragment());
                } else if (recordBeen.size() != 0 && yearMap.get(Integer.parseInt(yearList.get(i))).size() > 0 && !(yearList.get(i).equals("全部"))
                        ) {
                    FirstVolunteerTimeFragment fragment = new FirstVolunteerTimeFragment();
                    fragment.setContext(VolunteerTimeActivity.this);
                    fragment.setRecordBeanList(recordBeen);
                    fragment.setYear(yearList.get(i));
                    fragmentList.add(fragment);

                }
            }

            allFragment.setRecordBeanList(allRecordList);
            allFragment.setYearList(allYearList);
            fragmentList.add(0, allFragment);
        }
    }

    private void dealYear(VolunteerTime.DataBean dataBean, int nowYear) {
        Calendar calendar = Calendar.getInstance();

        int year;
        int yearCalender = calendar.get(Calendar.YEAR);
        yearMap = new TreeMap<>();
        firstYear = new ArrayList<>();
        secondYear = new ArrayList<>();
        thirdYear = new ArrayList<>();
        lastYear = new ArrayList<>();
        List<Integer> yearListInt = new ArrayList<>();
        List<VolunteerTime.DataBean.RecordBean> recordBeen = dataBean.getRecord();
        for (int x = 0; x < 4; x ++) {
            yearListInt.add(yearCalender - x);
        }
        for (int x = 0; x < recordBeen.size(); x ++) {
            year = Integer.parseInt(recordBeen.get(x).getStart_time().substring(0,4));
            if (year == yearListInt.get(nowYear)) {
                switch (nowYear) {
                    case 0:
                        firstYear.add(recordBeen.get(x));
                        break;
                    case 1:
                        secondYear.add(recordBeen.get(x));
                        break;
                    case 2:
                        thirdYear.add(recordBeen.get(x));
                        break;
                    case 3:
                        lastYear.add(recordBeen.get(x));
                        break;
                }
            }
        }

        switch (nowYear) {
            case 0:
                yearMap.put(yearListInt.get(nowYear), firstYear);
                if (firstYear.size() != 0) allYearList.add(yearListInt.get(nowYear) + "");
                break;
            case 1:
                yearMap.put(yearListInt.get(nowYear), secondYear);
                if (secondYear.size() != 0) allYearList.add(yearListInt.get(nowYear) + "");
                break;
            case 2:
                yearMap.put(yearListInt.get(nowYear), thirdYear);
                if (thirdYear.size() != 0) allYearList.add(yearListInt.get(nowYear) + "");
                break;
            case 3:
                yearMap.put(yearListInt.get(nowYear), lastYear);
                if (lastYear.size() != 0) allYearList.add(yearListInt.get(nowYear) + "");
                break;
        }
    }

    @OnClick(R.id.volunteer_unbind)
    public void unbindClick(View v) {
        if (v.getId() == R.id.volunteer_unbind) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> new MaterialDialog.Builder(this)
                    .title("解除账号绑定？")
                    .content("亲，真的要取消已绑定的账号吗？")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            volunteerSP.unBindVolunteerInfo();
                            Intent intent = new Intent(VolunteerTimeActivity.this, VolunteerTimeLoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    }).show());
        }
    }

    @OnClick(R.id.volunteer_time_title)
    public void showTabFromTitle(View view) {
        if (view.getId() == R.id.volunteer_time_title) {
            if (tabLayout.getVisibility() == View.VISIBLE) {
                tabLayout.setVisibility(View.GONE);
                unshowTab.setVisibility(View.VISIBLE);
                showTab.setVisibility(View.GONE);
            } else if (tabLayout.getVisibility() == View.GONE) {
                tabLayout.setVisibility(View.VISIBLE);
                unshowTab.setVisibility(View.GONE);
                showTab.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.volunteer_time_back)
    public void finishActivity(View view) {
        if (view.getId() == R.id.volunteer_time_back) {
            finish();
        }
    }

    @OnClick(R.id.volunteer_show_image)
    public void showTab(View view){
        if (view.getId() == R.id.volunteer_show_image) {
            if (tabLayout.getVisibility() == View.VISIBLE) {
                tabLayout.setVisibility(View.GONE);
                unshowTab.setVisibility(View.VISIBLE);
                showTab.setVisibility(View.GONE);
            } else if (tabLayout.getVisibility() == View.GONE) {
                tabLayout.setVisibility(View.VISIBLE);
                unshowTab.setVisibility(View.GONE);
                showTab.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.volunteer_unshow_image)
    public void unshowTab(View view){
        if (view.getId() == R.id.volunteer_unshow_image) {
            if (tabLayout.getVisibility() == View.VISIBLE) {
                tabLayout.setVisibility(View.GONE);
                unshowTab.setVisibility(View.VISIBLE);
                showTab.setVisibility(View.GONE);
            } else if (tabLayout.getVisibility() == View.GONE) {
                tabLayout.setVisibility(View.VISIBLE);
                unshowTab.setVisibility(View.GONE);
                showTab.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (toolbar != null)
            toolbarTittle.setText(yearList.get(tab.getPosition()));

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (toolbar != null)
            toolbarTittle.setText(yearList.get(position));
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
