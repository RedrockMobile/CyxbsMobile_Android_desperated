package com.mredrock.cyxbs.ui.adapter.me;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;


import java.util.List;

/**
 * Created by glossimarsun on 2017/10/2.
 */

public class VolunteerRecyclerChildAdapter extends RecyclerView.Adapter<VolunteerRecyclerChildAdapter.ViewHolder> {
    private List<VolunteerTime.DataBean.RecordBean> recordBeanList;

    public VolunteerRecyclerChildAdapter(List<VolunteerTime.DataBean.RecordBean> recordBeanList) {
        this.recordBeanList = recordBeanList;
    }

    static  class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView hourText;
        TextView activityText;
        TextView addressText;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            dateText = (TextView) itemView.findViewById(R.id.volunteer_time_day);
            hourText = (TextView) itemView.findViewById(R.id.volunteer_time_hour);
            activityText = (TextView) itemView.findViewById(R.id.volunteer_time_content);
            addressText = (TextView) itemView.findViewById(R.id.volunteer_time_address);
        }
    }
    @Override
    public VolunteerRecyclerChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer_child_month, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(VolunteerRecyclerChildAdapter.ViewHolder holder, int position) {
        VolunteerTime.DataBean.RecordBean record = recordBeanList.get(position);
        holder.dateText.setText(record.getStart_time().substring(5));
        holder.hourText.setText(record.getHours() + " 小时");
        holder.activityText.setText(record.getTitle());
        holder.addressText.setText(record.getAddress());
    }

    @Override
    public int getItemCount() {
        return recordBeanList.size();
    }
}
