package com.excitingboat.freshmanspecial.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.model.bean.Teacher;
import com.excitingboat.freshmanspecial.utils.RoundRectImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by PinkD on 2016/8/10.
 * StudentRecyclerAdapter
 */
public class TeacherRecyclerAdapter extends RecyclerView.Adapter<TeacherRecyclerAdapter.ViewHolder> {
    private List<Teacher> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public TeacherRecyclerAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void add(Teacher theExcellent) {
        data.add(theExcellent);
        notifyDataSetChanged();
    }

    public void addAll(Collection<Teacher> theExcellent) {
        data.addAll(theExcellent);
        notifyDataSetChanged();
    }

    public List<Teacher> getData() {
        return data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.project_freshman_special__item_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(data.get(position).getPhoto().get(0).getPhoto_thumbnail_src())
                .into(holder.roundRectImageView);
        holder.name.setText(data.get(position).getName());
        holder.college.setText(data.get(position).getCollege());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundRectImageView roundRectImageView;
        private TextView name;
        private TextView college;

        public ViewHolder(final View itemView) {
            super(itemView);
            roundRectImageView = (RoundRectImageView) itemView.findViewById(R.id.item_picture);
            name = (TextView) itemView.findViewById(R.id.item_name);
            college = (TextView) itemView.findViewById(R.id.item_school);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
}
