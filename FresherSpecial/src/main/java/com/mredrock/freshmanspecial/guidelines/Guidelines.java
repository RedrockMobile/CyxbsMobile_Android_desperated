package com.mredrock.freshmanspecial.guidelines;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.freshmanspecial.guidelines.Adapter.InfoFragmentAdapter;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.AdmissionNoticeFragment;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.CafeteriaFragment;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.CampusEnvironmentFragment;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.DailyLifeFragment;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.DormitoryFragment;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.PeripheralCuisineFragment;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.QQgroupFragment;
import com.mredrock.freshmanspecial.guidelines.GiudelinesFragment.SurroundingBeautyFragment;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.base.BaseActivity;
import com.mredrock.freshmanspecial.model.TabModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glossimar on 2017/8/3
 * 专题 ： 邮子攻略
 * 界面 ： 邮子攻略-主界面
 **/

public class Guidelines extends BaseActivity {


    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private TextView title;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView back;
    private View view_left, view_right;
    private InfoFragmentAdapter adapter;
    private TabModel tabModel;


    @Override
    public void initData() {
        titles.add("校园环境");
        titles.add("学生寝室");
        titles.add("学校食堂");
        titles.add("入学须知");
        titles.add("QQ群");
        titles.add("日常生活");
        titles.add("周边美食");
        titles.add("周边美景");

        fragments.add(new CampusEnvironmentFragment());
        fragments.add(new DormitoryFragment());
        fragments.add(new CafeteriaFragment());
        fragments.add(new AdmissionNoticeFragment());
        fragments.add(new QQgroupFragment());
        fragments.add(new DailyLifeFragment());
        fragments.add(new PeripheralCuisineFragment());
        fragments.add(new SurroundingBeautyFragment());

        adapter = new InfoFragmentAdapter(getSupportFragmentManager()
                , titles, fragments, Guidelines.this);
        initViews();
        title.setText("邮子攻略");
        setBack();
    }

    private void setBack(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.special_2017_activity_guidelines;
    }

    public void initViews() {
        tabLayout = (TabLayout) findViewById(R.id.guidelines_tablayout);
        viewPager = (ViewPager) findViewById(R.id.guidelines_viewpager);
        title = (TextView) findViewById(R.id.title_text);
        back = (ImageView) findViewById(R.id.back);
        view_left = $(R.id.tab_left_guidelines);
        view_right = $(R.id.tab_right_guidelines);
        tabModel = new TabModel();
        setTabLayout();
//        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.guidelines_horizontal_scroller);
    }

    private void setTabLayout() {
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    view_left.setVisibility(View.GONE);
                    view_right.setVisibility(View.VISIBLE);
                    return;
                }
                if (position == 7){
                    view_right.setVisibility(View.GONE);
                    view_left.setVisibility(View.VISIBLE);
                    return;
                } else {
                    view_left.setVisibility(View.VISIBLE);
                    view_right.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tabLayout != null) {
                tab.setCustomView(adapter.getTabView(i)); // 将自定义的tab加入
            }
        }
        //监听tab实现变色
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = (TextView) view.findViewById(R.id.tab_item_tabname);
                textView.setTextColor(Color.parseColor("#65B2FF"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = (TextView) view.findViewById(R.id.tab_item_tabname);
                textView.setTextColor(Color.parseColor("#FF383535"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //调整tab下划线宽度  滑动时有点卡顿先注释掉
//        final int padding = ScreenUnit.bulid(this).getDpWide()/20;
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                tabModel.setIndicator(tabLayout,padding,padding);
//            }
//        });

    }
}
