package com.mredrock.cyxbsmobile.ui.adapter.mypage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.RelateMe;
import com.mredrock.cyxbsmobile.ui.adapter.BaseRecyclerViewAdapter;
import java.util.List;

/**
 * Created by skylineTan on 2016/4/28 01:08.
 */
public class RelateMeAdapter extends BaseRecyclerViewAdapter<RelateMe,
        RelateMeAdapter.ViewHolder> {

    public RelateMeAdapter(List<RelateMe> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void bindData(ViewHolder holder, RelateMe data, int position) {

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_relate_me,
                                                    parent,false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
