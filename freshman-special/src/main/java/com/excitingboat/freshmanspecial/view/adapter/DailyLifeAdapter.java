package com.excitingboat.freshmanspecial.view.adapter;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.model.bean.Place;
import com.excitingboat.freshmanspecial.utils.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xushuzhan on 2016/8/15.
 */
public class DailyLifeAdapter extends RecyclerView.Adapter<DailyLifeAdapter.MyViewHolder> {
    private List<Place> data;
    private Fragment context;
    private Dialog dialog;
    private ImageView dialogPicture;
    public boolean isShow = false;

    public DailyLifeAdapter(Fragment context) {
        this.context = context;
        data = new ArrayList<>();
    }


    public void addAll(List data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void add(Place data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_freshman_special__item_fg_daily_lify, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.Adress.setText(data.get(position).getAddress());
            holder.Title.setText(data.get(position).getName());

            Glide.with(context)
                    .load(data.get(position).getPhoto().get(0).getPhoto_src())
                    .into(holder.picture_dl);
            holder.picture_dl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog == null) {
                        dialog = new Dialog(context.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.project_freshman_special__dialog_fg);
                        dialogPicture = (RoundImageView) dialog.findViewById(R.id.dialog_picture);

                    }
                    Glide.with(context)
                            .load(data.get(holder.getLayoutPosition()).getPhoto().get(0).getPhoto_src())
                            .into(dialogPicture);

                    dialog.show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView Adress;
        ImageView picture_dl;

        public MyViewHolder(View itemView) {
            super(itemView);
            picture_dl = (ImageView) itemView.findViewById(R.id.iv_fg_dl_small_pic);
            Title = (TextView) itemView.findViewById(R.id.tv_fg_dl_title);
            Adress = (TextView) itemView.findViewById(R.id.tv_fg_dl_content_address);
        }
    }
}

