package com.mredrock.cyxbsmobile.ui.activity.mypage;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.RelateMe;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.mredrock.cyxbsmobile.ui.adapter.RelateMeAdapter;

import java.util.ArrayList;
import java.util.List;

public class RelateMeActivity extends BaseActivity {

    @Bind(R.id.relate_me_recycler_View)
    RecyclerView relateMeRecyclerView;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private List<RelateMe> mRelateMeList;
    private RelateMeAdapter mRelateMeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relate_me);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }


    private void initData() {
        mRelateMeList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mRelateMeList.add(new RelateMe());
        }
        relateMeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRelateMeAdapter = new RelateMeAdapter(mRelateMeList, this);
        relateMeRecyclerView.setAdapter(mRelateMeAdapter);
    }


    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbarTitle.setText("与我相关");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(
                    v -> RelateMeActivity.this.finish());
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
