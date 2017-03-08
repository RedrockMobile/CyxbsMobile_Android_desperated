package com.mredrock.cyxbs.ui.activity.lost;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wusui on 2017/2/7.
 */

public class ReleaseSucceedActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_succeed);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this, 50);
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

}
