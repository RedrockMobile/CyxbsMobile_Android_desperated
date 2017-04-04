package com.mredrock.cyxbs.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.social.Topic;
import com.mredrock.cyxbs.util.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonla on 2017/4/2.
 * 11:35
 */

public class TopicAdapter extends RecyclerArrayAdapter<Topic> {

    public TopicAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(parent);
    }

    private class TopicViewHolder extends BaseViewHolder<Topic> {

        TextView mTvTopicTitle;
        TextView mTvTopicJoinNumber;
        RelativeLayout mLlItemTopic;
        ImageView mImageView;

        TopicViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_topic);
            mTvTopicTitle = $(R.id.tv_topic_title);
            mTvTopicJoinNumber = $(R.id.tv_topic_join_number);
            mLlItemTopic = $(R.id.rl_item_topic);
            mImageView = $(R.id.iv_topic_bg);
            mLlItemTopic.setLayoutParams(new ViewGroup
                    .LayoutParams(Utils.getScreenWidth(getContext()) / 2 - (int) Utils.dp2Px(getContext(), 20),
                    (int) Utils.dp2Px(getContext(), 175)));
        }

        @Override
        public void setData(Topic data) {
            super.setData(data);
            mTvTopicJoinNumber.setText(" "+data.getJoin_num() + "人参与");
            String keyword = data.getKeyword();
            if (keyword.length() > 7) {
                keyword = keyword.substring(0, 7);
            }
            mTvTopicTitle.setText("#" + keyword + "#");
            if ("".equals(data.getImg().getImg_src()) || data.getImg().getImg_src() == null) {
                loadByRandom();
            } else {
                Glide.with(getContext()).load(data.getImg().getImg_small_src()).centerCrop().into(mImageView);
            }
        }

        private void loadByRandom() {
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(ContextCompat.getColor(getContext(), R.color.material_color_blue_800));
            colors.add(ContextCompat.getColor(getContext(), R.color.material_color_amber_800));
            colors.add(ContextCompat.getColor(getContext(), R.color.material_color_teal_800));
            colors.add(ContextCompat.getColor(getContext(), R.color.material_color_brown_800));
            colors.add(ContextCompat.getColor(getContext(), R.color.material_color_orange_800));
            Random random = new Random();
            int randomIndex = random.nextInt(colors.size());
            int color = colors.get(randomIndex);
            mImageView.setBackgroundColor(color);
        }
    }
}
