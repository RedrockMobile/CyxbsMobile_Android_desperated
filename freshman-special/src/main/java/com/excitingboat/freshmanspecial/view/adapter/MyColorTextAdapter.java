package com.excitingboat.freshmanspecial.view.adapter;

import android.util.Log;

import com.excitingboat.freshmanspecial.R;
import com.excitingboat.yellowcake.ColorTextAdapter;
import com.excitingboat.yellowcake.ColorTextListView;

/**
 * Created by PinkD on 2016/8/8.
 * MyColorTextAdapter
 */
public class MyColorTextAdapter extends ColorTextAdapter {

    @Override
    public void setData(ColorTextListView.ViewHolder viewHolder, int position) {
        viewHolder.getRoundedRectangleView().setColor(colorTexts.get(position).getColor());
        viewHolder.getTextView().setText(colorTexts.get(position).getText());
    }
}
