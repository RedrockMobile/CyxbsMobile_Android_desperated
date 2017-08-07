package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.ItemChangedEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.TopicArticle;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.topic.TopicArticleAdapter;
import com.mredrock.cyxbs.util.Utils;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicArticleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnMoreListener {

    public static final String EXTRA_ID = "topic_article_id";
    public static final String EXTRA_POST_SUCCESS = "post_article_success";
    public static final String SHARE_BASE_URL = "http://hongyan.cqupt.edu.cn/cyxbsMobileTalk/?id=";
    public static final String TAG = TopicArticleActivity.class.getSimpleName();
    public static final int RESULT_CODE = 1001;

    @Bind(R.id.toolbar_title)
    TextView mTvTopicArticleTitle;
    @Bind(R.id.rv_topic_article)
    EasyRecyclerView mRvTopicArticle;
    @Bind(R.id.ll_topic_join)
    LinearLayout mLlTopicJoin;
    @Bind(R.id.srl_topic)
    SwipeRefreshLayout mSrlTopic;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    TopicArticleAdapter mAdapter;
    private int mID;
    private String mTitle;
    private TopicArticleHeader mHeader;
    private List<TopicArticle.ArticlesBean> mArticlesBeen = new ArrayList<>();

    private int mPage = 0;

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
        initToolbar();
        mAdapter = new TopicArticleAdapter(this);
        mSrlTopic.setColorSchemeResources(R.color.colorAccent);
        mSrlTopic.setOnRefreshListener(this);
        mID = getIntent().getIntExtra(EXTRA_ID, 0);
        mRvTopicArticle.setLayoutManager(new LinearLayoutManager(this));
        SpaceDecoration spaceDecoration = new SpaceDecoration((int) Utils.dp2Px(this, 8));
        spaceDecoration.setPaddingEdgeSide(false);
        mRvTopicArticle.addItemDecoration(spaceDecoration);
        mAdapter.setMore(R.layout.item_topic_more, this);
        mHeader = new TopicArticleHeader();
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

    private void initToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_back);
            mToolbar.setNavigationOnClickListener(v -> TopicArticleActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    @OnClick(R.id.ll_topic_join)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*case R.id.bt_topic_article_back:
                finish();
                break;
            case R.id.iv_topic_share:
                break;*/
            case R.id.ll_topic_join:
                Intent intent = new Intent(this, PostNewsActivity.class);
                intent.putExtra(PostNewsActivity.EXTRA_TOPIC_ID, mID);
                intent.putExtra(PostNewsActivity.EXTRA_TOPIC_TITLE, mTitle);
                startActivityForResult(intent, RESULT_CODE);
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
                mLlTopicJoin.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean onError(Throwable e) {
                mSrlTopic.setRefreshing(false);
                return super.onError(e);
            }

            @Override
            public void onNext(TopicArticle topicArticles) {
                super.onNext(topicArticles);
                mArticlesBeen.addAll(topicArticles.getArticles());
                if (topicArticles.getArticles().size() == 0 && mPage == 0) {

                } else {
                    mAdapter.addAll(topicArticles.getArticles());
                }
                mTvTopicArticleTitle.setText("#" + topicArticles.getKeyword() + "#");
                mTitle = topicArticles.getKeyword();
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

    /*@OnClick(R.id.iv_topic_share)*/
    public void onViewClicked() {
        Toast.makeText(this, "开发中...", Toast.LENGTH_SHORT).show();
//        RxPermissions.getInstance(this).request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(granted -> {
//                    if (granted) {
//                        UMWeb web = new UMWeb(SHARE_BASE_URL + mID);
//
//                        new ShareAction(this).withText("快来掌上重邮参与讨论吧")
//                                .withSubject("快来掌上重邮参加讨论吧")
//                                .withMedia(web)
//                                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
//                                .setCallback(new UMShareListener() {
//                                    @Override
//                                    public void onStart(SHARE_MEDIA share_media) {
//                                        Log.d(TAG, "onStart: ");
//                                    }
//
//                                    @Override
//                                    public void onResult(SHARE_MEDIA share_media) {
//                                        Log.d(TAG, "onResult: ");
//                                    }
//
//                                    @Override
//                                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                                        Toast.makeText(TopicArticleActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                    }
//
//                                    @Override
//                                    public void onCancel(SHARE_MEDIA share_media) {
//                                        Log.d(TAG, "onCancel: ");
//                                    }
//                                }).open();
//                    } else {
//                        Toast.makeText(this, "请授予权限", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
    private class ArticleFooter implements RecyclerArrayAdapter.ItemView {

        private View mView;

        public View getView() {
            return mView;
        }

        @Override
        public View onCreateView(ViewGroup parent) {
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic_no_more, parent, false);
            TextView tv = (TextView) mView.findViewById(R.id.tv_no_more);
            tv.setText("还没有人哦，快来参加话题吧~");
            return mView;
        }

        @Override
        public void onBindView(View headerView) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE && data != null) {
            boolean success = data.getBooleanExtra(EXTRA_POST_SUCCESS, false);
            if (success) {
                onRefresh();
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemChangedEvent(ItemChangedEvent event) {
        if (mArticlesBeen == null)
            return;
        int index = -1;
        for (TopicArticle.ArticlesBean article : mArticlesBeen) {
            index++;
            if (String.valueOf(article.getArticle_id()).equals(event.getArticleId())) {
                article.setIs_my_like(event.isMyLike());
                article.setLike_num(Integer.parseInt(event.getNum()));
                mAdapter.notifyItemChanged(index + 1);
            }
        }
    }

}


