package com.mredrock.cyxbs.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mredrock.cyxbs.R;

import java.util.List;

/**
 * Created by wusui on 2017/1/20.
 */

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.ViewHolder> {

    public LostAdapter(){

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LostAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_lost_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
