package com.mredrock.cyxbsmobile.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.ui.adapter.ViewPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-4-16.
 */
public class ImageActivity extends AppCompatActivity {

    @Bind(R.id.container)
    ViewPager mViewPager;

    private News.DataBean mDataBean;
    private ViewPagerAdapter mAdapter;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        mDataBean = getIntent().getParcelableExtra("dataBean");
        position = getIntent().getIntExtra("position", 0);
        init();
    }

    private void init() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mViewPager, mDataBean);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);

    }

    @Override
    public void finish() {
        super.finish();
    }


}
