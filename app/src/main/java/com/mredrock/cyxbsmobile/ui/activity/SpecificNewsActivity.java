package com.mredrock.cyxbsmobile.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.AutoNineGridlayout;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbsmobile.model.community.Comment;
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.SpecificNewsCommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpecificNewsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

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


    private News.DataBean dataBean;
    private List<Image> mImgs;
    private View mHeaderView;
    private WrapView mWrapView;
    private SpecificNewsCommentAdapter mSpecificNewsCommentAdapter;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_news);
        ButterKnife.bind(this);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.list_news_item_header, null, false);
        mWrapView = new WrapView(mHeaderView);
        dataBean = getIntent().getParcelableExtra("dataBean");
        init();
    }

    private void init() {
        initToolbar();
        mRefresh.setOnRefreshListener(this);

        List<Comment> mDatas = null;
        mSpecificNewsCommentAdapter = new SpecificNewsCommentAdapter(mDatas, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mSpecificNewsCommentAdapter);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mWrapView.itemView);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        mWrapView.mTextNickname.setText(dataBean.getUser_name() != null ? dataBean.getUser_name() + "" : "没有名字就显示我了");
        mWrapView.mNewsTextTime.setText(dataBean.getTime());
        mWrapView.mTextContennt.setText(dataBean.getContent());
        mWrapView.mNewsBtnFavorites.setText(dataBean.getLike_num());
        mWrapView.mNewsBtnMessage.setText(dataBean.getRemark_num());

        mImgs = new ArrayList<>();
        for (String url : getUrls(dataBean.getImg().getImg_small_src()))
            mImgs.add(new Image(url, Image.ADDIMAG));

        mWrapView.mAutoNineLayout.setImagesData(mImgs);

        reqestComentDatas();

    }

    private void reqestComentDatas() {

    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitle("");
        mToolBarTitle.setText(getString(R.string.specific_news_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> SpecificNewsActivity.this.finish());
    }

    private List<Comment> getDate() {
        return null;
       /* List<Comment> mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mDatas.add(new Comment());
        }
        return mDatas;*/
    }


    public String[] getUrls(String url) {
        return url.split(",");
    }

    @Override
    public void onRefresh() {

    }

    class WrapView {

        public View itemView;

        @Bind(R.id.news_item_card_view)
        CardView mCardVIew;
        @Bind(R.id.list_news_img_avatar)
        CircleImageView mImgAvatar;
        @Bind(R.id.list_news_text_nickname)
        TextView mTextNickname;
        @Bind(R.id.list_news_text_time)
        TextView mNewsTextTime;
        @Bind(R.id.textContennt)
        TextView mTextContennt;
        @Bind(R.id.autoNineLayout)
        AutoNineGridlayout mAutoNineLayout;
        @Bind(R.id.list_news_btn_message)
        Button mNewsBtnMessage;
        @Bind(R.id.list_news_btn_favorites)
        Button mNewsBtnFavorites;

        public WrapView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
