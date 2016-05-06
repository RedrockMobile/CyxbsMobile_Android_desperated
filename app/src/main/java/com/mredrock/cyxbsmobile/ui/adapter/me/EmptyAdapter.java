package com.mredrock.cyxbsmobile.ui.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.NoScrollGridView;
import com.mredrock.cyxbsmobile.model.EmptyRoom;
import com.mredrock.cyxbsmobile.ui.adapter.BaseRecyclerViewAdapter;

import java.util.List;


/**
 * Created by skylineTan on 2016/4/19 10:59.
 */
public class EmptyAdapter extends BaseRecyclerViewAdapter<EmptyRoom,
        EmptyAdapter.ViewHolder> {


    public EmptyAdapter(List<EmptyRoom> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void bindData(ViewHolder holder, EmptyRoom data, int position) {
        if (position == 0) {
            holder.vLine.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }
        holder.tvBuilding.setText(data.getFloor());
        holder.gvEmptyRoom.setAdapter(new EmptyGvAdapter(mContext, data
                .getEmptyRooms()));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_empty,
                        parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.empty_left_line)
        View vLine;
        @Bind(R.id.item_empty_tv_building)
        TextView tvBuilding;
        @Bind(R.id.item_empty_gv)
        NoScrollGridView gvEmptyRoom;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
