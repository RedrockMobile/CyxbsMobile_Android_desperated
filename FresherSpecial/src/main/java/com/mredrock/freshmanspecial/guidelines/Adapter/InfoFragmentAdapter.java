package com.mredrock.freshmanspecial.guidelines.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.ScreenUnit;

import java.util.List;

/**
 * Created by glossimar on 2017/8/3
 * 专题 ： 邮子攻略
 * 界面 ： 邮子攻略-FragmentAdapter
 */

public class InfoFragmentAdapter extends FragmentPagerAdapter {

    private List<String> titles;      //tab 标题
    private List<Fragment> fragments;

    private LayoutInflater layoutInflater;

    private Activity activity;

    public InfoFragmentAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments
                               , Activity activity) {
        super(fm);
        this.activity = activity;
        this.titles = titles;
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    /**
     *  getTabView : 自定义 Tab 的布局View
     * */
    public View getTabView(int position){
        layoutInflater =LayoutInflater.from(activity);
        View view=layoutInflater.inflate(R.layout.special_2017_tab_item_layout,null);
        int wide = ScreenUnit.bulid(activity).getPxWide();
        float density = ScreenUnit.bulid(activity).getDensity();
        TextView textView = (TextView) view.findViewById(R.id.tab_item_tabname);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) textView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        linearParams.width = (int)(wide-85*density)*3/12;// 控件的宽强制设置
        textView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        textView.setText(titles.get(position));
        if (position == 0) {
            textView.setTextColor(Color.parseColor("#65B2FF"));
        }
        return view;
    }
}
