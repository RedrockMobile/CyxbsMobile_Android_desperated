package com.mredrock.cyxbs.ui.activity.me;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.EmptyRoom;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.adapter.me.EmptyAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmptyRoomResultActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;

    private ArrayList<EmptyRoom> mEmptyRoomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_room_result);
        ButterKnife.bind(this);
        initToolbar();
        initData();
        initRv();
    }

    private void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            mToolbar.setNavigationIcon(R.drawable.ic_back);
            mToolbar.setNavigationOnClickListener(
                    v -> EmptyRoomResultActivity.this.finish());
        }
    }

    private void initData() {
        Parcelable[] emptyRooms = getIntent().getParcelableArrayExtra("data");
        mEmptyRoomList = new ArrayList<>();
        for (Parcelable emptyroom : emptyRooms) {
            mEmptyRoomList.add((EmptyRoom) emptyroom);
        }
    }

    private void initRv() {
        EmptyAdapter emptyAdapter = new EmptyAdapter(mEmptyRoomList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(emptyAdapter);
    }
}
