package com.excitingboat.freshmanspecial.view.adapter;

import com.excitingboat.freshmanspecial.config.BigData;
import com.excitingboat.yellowcake.ColorText;
import com.excitingboat.yellowcake.Yellowcake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by PinkD on 2016/8/9.
 * BigDataAdapter
 */
public abstract class BigDataAdapter {
    protected List<String> school;
    protected List<ColorText> colorTexts;

    public BigDataAdapter() {
        school = new ArrayList<>();
        colorTexts = new ArrayList<>();
        Collections.addAll(school, BigData.SCHOOL);
        init();
    }

    /**
     * Override it to init BigData
     */
    protected void init(){}

    /**
     * set cake data
     *
     * @param position School position
     */
    public abstract void setData(Yellowcake yellowcake, MyColorTextAdapter myColorTextAdapter, int position);

    public List<ColorText> getColorTexts() {
        return colorTexts;
    }

    public List<String> getSchool() {
        return school;
    }

}
