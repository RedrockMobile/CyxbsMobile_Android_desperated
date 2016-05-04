package com.mredrock.cyxbsmobile.ui.activity.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private int mPosition;

    public static final String DATA = "data";
    public static final String POSITION = "position";


    public static final void startWithData(Context context, News.DataBean dataBean, int position) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(DATA, dataBean);
        intent.putExtra(POSITION, position);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        mDataBean = getIntent().getParcelableExtra(DATA);
        mPosition = getIntent().getIntExtra(POSITION, 0);
        init();
    }

    private void init() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mViewPager, mDataBean);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
    }

}
