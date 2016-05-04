package com.mredrock.cyxbsmobile.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.Restaurant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AroundDishesAdapter extends BaseRecyclerViewAdapter<Restaurant, AroundDishesAdapter.AroundDishesViewHolder> {

    public AroundDishesAdapter(Context mContext, List<Restaurant> datas) {
        super(datas, mContext);
    }

    @Override
    protected void bindData(AroundDishesViewHolder holder, Restaurant data, int position) {
        holder.title.setText(data.name);
        holder.content.setText(data.content);
        holder.address.setText(data.shop_address);
        Glide.with(mContext)
                .load(data.shopimg_src)
                .crossFade()
                .into(holder.shopImage);
    }

    public void updateItems(List<Restaurant> data) {
        mDatas = data;
        notifyItemRangeChanged(0, mDatas.size());
    }

    @Override
    public AroundDishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_around_dishes, parent, false);
        return new AroundDishesViewHolder(view);
    }

    public static class AroundDishesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_around_dishes_shop_title)
        TextView title;
        @Bind(R.id.item_around_dishes_shop_content)
        TextView content;
        @Bind(R.id.item_around_dishes_shop_address)
        TextView address;
        @Bind(R.id.item_around_dishes_shop_img)
        ImageView shopImage;

        public AroundDishesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
