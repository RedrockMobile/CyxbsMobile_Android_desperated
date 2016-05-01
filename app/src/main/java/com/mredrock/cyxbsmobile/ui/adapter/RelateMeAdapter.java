package com.mredrock.cyxbsmobile.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.RelateMe;

import java.util.List;

/**
 * Created by skylineTan on 2016/4/10 16:39.
 */
public class RelateMeAdapter extends BaseRecyclerViewAdapter<RelateMe,
        RelateMeAdapter.ViewHolder> {

    public RelateMeAdapter(List<RelateMe> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void bindData(ViewHolder holder, RelateMe data, int position) {
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_relate_me, parent, false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
