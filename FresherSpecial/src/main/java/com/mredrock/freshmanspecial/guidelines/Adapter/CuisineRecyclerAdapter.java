package com.mredrock.freshmanspecial.guidelines.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.beans.CuisineBean;
import com.mredrock.freshmanspecial.units.ScreenUnit;

import java.util.List;

/**
 * Created by glossimar on 2017/8/11.
 */

public class CuisineRecyclerAdapter extends RecyclerView.Adapter<CuisineRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<CuisineBean.CuisineDataBean> list;  // 卡片里的内容单独组成一个类

    public CuisineRecyclerAdapter(List<CuisineBean.CuisineDataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName; //  名称
        TextView commit;  //  具体描述
        TextView address;   //  所在地点
        ImageView mainImage;    //  图片

        public ViewHolder(View itemView) {
            super(itemView);
            shopName = (TextView) itemView.findViewById(R.id.picword_horizontal_item_title);
            commit = (TextView) itemView.findViewById(R.id.picword_horizontal_item_text);
            address = (TextView) itemView.findViewById(R.id.picword_horizontal_item_address);
            mainImage = (ImageView) itemView.findViewById(R.id.picword_horizontal_item_image);

        }
    }

    @Override
    public CuisineRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.special_2017_picword_horizontal_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CuisineBean.CuisineDataBean cuisine = list.get(position);
        holder.address.setText(cuisine.getLocation());
        holder.shopName.setText(cuisine.getName());
        holder.commit.setText(cuisine.getResume());
        Log.d("123", "width:  " + 200);
        Glide.with(context)
                .load(cuisine.getUrl().get(0))
                .centerCrop()
                .crossFade(200)
                .override(ScreenUnit.bulid(context).getPxWide() / 6, ScreenUnit.bulid(context).getPxWide() / 8)
                .into(holder.mainImage);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

