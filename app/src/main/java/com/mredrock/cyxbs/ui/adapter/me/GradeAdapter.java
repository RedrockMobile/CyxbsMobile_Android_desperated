package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.Grade;
import com.mredrock.cyxbs.ui.adapter.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
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
        holder.mTvCourse.setText(data.course);
        holder.mTvProperty.setText(data.property.equals("理论") ? "理论" : "实践");
        holder.mTvGrade.setText(data.grade);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_grade, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_grade_tv_course)
        TextView mTvCourse;
        @BindView(R.id.item_grade_tv_property)
        TextView mTvProperty;
        @BindView(R.id.item_grade_tv_score)
        TextView mTvGrade;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
