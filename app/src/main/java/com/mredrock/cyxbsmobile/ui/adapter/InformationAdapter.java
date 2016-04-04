package com.mredrock.cyxbsmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mredrock.cyxbsmobile.R;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {


    public InformationAdapter() {
    }

    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_infomation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(InformationAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
