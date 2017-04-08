package com.mredrock.cyxbs.ui.adapter.topic;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.social.Topic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonla on 2017/4/5.
 * 19:21
 */

public class TopicHeaderAdapter extends RecyclerArrayAdapter<Topic> {

    public TopicHeaderAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicHeaderViewHolder(parent);
    }

    private class TopicHeaderViewHolder extends BaseViewHolder<Topic> {

        AppCompatImageView mImageView;
        TextView mTextView;

        TopicHeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_topic_bbdd);
            mTextView = $(R.id.tv_hot_topic_title);
            mImageView = $(R.id.iv_hot_topic_bg);
        }

        @Override
        public void setData(Topic data) {
            super.setData(data);
            String keyword = data.getKeyword();
            if (keyword.length() > 7) {
                keyword = keyword.substring(0, 7);
            }
            mTextView.setText("#" + keyword + "#");
            if ("".equals(data.getImg().getImg_src()) || data.getImg().getImg_src() == null) {
                mImageView.setBackgroundColor(loadByRandom(getContext()));
            } else {
                Glide.with(getContext()).load(data.getImg().getImg_small_src()).centerCrop().into(mImageView);
            }
        }
    }
    public static int loadByRandom(Context context) {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(context, R.color.material_color_blue_800));
        colors.add(ContextCompat.getColor(context, R.color.material_color_amber_800));
        colors.add(ContextCompat.getColor(context, R.color.material_color_teal_800));
        colors.add(ContextCompat.getColor(context, R.color.material_color_brown_800));
        colors.add(ContextCompat.getColor(context, R.color.material_color_orange_800));
        Random random = new Random();
        int randomIndex = random.nextInt(colors.size());
        int color = colors.get(randomIndex);
        return color;
    }
}
