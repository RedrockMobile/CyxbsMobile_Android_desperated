package com.mredrock.cyxbsmobile.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;

import java.util.List;

/**
 * Created by skylineTan on 2016/4/19 14:32.
 */
public class BuildingAdapter extends BaseAdapter {

    private List<String> mDatas;

    public BuildingAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }


    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_fab_sp_item, parent, false);
        }
        TextView tvSelector = (TextView) convertView.findViewById(R.id.item_empty_tv_selector);
        tvSelector.setText(mDatas.get(position));

        return convertView;
    }

}
