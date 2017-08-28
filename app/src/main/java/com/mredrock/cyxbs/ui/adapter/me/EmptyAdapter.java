package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.NoScrollGridView;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.ui.adapter.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by skylineTan on 2016/4/19 10:59.
 */
public class EmptyAdapter extends BaseRecyclerViewAdapter<EmptyRoom,
        EmptyAdapter.ViewHolder> {

    private static final int[] IDS = new int[] {R.drawable.circle_pink, R.drawable.circle_blue, R.drawable.circle_yellow};

    public EmptyAdapter(List<EmptyRoom> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void bindData(ViewHolder holder, EmptyRoom data, int position) {
        if (position == getItemCount() - 1) {
            holder.vLine.setVisibility(View.INVISIBLE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        holder.tvBuilding.setText(data.getFloor());
        holder.gvEmptyRoom.setAdapter(new EmptyGvAdapter(mContext, data
                .getEmptyRooms()));
        holder.mCircle.setImageResource(IDS[position % 3]);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_empty,
                        parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.empty_left_line)
        View vLine;
        @BindView(R.id.empty_left_circle)
        ImageView mCircle;
        @BindView(R.id.item_empty_tv_building)
        TextView tvBuilding;
        @BindView(R.id.item_empty_gv)
        NoScrollGridView gvEmptyRoom;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
