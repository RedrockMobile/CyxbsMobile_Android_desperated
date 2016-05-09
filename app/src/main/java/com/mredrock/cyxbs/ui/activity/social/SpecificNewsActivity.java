package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.CommentContent;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.model.social.OfficeNewsContent;
import com.mredrock.cyxbs.model.social.Stu;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;
import com.mredrock.cyxbs.ui.adapter.SpecificNewsCommentAdapter;
import com.mredrock.cyxbs.util.RxBus;
import com.mredrock.cyxbs.util.Utils;
import com.mredrock.cyxbs.util.download.DownloadHelper;
import com.mredrock.cyxbs.util.download.callback.OnDownloadListener;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpecificNewsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    public static final String START_DATA = "dataBean";
    public static final String ARTICLE_ID = "article_id";
    public static final String IS_FROM_PERSON_INFO = "isFormPersonInfo";
    public static final String IS_FROM_MY_TREND = "isFromMyTrend";

    public static final String TAG = "SpecificNewsActivity";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolBarTitle;
    @Bind(R.id.news_edt_comment)
    EditText mNewsEdtComment;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.btn_send)
    TextView mSendText;
    @Bind(R.id.downText)
    TextView mTextDown;

    private NewsAdapter.ViewHolder mWrapView;
    private View mHeaderView;
    private HotNewsContent mHotNewsContent;
    private SpecificNewsCommentAdapter mSpecificNewsCommentAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private List<CommentContent> mListComments = null;
    private View mFooterView;
    private boolean isFromMyTrend;

    public static void startActivityWithDataBean(Context context, HotNewsContent hotNewsContent, String articleId, boolean isFromPersonInfo, boolean isFromMyTrend) {
        Intent intent = new Intent(context, SpecificNewsActivity.class);
        intent.putExtra(START_DATA, hotNewsContent);
        intent.putExtra(ARTICLE_ID, articleId);
        intent.putExtra(IS_FROM_PERSON_INFO, isFromPersonInfo);
        intent.putExtra(IS_FROM_MY_TREND, isFromMyTrend);
        context.startActivity(intent);
    }

    public static final void startActivityWithArticleId(Context context, String articleId, boolean isFromPersonInfo, boolean isFromMyTrend) {
        Intent intent = new Intent(context, SpecificNewsActivity.class);
        intent.putExtra(ARTICLE_ID, articleId);
        intent.putExtra(IS_FROM_PERSON_INFO, isFromPersonInfo);
        intent.putExtra(IS_FROM_MY_TREND, isFromMyTrend);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_news);
        ButterKnife.bind(this);
        mRefresh.setColorSchemeColors(
                ContextCompat.getColor(APP.getContext(), R.color.colorAccent),
                ContextCompat.getColor(APP.getContext(), R.color.colorPrimary)
        );
        mHeaderView = LayoutInflater.from(this)
                .inflate(R.layout.list_news_item_header, null, false);
        mWrapView = new NewsAdapter.ViewHolder(mHeaderView);

        mWrapView.isFromPersonInfo = getIntent().getBooleanExtra(IS_FROM_PERSON_INFO, false);
        HotNewsContent hotNewsContent = getIntent().getParcelableExtra(START_DATA);
        String article_id = getIntent().getStringExtra(ARTICLE_ID);
        isFromMyTrend = getIntent().getBooleanExtra(IS_FROM_MY_TREND, false);

        if (hotNewsContent != null) {
            mHotNewsContent = hotNewsContent;
            mHotNewsContent.content.content = mHotNewsContent.content.content.replace("\\n", "\n");
            mWrapView.setData(mHotNewsContent, true, hotNewsContent.getType());
            if (isFromMyTrend) mWrapView.mBtnFavor.setOnClickListener(null);
            mWrapView.mTextContent.setText(mHotNewsContent.content.content);
            if (mHotNewsContent.type_id < BBDDNews.BBDD || (mHotNewsContent.type_id == 6 && mHotNewsContent.user_id == null))
                doWithNews(mWrapView, mHotNewsContent.content);
            requestComments();
        } else getDataBeanById(article_id);

        init();
    }

    private void init() {
        initToolbar();
        mRefresh.setOnRefreshListener(this);
        mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mListComments, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mSpecificNewsCommentAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mWrapView.itemView);

        RxBus.getDefault().toObserverable(CommentContent.class)
                .subscribe(commentContent -> {
                    mNewsEdtComment.setText(" ");
                    mNewsEdtComment.setText("回复 " + commentContent.getNickname() + " : ");
                    mNewsEdtComment.clearFocus();
                    mNewsEdtComment.setSelection(mNewsEdtComment.getText().toString().length());
                });

    }

    private void doWithNews(NewsAdapter.ViewHolder mWrapView, OfficeNewsContent bean) {

        mWrapView.mTextContent.setText(Html.fromHtml(mHotNewsContent.content != null ? mHotNewsContent.content.content : ""));
        mWrapView.mTextName.setText(bean.getOfficeName());
        mWrapView.mTextView_ex.setVisibility(View.INVISIBLE);


        if (StringUtils.startsWith(mHotNewsContent.content.content, "<div"))
            mWrapView.mTextContent.setText(mHotNewsContent.content.title);
        if (bean.address != null && !bean.address.equals("")) {
            mTextDown.setVisibility(View.VISIBLE);
            String[] address = bean.address.split("\\|");
            String[] names = bean.name.split("\\|");
            mTextDown.setOnClickListener(view -> showDownListDialog(address, names));
        }
    }

    public void showDownListDialog(String[] address, String[] names) {

        DownloadHelper downloadHelper = new DownloadHelper(this, true);
        downloadHelper.prepare(Arrays.asList(names), Arrays.asList(address),
                new OnDownloadListener() {
                    @Override
                    public void startDownload() {
                    }

                    @Override
                    public void downloadSuccess() {
                        downloadHelper.tryOpenFile();
                    }

                    @Override
                    public void downloadFailed(String message) {
                        Utils.toast(SpecificNewsActivity.this, getResources().getString(R.string.load_failed));
                        if (message != null) {
                            Log.d(TAG, "explore_error is---->>> " + message);
                        }
                    }
                });

    }

    private void requestComments() {
        RequestManager.getInstance()
                .getRemarks(mHotNewsContent.article_id, mHotNewsContent.type_id)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribe(reMarks -> {
                    mListComments = reMarks;
                    if ((mListComments == null || mListComments.size() == 0) && mFooterView == null)
                        addFooterView();
                    if ((mListComments.size() != 0) && mFooterView != null)
                        removeFooterView();
                    mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mListComments, SpecificNewsActivity.this);
                    mHeaderViewRecyclerAdapter.setAdapter(mSpecificNewsCommentAdapter);
                    closeLoadingProgress();
                }, throwable -> {
                    closeLoadingProgress();
                    getDataFailed(throwable.toString());
                });
    }

    private void removeFooterView() {
        mHeaderViewRecyclerAdapter.reMoveFooterView();
        mHeaderViewRecyclerAdapter.notifyDataSetChanged();
    }

    private void addFooterView() {
        mFooterView = LayoutInflater.from(this)
                .inflate(R.layout.list_footer_item_remark, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterView);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitle("");
        mToolBarTitle.setText(getString(R.string.specific_news_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> SpecificNewsActivity.this.finish());
    }

    private void getDataFailed(String reason) {
        Toast.makeText(this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
        Log.e(TAG, reason);
    }

    @OnClick(R.id.btn_send)
    public void sendComment(View view) {
        if (mNewsEdtComment.getText().toString().equals(""))
            Toast.makeText(SpecificNewsActivity.this, getString(R.string.alter), Toast.LENGTH_SHORT)
                    .show();
        else
            RequestManager.getInstance()
                    .postReMarks(mHotNewsContent.id, mHotNewsContent.type_id, mNewsEdtComment.getText()
                            .toString())
                    .doOnSubscribe(() -> showLoadingProgress())
                    .subscribe(new SimpleSubscriber<String>(this, false, false, new SubscriberListener<String>() {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            requestComments();
                            mNewsEdtComment.getText().clear();
                            //mRecyclerView.scrollTo(100, 100);
                            mRecyclerView.scrollToPosition(1);

                            String msgNumber = Integer.parseInt(mWrapView.mBtnMsg.getText().toString()) + 1 + "";
                            mWrapView.mBtnMsg.setText(msgNumber);
                            mHotNewsContent.remark_num = msgNumber;
                            RxBus.getDefault().post(mHotNewsContent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            showUploadFail(e.toString());
                        }
                    }) {
                        @Override
                        public void onError(Throwable e) {
                            this.dismissProgressDialog();
                            if (this.listener != null) {
                                listener.onError(e);
                            }
                        }
                    });

    }

    private void getDataBeanById(String article_id) {
        RequestManager.getInstance().getTrendDetail(Stu.STU_NUM, Stu.ID_NUM,
                mHotNewsContent == null ? BBDDNews.BBDD : mHotNewsContent.type_id,
                article_id)
                .subscribe(newses -> {
                    if (newses != null && newses.size() > 0) {
                        mHotNewsContent = newses.get(0).data;
                        mHotNewsContent.user_head = APP.getUser(SpecificNewsActivity.this).photo_thumbnail_src;
                        mHotNewsContent.nick_name = APP.getUser(SpecificNewsActivity.this).nickname;
                        mWrapView.setData(mHotNewsContent, true, mHotNewsContent.getType());
                        if (isFromMyTrend) mWrapView.mBtnFavor.setOnClickListener(null);
                        requestComments();
                    }
                }, throwable -> {
                    showUploadFail(throwable.getMessage());
                });
    }

    @Override
    public void onRefresh() {
        requestComments();
    }

    private void showLoadingProgress() {
        mRefresh.setRefreshing(true);
    }

    private void closeLoadingProgress() {
        mRefresh.setRefreshing(false);
    }

    private void showUploadFail(String reason) {
        closeLoadingProgress();
        Log.e(TAG, "showUploadFail---->>>" + reason);
    }

    private void showUploadSucess() {
        Log.d(TAG, "showUploadSuccess");
    }


}
