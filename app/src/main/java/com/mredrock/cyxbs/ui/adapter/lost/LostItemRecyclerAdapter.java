package com.mredrock.cyxbs.ui.adapter.lost;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mredrock.cyxbs.R;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LostItemRecyclerAdapter extends RecyclerView.Adapter<LostItemRecyclerAdapter.LostItemViewHolder> {

    @Override
    public LostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LostItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(LostItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class LostItemViewHolder extends RecyclerView.ViewHolder {

        public LostItemViewHolder(ViewGroup itemView) {
            super(itemView);
            LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_lost, itemView, false);
        }

    }
}
