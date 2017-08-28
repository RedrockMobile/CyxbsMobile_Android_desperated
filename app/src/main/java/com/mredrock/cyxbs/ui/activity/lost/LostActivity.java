package com.mredrock.cyxbs.ui.activity.lost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.lost.LostViewPagerAdapter;
import com.mredrock.cyxbs.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostActivity extends BaseActivity {

    @BindView(R.id.toolbar) RelativeLayout toolbar;
    @BindView(R.id.rb_lost) RadioButton chooseModeLost;
    @BindView(R.id.rb_found) RadioButton chooseModeFound;

    @BindView(R.id.view_pager) ViewPager pager;
    @BindView(R.id.tab_layout) TabLayout tab;

    LostViewPagerAdapter adapter;
    String[] lostKindList;

    public static final int THEME_LOST = 0;
    public static final int THEME_FOUND = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        ButterKnife.bind(this);

        lostKindList = getResources().getStringArray(R.array.lost_category_list);
        adapter = new LostViewPagerAdapter(getSupportFragmentManager());
        LogUtils.LOGE("LostActivity",adapter.getItem(0).toString());
        pager.setAdapter(adapter);

        pager.setCurrentItem(0);
        tab.setupWithViewPager(pager, true);
        chooseModeLost.setOnClickListener((v) -> refreshMode());
        chooseModeFound.setOnClickListener((v) -> refreshMode());
    }

    private void refreshMode() {
        adapter.setMode(getMode());
    }

    private int getMode() {
        if (chooseModeLost.isChecked()) {
            return THEME_LOST;
        } else {
            return THEME_FOUND;
        }
    }

    @OnClick(R.id.bt_navigation_up)
    public void onNavigationUpClick() {
        finish();
    }



    public static void start(Context context) {
        Intent starter = new Intent(context, LostActivity.class);
        context.startActivity(starter);
    }

}
