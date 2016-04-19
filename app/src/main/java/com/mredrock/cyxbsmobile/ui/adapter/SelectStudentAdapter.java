package com.mredrock.cyxbsmobile.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.Student;
import com.mredrock.cyxbsmobile.ui.activity.mypage.NoCourseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by skylineTan on 2016/4/13 20:00.
 */
public class SelectStudentAdapter extends BaseRecyclerViewAdapter<Student, SelectStudentAdapter.ViewHolder> {

    public SelectStudentAdapter(List<Student> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void bindData(ViewHolder holder, Student data, int position) {
        holder.selectName.setText(data.stunum);
        holder.selectMajor.setText(data.major);
        holder.selectStuNum.setText(data.stunum);
        setOnItemClickListener((parent, view, position1, id) -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(NoCourseActivity.EXTRA_NO_COURSE, data);
            intent.putExtras(bundle);
            ((AppCompatActivity) mContext).setResult(Activity.RESULT_OK, intent);
            ((AppCompatActivity) mContext).finish();
        });
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_student, parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.select_name)
        TextView selectName;
        @Bind(R.id.select_major)
        TextView selectMajor;
        @Bind(R.id.select_stu_num)
        TextView selectStuNum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
