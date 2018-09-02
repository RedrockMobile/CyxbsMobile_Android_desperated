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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.event.AskLoginEvent;
import com.mredrock.cyxbs.event.LoginStateChangeEvent;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.CommentContent;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.model.social.OfficeNewsContent;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbs.ui.adapter.NewsAdapter;
import com.mredrock.cyxbs.ui.adapter.SpecificNewsCommentAdapter;
import com.mredrock.cyxbs.ui.widget.EditTextBottomSheetDialog;
import com.mredrock.cyxbs.util.RxBus;
import com.mredrock.cyxbs.util.Utils;
import com.mredrock.cyxbs.util.download.DownloadHelper;
import com.mredrock.cyxbs.util.download.callback.OnDownloadListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class SpecificNewsActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, EditTextBottomSheetDialog.OnClickListener
        , View.OnClickListener {


    public static final String START_DATA = "dataBean";
    public static final String ARTICLE_ID = "article_id";
    public static final String IS_FROM_PERSON_INFO = "isFormPersonInfo";
    public static final String IS_FROM_MY_TREND = "isFromMyTrend";

    public static final String TAG = "SpecificNewsActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolBarTitle;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.downText)
    TextView mTextDown;

    @BindView(R.id.comment)
    ViewGroup mComment;
    @BindView(R.id.favor)
    ViewGroup mFavor;
    @BindView(R.id.favor_text)
    TextView mFavorBtn;
    private TextView mMsgNum;

    private NewsAdapter.NewsViewHolder mWrapView;
    private View mHeaderView;
    private HotNewsContent mHotNewsContent;
    private SpecificNewsCommentAdapter mSpecificNewsCommentAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private List<CommentContent> mListComments = null;
    private View mFooterView;
    private boolean isFromMyTrend;
    String article_id;
    private RxPermissions mRxPermissions;

    private EditTextBottomSheetDialog mCommentDialog;

    private User mUser;

    private Disposable mDisposable;

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.comment)
    public void showCommentDialog() {
        if (mCommentDialog != null) {
            mCommentDialog.show();
        }
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
        mRxPermissions = new RxPermissions(this);
        ButterKnife.bind(this);
        //  mUser = BaseAPP.getUser(this);
        mCommentDialog = new EditTextBottomSheetDialog(this);
        mCommentDialog.setOnClickListener(this);

        mRefresh.setColorSchemeColors(
                ContextCompat.getColor(BaseAPP.getContext(), R.color.colorAccent),
                ContextCompat.getColor(BaseAPP.getContext(), R.color.colorPrimary)
        );
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.list_news_item_header, null, false);
        mWrapView = new NewsAdapter.NewsViewHolder(mHeaderView);
        mWrapView.mCardView.setCardElevation(0);
        mWrapView.mCardView.setClickable(false);
        mWrapView.mBtnFavor.setVisibility(View.GONE);
        mWrapView.mBtnMsg.setVisibility(View.GONE);
        mWrapView.mDivider.setVisibility(View.GONE);
        mMsgNum = (TextView) mHeaderView.findViewById(R.id.msg_num);

        mWrapView.isFromPersonInfo = getIntent().getBooleanExtra(IS_FROM_PERSON_INFO, false);
        HotNewsContent hotNewsContent = getIntent().getParcelableExtra(START_DATA);

        mHotNewsContent = hotNewsContent;
        article_id = getIntent().getStringExtra(ARTICLE_ID);
        isFromMyTrend = getIntent().getBooleanExtra(IS_FROM_MY_TREND, false);

        if (hotNewsContent != null) {
            mHotNewsContent = hotNewsContent;
            mHotNewsContent.officeNewsContent.content = mHotNewsContent.officeNewsContent.content.replace("\\n", "\n");
            mWrapView.setData(mHotNewsContent, true);
            mWrapView.mTextContent.setText(mHotNewsContent.officeNewsContent.content);
            if (mHotNewsContent.typeId < BBDDNews.BBDD || (mHotNewsContent.typeId == 6 && mHotNewsContent.user_id == null))
                doWithNews(mWrapView, mHotNewsContent.officeNewsContent);
            mFavorBtn.setText(mHotNewsContent.likeNum);
            mFavor.setOnClickListener(this);
            mMsgNum.setText(mHotNewsContent.remarkNum);
            mFavorBtn.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, hotNewsContent.isMyLike ? R.drawable.ic_favor_blue_comment : R.drawable.ic_favor_blue_white), null, null, null);
            requestComments();
        } else {
            getDataBeanById(article_id);
        }
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void init() {
        initToolbar();
        mRefresh.setOnRefreshListener(this);
        mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mListComments, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mSpecificNewsCommentAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mWrapView.itemView);
//        mSendText.addTextView(mNewsEdtComment);

        mDisposable = RxBus.getDefault().toFlowable(CommentContent.class)
                .subscribe(commentContent -> {
                    mCommentDialog.setText("回复 " + commentContent.getNickname() + " : ");
                    mCommentDialog.show();
                });

    }

    private void doWithNews(NewsAdapter.NewsViewHolder mWrapView, OfficeNewsContent bean) {

        mWrapView.mTextContent.setText(Html.fromHtml(mHotNewsContent.officeNewsContent != null ? mHotNewsContent.officeNewsContent.content : ""));
        mWrapView.mTextName.setText(bean.getOfficeName());
        mWrapView.mTextViewEx.setVisibility(View.INVISIBLE);
        mWrapView.mImgAvatar.setImageResource(R.drawable.ic_official_notification);


        /*if (StringUtils.startsWith(mHotNewsContent.officeNewsContent.content, "<div"))
            mWrapView.mTextContent.setText(mHotNewsContent.officeNewsContent.title);*/
        if (bean.address != null && !bean.address.equals("")) {
            mTextDown.setVisibility(View.VISIBLE);
            String[] address = bean.address.split("\\|");
            String[] names = bean.name.split("\\|");
            mTextDown.setOnClickListener(view -> showDownListDialog(address, names));
        }
    }

    public void showDownListDialog(String[] address, String[] names) {

        DownloadHelper downloadHelper = new DownloadHelper(this, true);
        downloadHelper.prepare(mRxPermissions, Arrays.asList(names), Arrays.asList(address),
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
        RequestManager.getInstance().getRemarks(new SimpleObserver<>(this, new SubscriberListener<List<CommentContent>>() {
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
    }

    private void addFooterView() {
        mFooterView = LayoutInflater.from(this)
                .inflate(R.layout.list_footer_item_remark, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterView);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitle("");
        mToolBarTitle.setText(getString(R.string.specific_news_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void getDataFailed() {
        Toast.makeText(this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
    }

    private void getDataBeanById(String articleId) {
        RequestManager.getInstance().getTrendDetail(new SimpleObserver<>(this, new SubscriberListener<List<HotNews>>() {
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
                    requestComments();
                }
            }
        }), mHotNewsContent == null ? BBDDNews.BBDD : mHotNewsContent.typeId, articleId);

    }

    /*
        @Override
        public void onBackPressed() {
            if (mCommentDialog.getText().toString().isEmpty()) {
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
    */
    @Override
    protected void onResume() {
        super.onResume();
        mUser = BaseAPP.getUser(this);

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

    @Override
    public void onCancel() {

    }

    @Override
    public void onSend(EditText editText) {
        if (editText.getText().toString().equals(""))
            Toast.makeText(SpecificNewsActivity.this, getString(R.string.alter), Toast.LENGTH_SHORT).show();
        else {
            RequestManager.getInstance().postReMarks(new SimpleObserver<>(this, true, false, new SubscriberListener<Unit>() {
                @Override
                public void onComplete() {
                    super.onComplete();
                    requestComments();
                    editText.getText().clear();
                    mRecyclerView.scrollToPosition(1);
                    String msgNumber = Integer.parseInt(mMsgNum.getText().toString()) + 1 + "";
                    mMsgNum.setText(msgNumber);
                    mHotNewsContent.remarkNum = msgNumber;
                    RxBus.getDefault().post(mHotNewsContent);
                    mCommentDialog.dismiss();
                }

                @Override
                public boolean onError(Throwable e) {
                    super.onError(e);
                    showUploadFail(e.toString());
                    return false;
                }
            }), mHotNewsContent.articleId, mHotNewsContent.typeId, editText.getText().toString(), mUser.id, mUser.stuNum, mUser.idNum);

        }
    }

    @Override
    public void onClick(View v) {
        if (mHotNewsContent.isMyLike) {
            mWrapView.dislike(mFavorBtn);
        } else {
            mWrapView.like(mFavorBtn);
        }
    }
}
