package com.mredrock.freshmanspecial.units;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mredrock.freshmanspecial.view.JunxunFragments.SlideFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-12.
 */

public class SlidePagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<String> titleList = new ArrayList<>();
    private List<String> imageUrlList = new ArrayList<>();

    public SlidePagerAdapter(FragmentManager fm,Context context,List<String> titleList,List<String> imageUrlList) {
        super(fm);
        this.context = context;
        this.titleList = titleList;
        this.imageUrlList = imageUrlList;
    }

    @Override
    public Fragment getItem(int position) {
        return new SlideFragment(titleList.get(position),imageUrlList.get(position));
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }
}
