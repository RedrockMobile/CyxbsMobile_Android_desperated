package com.excitingboat.freshmanspecial.view.adapter;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.excitingboat.freshmanspecial.R;
import com.excitingboat.freshmanspecial.model.bean.SurroundSight;
import com.excitingboat.freshmanspecial.utils.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xushuzhan on 2016/8/15.
 */
public class AroundViewAdapter extends RecyclerView.Adapter<AroundViewAdapter.MyViewHolder> {
    private static final String TAG = "AroundViewAdapter";
    private List<SurroundSight> data;
    private Fragment context;
    private Dialog dialog;
    private ImageView dialogPicture;
    public boolean isShow = false;

    public AroundViewAdapter(Fragment context) {
        this.context = context;
        this.data = new ArrayList<>();
    }


    public void addAll(List data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void add(SurroundSight data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_freshman_special__item_fg_around_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        if(position==3||position==6){
            holder.more_a.setVisibility(View.INVISIBLE);
        }
        holder.Introduction.setText(data.get(position).getIntroduction());
        holder.Address.setText(data.get(position).getTourroute());
        holder.Title.setText(data.get(position).getName());

        Glide.with(context)
                .load(data.get(position).getPhoto().get(0).getPhoto_src())
                .into(holder.picture_av);
        holder.picture_av.setOnClickListener(new View.OnClickListener() {
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
        TextView Introduction;
        TextView Address;
        ImageButton more_i;
        ImageButton more_a;
        ImageView picture_av;

        public MyViewHolder(View itemView) {
            super(itemView);
            picture_av = (ImageView) itemView.findViewById(R.id.iv_fg_av_small_pic);
            Title = (TextView) itemView.findViewById(R.id.tv_fg_av_title);
            Introduction = (TextView) itemView.findViewById(R.id.tv_fg_av_content_s);
            Address = (TextView) itemView.findViewById(R.id.tv_fg_av_content_address);
            more_i = (ImageButton) itemView.findViewById(R.id.ib_fg_av_more_i);
            more_a = (ImageButton) itemView.findViewById(R.id.ib_fg_av_more_a);
            MoreAndLess();

        }


        private void MoreAndLess() {
            //展开收起效果
            more_i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShow) {
                        Introduction.setMaxLines(2);
                        more_i.setBackgroundResource(R.drawable.more);
                        isShow = false;
                    } else {
                        Introduction.setMaxLines(10000);
                        more_i.setBackgroundResource(R.drawable.less);
                        isShow = true;
                    }
                }
            });
            Introduction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShow) {
                        Introduction.setMaxLines(2);
                        more_i.setBackgroundResource(R.drawable.more);
                        isShow = false;
                    } else {
                        Introduction.setMaxLines(10000);
                        more_i.setBackgroundResource(R.drawable.less);
                        isShow = true;
                    }
                }
            });
            more_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShow) {
                        Address.setMaxLines(1);
                        more_a.setBackgroundResource(R.drawable.more);
                        isShow = false;
                    } else {
                        Address.setMaxLines(10000);
                        more_a.setBackgroundResource(R.drawable.less);
                        isShow = true;
                    }
                }
            });
            Address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShow) {
                        Address.setMaxLines(1);
                        more_a.setBackgroundResource(R.drawable.more);
                        isShow = false;
                    } else {
                        Address.setMaxLines(10000);
                        more_a.setBackgroundResource(R.drawable.less);
                        isShow = true;
                    }
                }
            });
        }
    }
}

