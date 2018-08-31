package com.mredrock.cyxbs.ui.adapter.topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.model.social.OfficeNewsContent;
import com.mredrock.cyxbs.model.social.TopicArticle;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;

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
        return new TopicArticleViewHolderProxy(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false));
    }

    public static class TopicArticleViewHolderProxy extends BaseViewHolder<TopicArticle.ArticlesBean> {

        TopicArticleViewHolder mTopicArticleViewHolder;

        public TopicArticleViewHolderProxy(View itemView) {
            super(itemView);
            mTopicArticleViewHolder = new TopicArticleViewHolder(itemView);
        }

        @Override
        public void setData(com.mredrock.cyxbs.model.social.TopicArticle.ArticlesBean data) {
            super.setData(data);
            mTopicArticleViewHolder.setData(new ArticleAdapter().convert(data), false);
        }

        private class TopicArticleViewHolder extends NewsAdapter.NewsViewHolder {

            public TopicArticleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    private static class ArticleAdapter {
        HotNewsContent convert(TopicArticle.ArticlesBean articlesBean) {
            HotNewsContent hotNewsContent = new HotNewsContent();
            hotNewsContent.userHead = articlesBean.getUser_thumbnail_src();
            hotNewsContent.user_id = String.valueOf(articlesBean.getUser_id());
            hotNewsContent.articleId = String.valueOf(articlesBean.getArticle_id());
            hotNewsContent.likeNum = String.valueOf(articlesBean.getLike_num());
            hotNewsContent.nickName = articlesBean.getNickname();
            hotNewsContent.remarkNum = String.valueOf(articlesBean.getRemark_num());
            hotNewsContent.typeId = BBDDNews.TOPIC_ARTICLE;
            hotNewsContent.time = articlesBean.getCreated_time();
            hotNewsContent.isMyLike = articlesBean.isIs_my_like();
            hotNewsContent.officeNewsContent = new OfficeNewsContent(articlesBean.getContent());
            hotNewsContent.img = new HotNewsContent.ImgBean(articlesBean.getArticle_thumbnail_src(), articlesBean.getArticle_photo_src());
            return hotNewsContent;
        }
    }
}
