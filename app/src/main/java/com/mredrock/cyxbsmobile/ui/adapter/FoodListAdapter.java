package com.mredrock.cyxbsmobile.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.Food;
import com.mredrock.cyxbsmobile.util.GlideHelper;
import com.mredrock.cyxbsmobile.util.LogUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FoodListAdapter extends BaseRecyclerViewAdapter<Food, FoodListAdapter.RestaurantsViewHolder> {

    private static final String TAG = LogUtils.makeLogTag(FoodListAdapter.class);

    private GlideHelper mGlideHelper;

    public FoodListAdapter(Context mContext, List<Food> datas) {
        super(datas, mContext);

        mGlideHelper = new GlideHelper(mContext);
    }

    @Override
    protected void bindData(RestaurantsViewHolder holder, Food data, int position) {
        holder.title.setText(data.name);
        holder.introduction.setText(data.introduction);
        holder.location.setText(data.shop_address);
        mGlideHelper.loadImage(data.shopimg_src, holder.shopImage);
    }

    @Override
    public RestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_surrounding_food, parent, false);
        return new RestaurantsViewHolder(view);
    }

    public static class RestaurantsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.restaurant_name)
        TextView title;
        @Bind(R.id.restaurant_introduction)
        TextView introduction;
        @Bind(R.id.restaurant_location)
        TextView location;
        @Bind(R.id.restaurant_photo)
        ImageView shopImage;

        public RestaurantsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
