package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Grade;
import com.mredrock.cyxbs.ui.adapter.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by skylineTan on 2016/4/21 19:16.
 */
public class GradeAdapter extends BaseRecyclerViewAdapter<Grade, GradeAdapter
        .ViewHolder> {

    private String drawer_primary_text = "#DE000000";
    private String darker_grey = "#1AFFFFFF";
    private List<Grade> mGradeList;

    public GradeAdapter(List<Grade> mDatas, Context context) {
        super(mDatas, context);
        mGradeList = mDatas;
    }


    @Override
    protected void bindData(ViewHolder holder, Grade data, int position) {
        setItemBackgroundColor(holder.itemView, position);
        holder.mTvCourse.setText(data.course);
        trySetGradeTitleColor(holder, position);
        holder.mTvProperty.setText(data.property);
        holder.mTvGrade.setText(data.grade);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_grade, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 为每个Item设置不同的背景色.
     */
    private void setItemBackgroundColor(View view, int position) {
        if (position % 2 != 0) {
            view.setBackgroundColor(Color.parseColor(darker_grey));
        } else {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    private void trySetGradeTitleColor(ViewHolder holder, int position) {
        int titleColor = Color.parseColor(drawer_primary_text);
        if (position == 0) {
            titleColor = mContext.getResources().getColor(R.color.black_lightly);
        }
        holder.mTvCourse.setTextColor(titleColor);
        holder.mTvProperty.setTextColor(titleColor);
        holder.mTvGrade.setTextColor(titleColor);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_grade_tv_course)
        TextView mTvCourse;
        @Bind(R.id.item_grade_tv_property)
        TextView mTvProperty;
        @Bind(R.id.item_grade_tv_score)
        TextView mTvGrade;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
