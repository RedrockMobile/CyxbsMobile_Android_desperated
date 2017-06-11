package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.social.TopicArticle;
import com.mredrock.cyxbs.ui.adapter.topic.TopicHeaderAdapter;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simonla on 2017/4/6.
 * 10:23
 */

public class TopicArticleHeader implements RecyclerArrayAdapter.ItemView {

    public static final String TAG = TopicArticleHeader.class.getSimpleName();

    @Bind(R.id.iv_topic_article_bg)
    ImageView mIvTopicArticleBg;
    @Bind(R.id.tv_topic_join_number)
    TextView mTvTopicJoinNumber;
    @Bind(R.id.tv_topic_article_header_title)
    TextView mTvTopicArticleTitle;
    @Bind(R.id.expandable_text)
    TextView mExpandableText;
    @Bind(R.id.expand_collapse)
    ImageButton mExpandCollapse;
    @Bind(R.id.etv_topic_content)
    ExpandableTextView mEtvTopicContent;
    private TopicArticle mTopicArticle;
    private Context mContext;

    void setTopicArticle(TopicArticle topicArticle) {
        mTopicArticle = topicArticle;
        setData();
    }

    private void setData() {
        Log.d(TAG, "setData: " + mTopicArticle.getJoin_num());
        mTvTopicJoinNumber.setText(String.valueOf(mTopicArticle.getJoin_num()));
        mIvTopicArticleBg.getVisibility();
        Log.d(TAG, "onCreateView: " + mTopicArticle.getPhoto_src());
        if (mTopicArticle.getPhoto_src() == null || mTopicArticle.getPhoto_src().equals("")) {
            mIvTopicArticleBg.setBackgroundColor(TopicHeaderAdapter.loadByRandom(mContext));
        } else {
            String fixedPhotoUrl = fixFuckWebBug(mTopicArticle.getThumbnail_src());
            Glide.with(mContext).load(fixedPhotoUrl)
                    .error(new ColorDrawable(TopicHeaderAdapter.loadByRandom(mContext)))
                    .into(mIvTopicArticleBg);

        }
        mTvTopicArticleTitle.setText("#" + mTopicArticle.getKeyword() + "#");
        mEtvTopicContent.setText(mTopicArticle.getContent());
    }

    private String fixFuckWebBug(String thumbnail_src) {
        String url = "http://hongyan.cqupt.edu.cn/cyxbsMobile/Public/photo/thumbnail/";
        String[] photoUrl = thumbnail_src.split(",");
        if (!photoUrl[0].startsWith("http://")) {
            return url + photoUrl[0];
        } else {
            return photoUrl[0];
        }
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_topic_article, parent, false);
        ButterKnife.bind(this, view);
        mContext = parent.getContext();
        return view;
    }

    @Override
    public void onBindView(View headerView) {}
}

