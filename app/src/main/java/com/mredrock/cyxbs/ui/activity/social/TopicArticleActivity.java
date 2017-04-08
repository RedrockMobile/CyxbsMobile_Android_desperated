package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.TopicArticle;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.topic.TopicArticleAdapter;
import com.mredrock.cyxbs.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicArticleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,RecyclerArrayAdapter.OnMoreListener {

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
    @Bind(R.id.srl_topic)
    SwipeRefreshLayout mSrlTopic;
    TextView mNoMore;

    TopicArticleAdapter mAdapter;
    private int mID;
    private TopicArticleHeader mHeader;

    private int mPage = 0;

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, TopicArticleActivity.class);
        intent.putExtra(TopicArticleActivity.EXTRA_ID, id);
        Log.d(TAG, "start: " + id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_article);
        ButterKnife.bind(this);
        mAdapter= new TopicArticleAdapter(this);
        mSrlTopic.setColorSchemeResources(R.color.colorAccent);
        mSrlTopic.setOnRefreshListener(this);
        mID = getIntent().getIntExtra(EXTRA_ID, 0);
        mRvTopicArticle.setLayoutManager(new LinearLayoutManager(this));
        SpaceDecoration spaceDecoration = new SpaceDecoration((int) Utils.dp2Px(this, 8));
        spaceDecoration.setPaddingEdgeSide(false);
        mRvTopicArticle.addItemDecoration(spaceDecoration);
        mAdapter.setMore(R.layout.item_topic_more, this);
        mHeader= new TopicArticleHeader();
        View v = LayoutInflater.from(this).inflate(R.layout.item_topic_no_more, mRvTopicArticle, false);
        mNoMore= (TextView) v.findViewById(R.id.tv_no_more);
        mAdapter.setNoMore(mNoMore);
        mAdapter.setNoMore(R.layout.item_topic_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                mAdapter.resumeMore();
            }

            @Override
            public void onNoMoreClick() {
                mAdapter.resumeMore();
            }
        });
        mRvTopicArticle.setAdapterWithProgress(mAdapter);
        mAdapter.addHeader(mHeader);
        onRefresh();
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

    @Override
    public void onRefresh() {
        mSrlTopic.setRefreshing(true);
        mPage = 0;
        mAdapter.clear();
        loadArticle();
    }

    private void loadArticle() {
        User user = APP.getUser(this);
        RequestManager.getInstance().getTopicArticle(new SimpleSubscriber<>(this, new SubscriberListener<TopicArticle>() {
            @Override
            public void onCompleted() {
                super.onCompleted();
                mPage++;
                mSrlTopic.setRefreshing(false);
            }

            @Override
            public boolean onError(Throwable e) {
                mSrlTopic.setRefreshing(false);
                return super.onError(e);
            }

            @Override
            public void onNext(TopicArticle topicArticles) {
                super.onNext(topicArticles);
                if (topicArticles.getArticles().size() == 0 && mPage == 0) {
                } else {
                    mAdapter.addAll(topicArticles.getArticles());
                }
                mTvTopicArticleTitle.setText("#" + topicArticles.getKeyword() + "#");
                mHeader.setTopicArticle(topicArticles);
            }
        }), 10, mPage, user.stuNum, user.idNum, mID);
    }

    @Override
    public void onMoreShow() {
        loadArticle();
    }

    @Override
    public void onMoreClick() {

    }
}


