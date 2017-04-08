package com.mredrock.cyxbs.ui.adapter.topic;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ExpandableTextView;
import com.mredrock.cyxbs.component.widget.ninelayout.AutoNineGridlayout;
import com.mredrock.cyxbs.model.social.TopicArticle;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simonla on 2017/4/6.
 * 09:39
 */

public class TopicArticleAdapter extends RecyclerArrayAdapter<TopicArticle.ArticlesBean> {
    public TopicArticleAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false));
    }

    public static class TopicArticleViewHolder extends BaseViewHolder<TopicArticle.ArticlesBean> {

        @Bind(R.id.list_news_img_avatar)
        CircularImageView mListNewsImgAvatar;
        @Bind(R.id.list_news_text_nickname)
        TextView mListNewsTextNickname;
        @Bind(R.id.list_news_text_time)
        TextView mListNewsTextTime;
        @Bind(R.id.textView_ex)
        TextView mTextViewEx;
        @Bind(R.id.expandable_text)
        TextView mExpandableText;
        @Bind(R.id.expand_collapse)
        TextView mExpandCollapse;
        @Bind(R.id.expand_text_view)
        ExpandableTextView mExpandTextView;
        @Bind(R.id.autoNineLayout)
        AutoNineGridlayout mAutoNineLayout;
        @Bind(R.id.singleImg)
        ImageView mSingleImg;
        @Bind(R.id.list_news_btn_message)
        TextView mListNewsBtnMessage;
        @Bind(R.id.list_news_btn_favorites)
        TextView mListNewsBtnFavorites;
        @Bind(R.id.news_item_card_view)
        CardView mNewsItemCardView;

        public TopicArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void setData(TopicArticle.ArticlesBean data) {
            super.setData(data);
            mExpandTextView.setText(data.getContent());
            Glide.with(getContext()).load(data.getUser_thumbnail_src()).into(mListNewsImgAvatar);
        }
    }
}
