package com.mredrock.freshmanspecial.units;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.freshmanspecial.R;

/**
 * Created by zia on 17-8-3.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Fragment[] fragments;
    private LayoutInflater layoutInflater;
    private Context context;

    public ViewPagerAdapter(Context context, FragmentManager fm, String[] titles, Fragment[] fragments) {
        super(fm);
        this.titles = titles;
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    /**
     *  getTabView : 自定义 Tab 的布局View
     * */
    public View getTabView(int position){
        layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.special_2017_tab_item_layout,null);
        int wide = ScreenUnit.bulid(context).getPxWide();
        float density = ScreenUnit.bulid(context).getDensity();
        TextView textView = (TextView) view.findViewById(R.id.tab_item_tabname);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) textView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        linearParams.width = (int)(wide-85*density)*2/7;// 控件的宽强制设置
        textView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        textView.setText(titles[position]);
        if (position == 0) {
            textView.setTextColor(Color.parseColor("#65B2FF"));
        }
        return view;
    }

    /**
     *  二级导航栏
     *  getTabView : 自定义 Tab 的布局View
     * */
    public View getSecondTabView(int position){
        layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.special_2017_tab_groups,null);
//        int wide = ScreenUnit.bulid(context).getPxWide();
//        float density = ScreenUnit.bulid(context).getDensity();
        TextView textView = (TextView) view.findViewById(R.id.tab_groups_title);
//        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) textView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
//        linearParams.width = (int)(wide-85*density)*2/7;// 控件的宽强制设置
//        textView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        textView.setText(titles[position]);
        return view;
    }
}
