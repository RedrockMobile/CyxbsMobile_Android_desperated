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
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.social.TopicArticle;
import com.mredrock.cyxbs.ui.adapter.topic.TopicHeaderAdapter;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by simonla on 2017/4/6.
 * 10:23
 */

public class TopicArticleHeader implements RecyclerArrayAdapter.ItemView {

    public static final String TAG = TopicArticleHeader.class.getSimpleName();

    @BindView(R.id.iv_topic_article_bg)
    ImageView mIvTopicArticleBg;
    @BindView(R.id.tv_topic_join_number)
    TextView mTvTopicJoinNumber;
    @BindView(R.id.tv_topic_article_header_title)
    TextView mTvTopicArticleTitle;
    @BindView(R.id.expandable_text)
    TextView mExpandableText;
    @BindView(R.id.expand_collapse)
    ImageButton mExpandCollapse;
    @BindView(R.id.etv_topic_content)
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
            String name = mTopicArticle.getPhoto_src();
            name = name.substring(0, name.indexOf(",") <= 0 ? name.length() : name.indexOf(","));
            String src = Const.APP_HOME + "/Public/photo/" + name;
            Glide.with(mContext).load(src)
                    .error(new ColorDrawable(TopicHeaderAdapter.loadByRandom(mContext)))
                    .into(mIvTopicArticleBg);
        }
        mTvTopicArticleTitle.setText("#" + mTopicArticle.getKeyword() + "#");
        mEtvTopicContent.setText(mTopicArticle.getContent());
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_topic_article, parent, false);
        ButterKnife.bind(this, view);
        mContext = parent.getContext();
        return view;
    }

    @Override
    public void onBindView(View headerView) {
    }
}

