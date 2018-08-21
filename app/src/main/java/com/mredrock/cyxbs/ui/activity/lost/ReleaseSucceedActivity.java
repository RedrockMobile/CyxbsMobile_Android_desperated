package com.mredrock.cyxbs.ui.activity.lost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wusui on 2017/2/7.
 */

public class ReleaseSucceedActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_succeed);
        ButterKnife.bind(this);
        initializeToolbar();
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeToolbar() {
        toolbar.setTitle("");
        toolbarTitle.setText("发布成功");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this,LostActivity.class);
        startActivity(intent);
    }


}
