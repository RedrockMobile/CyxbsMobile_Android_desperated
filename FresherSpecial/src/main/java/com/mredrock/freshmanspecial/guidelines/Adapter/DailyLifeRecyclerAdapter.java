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
import com.mredrock.freshmanspecial.beans.DailyLifeBean;
import com.mredrock.freshmanspecial.units.ScreenUnit;

import java.util.List;

/**
 * Created by glossimar on 2017/8/11.
 */

public class DailyLifeRecyclerAdapter extends RecyclerView.Adapter<DailyLifeRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<DailyLifeBean.DailyLifeDataBean> list;  // 卡片里的内容单独组成一个类

    public DailyLifeRecyclerAdapter(List<DailyLifeBean.DailyLifeDataBean> list, Context context) {
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
    public DailyLifeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.special_2017_picword_horizontal_item, parent, false);
        DailyLifeRecyclerAdapter.ViewHolder holder = new DailyLifeRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DailyLifeRecyclerAdapter.ViewHolder holder, int position) {
        DailyLifeBean.DailyLifeDataBean dailyLife = list.get(position);
        holder.address.setText(dailyLife.getLocation());
        holder.shopName.setText(dailyLife.getName());
        holder.commit.setText(dailyLife.getResume());
        Log.d("123", "height:  " + ScreenUnit.bulid(context).getPxWide() / 48 * 9);
        Glide.with(context)
                .load(dailyLife.getUrl().get(0))
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
