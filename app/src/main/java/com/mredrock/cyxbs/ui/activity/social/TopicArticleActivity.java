package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicArticleActivity extends BaseActivity {

    public static final String EXTRA_ID = "topic_article_id";
    public static final String TAG = TopicArticleActivity.class.getSimpleName();

    @Bind(R.id.tv_topic_article_title)
    TextView mTvTopicArticleTitle;
    @Bind(R.id.iv_topic_share)
    ImageView mIvTopicShare;
    @Bind(R.id.rv_topic_article)
    EasyRecyclerView mRvTopicArticle;
    @Bind(R.id.ll_topic_join)
    LinearLayout mLlTopicJoin;
    @Bind(R.id.bt_topic_article_back)
    AppCompatImageView mBtTopicArticleBack;

    private int mId;

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, TopicArticleActivity.class);
        intent.putExtra(TopicArticleActivity.EXTRA_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_article);
        ButterKnife.bind(this);
        mId = getIntent().getIntExtra(EXTRA_ID, 0);
    }

    @OnClick({R.id.bt_topic_article_back, R.id.iv_topic_share, R.id.ll_topic_join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_topic_article_back:
                finish();
                break;
            case R.id.iv_topic_share:
                break;
            case R.id.ll_topic_join:
                break;
        }
    }
}
