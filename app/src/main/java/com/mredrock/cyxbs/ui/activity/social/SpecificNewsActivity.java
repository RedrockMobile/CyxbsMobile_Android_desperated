package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.CommentContent;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.model.social.OfficeNewsContent;
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
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private DividerItemDecoration mDividerItemDecoration;
    private List<CommentContent> mListComments = null;
    private View mFooterView;
    private boolean isFromMyTrend;
    String article_id;

    private User mUser;

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


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
        StatusBarUtil.setTranslucent(this, 50);
        //  mUser = APP.getUser(this);
        mRefresh.setColorSchemeColors(
                ContextCompat.getColor(APP.getContext(), R.color.colorAccent),
                ContextCompat.getColor(APP.getContext(), R.color.colorPrimary)
        );
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.list_news_item_header, null, false);
        mWrapView = new NewsAdapter.ViewHolder(mHeaderView);

        mWrapView.isFromPersonInfo = getIntent().getBooleanExtra(IS_FROM_PERSON_INFO, false);
        HotNewsContent hotNewsContent = getIntent().getParcelableExtra(START_DATA);

        mHotNewsContent = hotNewsContent;
        article_id = getIntent().getStringExtra(ARTICLE_ID);
        isFromMyTrend = getIntent().getBooleanExtra(IS_FROM_MY_TREND, false);

        if (hotNewsContent != null) {
            mHotNewsContent = hotNewsContent;
            mHotNewsContent.officeNewsContent.content = mHotNewsContent.officeNewsContent.content.replace("\\n", "\n");
            mWrapView.setData(mHotNewsContent, true);
            if (isFromMyTrend) mWrapView.mBtnFavor.setOnClickListener(null);
            mWrapView.mTextContent.setText(mHotNewsContent.officeNewsContent.content);
            if (mHotNewsContent.typeId < BBDDNews.BBDD || (mHotNewsContent.typeId == 6 && mHotNewsContent.user_id == null))
                doWithNews(mWrapView, mHotNewsContent.officeNewsContent);
            requestComments();
        } else {
            getDataBeanById(article_id);
        }
        init();
    }

    private void init() {
        initToolbar();
        mRefresh.setOnRefreshListener(this);
        mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mListComments, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mSpecificNewsCommentAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mWrapView.itemView);

        mNewsEdtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 500) {
                    Utils.toast(SpecificNewsActivity.this, "最长只能输入500字哦～");
                }
            }
        });

        RxBus.getDefault().toObserverable(CommentContent.class)
                .subscribe(commentContent -> {
                    mNewsEdtComment.setText(" ");
                    mNewsEdtComment.setText("回复 " + commentContent.getNickname() + " : ");
                    mNewsEdtComment.clearFocus();
                    mNewsEdtComment.setSelection(mNewsEdtComment.getText().toString().length());
                });

    }

    private void doWithNews(NewsAdapter.ViewHolder mWrapView, OfficeNewsContent bean) {

        mWrapView.mTextContent.setText(Html.fromHtml(mHotNewsContent.officeNewsContent != null ? mHotNewsContent.officeNewsContent.content : ""));
        mWrapView.mTextName.setText(bean.getOfficeName());
        mWrapView.mTextViewEx.setVisibility(View.INVISIBLE);
        mWrapView.mImgAvatar.setImageResource(R.drawable.ic_official_notification);


        if (StringUtils.startsWith(mHotNewsContent.officeNewsContent.content, "<div"))
            mWrapView.mTextContent.setText(mHotNewsContent.officeNewsContent.title);
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
                    }
                });

    }

    private void requestComments() {
        RequestManager.getInstance().getRemarks(new SimpleSubscriber<>(this, new SubscriberListener<List<CommentContent>>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingProgress();
            }

            @Override
            public boolean onError(Throwable e) {
                super.onError(e);
                closeLoadingProgress();
                getDataFailed();
                return false;
            }

            @Override
            public void onNext(List<CommentContent> commentContents) {
                super.onNext(commentContents);
                mListComments = commentContents;
                if ((mListComments == null || mListComments.size() == 0) && mFooterView == null)
                    addFooterView();
                if ((mListComments.size() != 0) && mFooterView != null)
                    removeFooterView();
                mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mListComments, SpecificNewsActivity.this);
                mHeaderViewRecyclerAdapter.setAdapter(mSpecificNewsCommentAdapter);
                closeLoadingProgress();
            }
        }), mHotNewsContent.articleId, mHotNewsContent.typeId);
    }

    private void removeFooterView() {
        mHeaderViewRecyclerAdapter.reMoveFooterView();
        mHeaderViewRecyclerAdapter.notifyDataSetChanged();
        mRecyclerView.removeItemDecoration(mDividerItemDecoration);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
    }

    private void addFooterView() {
        mFooterView = LayoutInflater.from(this)
                .inflate(R.layout.list_footer_item_remark, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterView);
        mRecyclerView.removeItemDecoration(mDividerItemDecoration);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("");
        mToolBarTitle.setText(getString(R.string.specific_news_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void getDataFailed() {
        Toast.makeText(this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_send)
    public void sendComment(View view) {
        if (mNewsEdtComment.getText().toString().equals(""))
            Toast.makeText(SpecificNewsActivity.this, getString(R.string.alter), Toast.LENGTH_SHORT).show();
        else {
            RequestManager.getInstance().postReMarks(new SimpleSubscriber<>(this,true,false, new SubscriberListener<String>() {
                @Override
                public void onCompleted() {
                    super.onCompleted();
                    requestComments();
                    mNewsEdtComment.getText().clear();
                    mRecyclerView.scrollToPosition(1);
                    String msgNumber = Integer.parseInt(mWrapView.mBtnMsg.getText().toString()) + 1 + "";
                    mWrapView.mBtnMsg.setText(msgNumber);
                    mHotNewsContent.remarkNum = msgNumber;
                    RxBus.getDefault().post(mHotNewsContent);
                }

                @Override
                public boolean onError(Throwable e) {
                    super.onError(e);
                    showUploadFail(e.toString());
                    return false;
                }
            }), mHotNewsContent.articleId, mHotNewsContent.typeId, mNewsEdtComment.getText().toString(), mUser.id, mUser.stuNum, mUser.idNum);

        }

    }

    private void getDataBeanById(String articleId) {
        RequestManager.getInstance().getTrendDetail(new SimpleSubscriber<>(this, new SubscriberListener<List<HotNews>>() {
            @Override
            public boolean onError(Throwable e) {
                super.onError(e);
                showUploadFail(e.getMessage());
                return false;
            }

            @Override
            public void onNext(List<HotNews> hotNewses) {
                super.onNext(hotNewses);
                if (hotNewses != null && hotNewses.size() > 0) {
                    // cache old mHotNewsContent for there is no such field in the result of API_TREND_DETAIL
                    HotNewsContent oldHotNewsContent = mHotNewsContent;
                    mHotNewsContent = hotNewses.get(0).data;
                    if (oldHotNewsContent != null) {
                        if (mHotNewsContent.user_id == null || mHotNewsContent.user_id.equals("")) {
                            mHotNewsContent.user_id = oldHotNewsContent.user_id;
                        }
                    }
                    mWrapView.setData(mHotNewsContent, true);
                    if (isFromMyTrend) mWrapView.mBtnFavor.setOnClickListener(null);
                    requestComments();
                }
            }
        }), mHotNewsContent == null ? BBDDNews.BBDD : mHotNewsContent.typeId, articleId);

    }


    @Override
    public void onBackPressed() {
        if (mNewsEdtComment.getText().toString().isEmpty()) {
            super.onBackPressed();
        } else {
            Handler handler = new Handler(getMainLooper());
            handler.post(() -> new MaterialDialog.Builder(this)
                    .title("退出编辑?")
                    .content("是否放弃编辑内容并且退出?")
                    .positiveText("退出")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            finish();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    }).show());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = APP.getUser(this);

        MobclickAgent.onResume(this);
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
    }

    @Override
    @Subscribe(priority = 1, threadMode = ThreadMode.POSTING)
    public void onAskLoginEvent(AskLoginEvent event) {
        super.onAskLoginEvent(event);
        EventBus.getDefault().cancelEventDelivery(event);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        getDataBeanById(article_id);
    }
}
