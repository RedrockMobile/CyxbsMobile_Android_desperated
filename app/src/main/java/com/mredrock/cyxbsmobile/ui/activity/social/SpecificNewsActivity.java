package com.mredrock.cyxbsmobile.ui.activity.social;

import android.os.Bundle;
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

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbsmobile.model.social.Comment;
import com.mredrock.cyxbsmobile.model.social.ContentBean;
import com.mredrock.cyxbsmobile.model.social.News;
import com.mredrock.cyxbsmobile.model.social.OkResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;

import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.NewsAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.SpecificNewsCommentAdapter;
import com.mredrock.cyxbsmobile.util.Util;
import com.mredrock.cyxbsmobile.util.download.DownloadHelper;
import com.mredrock.cyxbsmobile.util.download.callback.OnDownloadListener;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpecificNewsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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


    private News.DataBean dataBean;
    private View mHeaderView;
    private NewsAdapter.ViewHolder mWrapView;
    private SpecificNewsCommentAdapter mSpecificNewsCommentAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private List<Comment.ReMark> mDatas = null;

    private View mFooterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_news);
        ButterKnife.bind(this);
        mRefresh.setColorSchemeColors(R.color.colorAccent);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.list_news_item_header, null, false);
        mWrapView = new NewsAdapter.ViewHolder(mHeaderView);
        dataBean = getIntent().getParcelableExtra("dataBean");
        init();
    }

    private void init() {
        initToolbar();
        mRefresh.setOnRefreshListener(this);
        mSendText.setOnClickListener(this);
        mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mDatas, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mSpecificNewsCommentAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mWrapView.itemView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        mWrapView.setData(dataBean, true);
        if (dataBean.content.articletype_id != null)
            doWithNews(mWrapView, dataBean.content);

        reqestComentDatas();
    }

    private void doWithNews(NewsAdapter.ViewHolder mWrapView, ContentBean bean) {
        mWrapView.mTextContent.setText(Html.fromHtml(dataBean.content != null ? dataBean.content.content : ""));
        mWrapView.mTextName.setText(!bean.unit.equals("") ? bean.unit : "教务在线");
        mWrapView.mTextView_ex.setVisibility(View.INVISIBLE);
        if (dataBean.content.content.charAt(0) == '<')
            mWrapView.mTextContent.setText(dataBean.content.title);
        if (!bean.address.equals("")) {
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
                        Util.toast(SpecificNewsActivity.this, getResources().getString(R.string.load_failed));
                        if (message != null) {
                            Log.d("====>>>", "error is " + message);
                        }
                    }
                });

    }


    private void reqestComentDatas() {
        RequestManager.getInstance()
                .getRemarks(dataBean.id, dataBean.type_id)
                .doOnSubscribe(() -> showLoadingProgress())
                .subscribe(reMarks -> {
                    mDatas = reMarks.data;

                    if ((mDatas == null || mDatas.size() == 0) && mFooterView == null)
                        addFooterView();
                    if ((mDatas.size() != 0) && mFooterView != null)
                        removeFooterView();
                    mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mDatas, SpecificNewsActivity.this);
                    mHeaderViewRecyclerAdapter.setAdapter(mSpecificNewsCommentAdapter);
                    closeLoadingProgress();
                }, throwable -> {
                    closeLoadingProgress();
                    getDataFailed();
                });

    }

    private void removeFooterView() {
        mHeaderViewRecyclerAdapter.reMoveFooterView();
        mHeaderViewRecyclerAdapter.notifyDataSetChanged();
    }

    private void addFooterView() {
        mFooterView = LayoutInflater.from(this).inflate(R.layout.list_footer_item_remark, mRecyclerView, false);
        mHeaderViewRecyclerAdapter.addFooterView(mFooterView);
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitle("");
        mToolBarTitle.setText(getString(R.string.specific_news_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> SpecificNewsActivity.this.finish());
    }

    private void getDataFailed() {
        Toast.makeText(this, getString(R.string.erro), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                sendReMark();
                break;
        }
    }

    private void sendReMark() {
        if (mNewsEdtComment.getText().toString().equals(""))
            Toast.makeText(SpecificNewsActivity.this, getString(R.string.alter), Toast.LENGTH_SHORT).show();
        else
            RequestManager.getInstance()
                    .postReMarks(dataBean.id, dataBean.type_id, mNewsEdtComment.getText().toString())
                    .doOnSubscribe(() -> showLoadingProgress())
                    .subscribe(new SimpleSubscriber<>(this, new SubscriberListener<OkResponse>() {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }
                    }));
                   /* .subscribe(okResponse -> {
                        if (okResponse.state == OkResponse.RESPONSE_OK) {
                            reqestComentDatas();
                            mNewsEdtComment.getText().clear();
                        }
                    }, throwable -> showUploadFail(throwable.toString()));*/

    }

    @Override
    public void onRefresh() {
        reqestComentDatas();
    }

    private void showLoadingProgress() {
        mRefresh.setRefreshing(true);
    }

    private void closeLoadingProgress() {
        mRefresh.setRefreshing(false);
    }

    private void showUploadFail(String reason) {
        Log.d("===========>>>", "showUploadFail:" + reason);
    }

    private void showUploadSucess() {
        Log.d("===========>>>", "showUploadSucess");
    }


}
