package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.VolunteerTime;

import java.util.List;
import java.util.TreeMap;


/**
 * Created by glossimar on 2017/10/2.
 */

public class VolunteerRecyclerAdapter extends RecyclerView.Adapter<VolunteerRecyclerAdapter.ViewHolder> {
    private List<VolunteerTime.DataBean.RecordBean> recordBeanList;
    private List<String> yearList;
    private VolunteerRecyclerChildAdapter adapter;
    private Context context;
    private List<List<VolunteerTime.DataBean.RecordBean>> allList;

    public VolunteerRecyclerAdapter(List<VolunteerTime.DataBean.RecordBean> recordBeanList, Context context
            , List<String> yearList, List<List<VolunteerTime.DataBean.RecordBean>> allList){
        this.recordBeanList = recordBeanList;
        this.yearList = yearList;
        this.context = context;
        this.allList = allList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView yearText;
        RecyclerView monthRecycler;
        TextView divider;

        public ViewHolder(View itemView) {
            super(itemView);
            yearText = (TextView) itemView.findViewById(R.id.volunteer_time_year);
            monthRecycler = (RecyclerView) itemView.findViewById(R.id.volunteer_time_child_recycler);
            divider = (TextView) itemView.findViewById(R.id.volunteer_time_divider_line);
        }
    }
    @Override
    public VolunteerRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer_child_year, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(VolunteerRecyclerAdapter.ViewHolder holder, int position) {
//        if (yearList.get(yearList.size() - 1).equals("全部"))
        holder.yearText.setText(yearList.get(position));
        if (allList == null) {
            adapter = new VolunteerRecyclerChildAdapter(recordBeanList);
        } else {
            adapter = new VolunteerRecyclerChildAdapter(allList.get(position));
        }

        holder.monthRecycler.setAdapter(adapter);
        holder.monthRecycler.setLayoutManager(new LinearLayoutManager(context));

        if (position == yearList.size()-1){

            holder.divider.setVisibility(View.VISIBLE);


        }
    }

    @Override
    public int getItemCount() {
        return yearList.size();
    }
}
