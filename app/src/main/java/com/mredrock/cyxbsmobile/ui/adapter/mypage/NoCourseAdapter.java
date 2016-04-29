package com.mredrock.cyxbsmobile.ui.adapter.mypage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/12 23:31.
 */
public class NoCourseAdapter extends RecyclerView.Adapter<NoCourseAdapter.ViewHolder> {

    private List<String> mNameList;
    private OnItemButtonClickListener mListener;
    private boolean flag = false;


    public NoCourseAdapter(List<String> nameList) {
        mNameList = nameList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_no_course_user, parent, false));
    }


    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.noCourseName.setText(mNameList.get((mNameList.size() - 1) - position));
        if(flag){
            holder.noCourseDelete.setVisibility(View.VISIBLE);
        }
        holder.noCourseDelete.setOnClickListener(view -> {
            mNameList.remove((mNameList.size() - 1) - position);
            notifyDataSetChanged();
            if(mListener != null){
                mListener.onClickEnd(position);
            }
        });
    }


    @Override public int getItemCount() {
        return mNameList.size();
    }


    public void setOnItemButtonClickListener(OnItemButtonClickListener listener) {
        mListener = listener;
    }


    public void setButtonVisible() {
        flag = true;
        notifyDataSetChanged();
    }


    public void setButtonInVisible() {
        flag = false;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.no_course_name) TextView noCourseName;
        @Bind(R.id.no_course_delete) ImageView noCourseDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemButtonClickListener {
        void onClickEnd(int position);
    }
}
