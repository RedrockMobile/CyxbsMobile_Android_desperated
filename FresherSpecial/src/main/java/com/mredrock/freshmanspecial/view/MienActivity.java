package com.mredrock.freshmanspecial.view;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.ScreenUnit;
import com.mredrock.freshmanspecial.units.ViewPagerAdapter;
import com.mredrock.freshmanspecial.units.base.BaseActivity;
import com.mredrock.freshmanspecial.presenter.IMienPresenter;
import com.mredrock.freshmanspecial.presenter.MienPresenter;
import com.mredrock.freshmanspecial.view.MienFragments.BeautyFragment;
import com.mredrock.freshmanspecial.view.MienFragments.OriginalFragment;
import com.mredrock.freshmanspecial.view.MienFragments.StudentFragment;
import com.mredrock.freshmanspecial.view.MienFragments.StudentGroupFragment;
import com.mredrock.freshmanspecial.view.MienFragments.TeacherFragment;

public class MienActivity extends BaseActivity implements IMienActivity{


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private IMienPresenter presenter;
    private TextView title;
    private ImageView back;
    private View view_left, view_right;


    private void setTitle(String title) {
        this.title.setText(title);
    }

    private void setPager() {
        //设置下划线边距
//        presenter.setTabLayoutBottomLine(20);
        int wide = ScreenUnit.bulid(this).getDpWide();
//        presenter.setTabLayoutBottomLine(wide/30);

    }

    private void initPager() {
//        int wide = ScreenUnit.bulid(this).getDpWide()*2/7;
//        tabLayout.setMinimumWidth(wide);
        String[] tabTitles = new String[]{"学生组织", "原创重邮", "美在重邮","优秀学生","优秀教师"};
        Fragment[] fragments = new Fragment[]{new StudentGroupFragment(), new OriginalFragment(), new BeautyFragment(), new StudentFragment(), new TeacherFragment()};
        ViewPagerAdapter adapter = new ViewPagerAdapter(this,getSupportFragmentManager(), tabTitles, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tabLayout != null) {
                tab.setCustomView(adapter.getTabView(i)); // 将自定义的tab加入
            }
        }
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
                if (position == 4){
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
    protected void initData() {
        viewPager = $(R.id.vp_mien);
        tabLayout = $(R.id.tab_mien);
        title = $(R.id.title_text);
        back = $(R.id.back);
        view_left = $(R.id.tab_left_mien);
        view_right = $(R.id.tab_right_mien);
        presenter = new MienPresenter(this);
        initPager();
        setPager();
        setTitle("重邮风采");
        setBack();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.special_2017_activity_cqupt_mien;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }


}
