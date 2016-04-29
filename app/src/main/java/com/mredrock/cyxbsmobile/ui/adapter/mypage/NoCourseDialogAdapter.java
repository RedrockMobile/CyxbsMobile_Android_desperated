package com.mredrock.cyxbsmobile.ui.adapter.mypage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.ui.adapter.BaseRecyclerViewAdapter;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/14 14:22.
 */
public class NoCourseDialogAdapter
        extends BaseRecyclerViewAdapter<String, NoCourseDialogAdapter.ViewHolder> {


    public NoCourseDialogAdapter(List<String> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void bindData(ViewHolder holder, String data, int position) {
        holder.noCourseTv.setText(data);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_no_course, parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.no_course_tv) TextView noCourseTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
