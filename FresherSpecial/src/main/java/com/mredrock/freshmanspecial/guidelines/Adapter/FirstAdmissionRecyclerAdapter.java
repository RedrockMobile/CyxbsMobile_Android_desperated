package com.mredrock.freshmanspecial.guidelines.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.freshmanspecial.beans.GuidelinesAdmissionFirBean;
import com.mredrock.freshmanspecial.R;

import java.util.List;

/**
 * Created by glossimar on 2017/8/10.
 * 专题 ： 邮子攻略
 * 界面 ： 邮子攻略-recyclerview  入学须知第一栏标题
 */

public class FirstAdmissionRecyclerAdapter extends
        RecyclerView.Adapter<FirstAdmissionRecyclerAdapter.ViewHolder> {
    private List<GuidelinesAdmissionFirBean> guidelinesAdmissionFirBeanList;
    private Context context;


    public FirstAdmissionRecyclerAdapter(List<GuidelinesAdmissionFirBean> guidelinesAdmissionFirBeanList
    , Context context) {
        this.guidelinesAdmissionFirBeanList = guidelinesAdmissionFirBeanList;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView firstTitle;
        RecyclerView recyclerView;


        public ViewHolder(View itemView) {
            super(itemView);
            firstTitle = (TextView) itemView.findViewById(R.id.admission_recycleview_first_title);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.admission_recycleview_second_recycler);
        }
    }
    @Override
    public FirstAdmissionRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.special_2017_admission_recycleview, parent, false);
        FirstAdmissionRecyclerAdapter.ViewHolder holder = new FirstAdmissionRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FirstAdmissionRecyclerAdapter.ViewHolder holder, int position) {
        GuidelinesAdmissionFirBean bean = guidelinesAdmissionFirBeanList.get(position);
        holder.firstTitle.setText(bean.getFirstTitle());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(new SecondAdmissionRecyclerAdapter(
                bean.getGuidelinesAdmissionSecBeanList(),context));
    }

    @Override
    public int getItemCount() {
        return guidelinesAdmissionFirBeanList.size();
    }
}
