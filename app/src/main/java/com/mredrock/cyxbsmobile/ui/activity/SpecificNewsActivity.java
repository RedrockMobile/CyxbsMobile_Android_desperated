package com.mredrock.cyxbsmobile.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbsmobile.model.Comment;
import com.mredrock.cyxbsmobile.ui.adapter.SpecificNewsCommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpecificNewsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar_title)
    TextView mToolBarTitle;
    @Bind(R.id.news_item_card_view)
    CardView mCardVIew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_news);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initToolbar();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mCardVIew.getLayoutParams();
        params.topMargin = 0;
        mCardVIew.setLayoutParams(params);

        List<Comment> mDatas = getDate();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SpecificNewsCommentAdapter(mDatas, this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    private void initToolbar() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setTitle("");
        mToolBarTitle.setText(getString(R.string.specific_news_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(view -> SpecificNewsActivity.this.finish());
    }

    private List<Comment> getDate() {
        List<Comment> mDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mDatas.add(new Comment());
        }
        return mDatas;
    }

}
