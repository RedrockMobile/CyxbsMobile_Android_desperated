package com.mredrock.cyxbs.freshman.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.ui.activity.campus.CampusStrategyEntranceActivity;
import com.mredrock.cyxbs.freshman.utils.DensityUtils;

import java.util.List;

public class EntranceAdapter extends RecyclerView.Adapter<EntranceAdapter.EntranceViewHolder> {

    private List<CampusStrategyEntranceActivity.SimpleData> simpleDataList;
    private ChangePageListener mListener;

    public EntranceAdapter(List<CampusStrategyEntranceActivity.SimpleData> data, ChangePageListener listener) {
        this.simpleDataList = data;
        this.mListener = listener;
    }

    @Override
    public EntranceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(App.getContext()).inflate(R.layout.freshman_recycle_item_entrance, parent, false);
        return new EntranceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntranceViewHolder holder, int position) {
        ViewGroup.LayoutParams p = holder.itemView.getLayoutParams();
        p.height = DensityUtils.getScreenHeight(App.getContext()) / 5;
        holder.itemView.setLayoutParams(p);
        holder.loadData(simpleDataList.get(position));

    }

    @Override
    public int getItemCount() {
        return simpleDataList.size();
    }

    class EntranceViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;

        EntranceViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.iv_grid_entrance);

            itemView.setOnClickListener(v ->
                    mListener.changePage(simpleDataList.get(getLayoutPosition()).getName())
            );
        }

        private void loadData(CampusStrategyEntranceActivity.SimpleData data) {
            icon.setImageDrawable(App.getContext().getResources().getDrawable(data.getId()));
        }
    }

    public interface ChangePageListener {
        void changePage(String name);
    }
}
