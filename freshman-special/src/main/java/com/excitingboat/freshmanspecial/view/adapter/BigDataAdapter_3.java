package com.excitingboat.freshmanspecial.view.adapter;

import com.excitingboat.freshmanspecial.config.BigData;
import com.excitingboat.yellowcake.ColorText;
import com.excitingboat.yellowcake.Yellowcake;

/**
 * Created by PinkD on 2016/8/9.
 * BigDataAdapter for Fragment3
 */
public class BigDataAdapter_3 extends BigDataAdapter{

    @Override
    public void setData(Yellowcake yellowcake, MyColorTextAdapter myColorTextAdapter, int position) {
        yellowcake.setData(BigData.JOB_PERCENT[position], BigData.COLORS);
        int count = BigData.JOB_TYPE.length;
        for (int i = 0; i < count; i++) {
            myColorTextAdapter.add(new ColorText(BigData.COLORS[i], BigData.JOB_TYPE[i]));
        }
        myColorTextAdapter.notifyDataSetChanged();
    }
}
