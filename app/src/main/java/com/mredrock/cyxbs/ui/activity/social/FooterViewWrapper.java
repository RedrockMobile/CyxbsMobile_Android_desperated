package com.mredrock.cyxbs.ui.activity.social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mredrock.cyxbs.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simonla on 2017/4/5.
 * 19:13
 */

public class FooterViewWrapper {

    @Bind(R.id.progressBar)
    CircleProgressBar mCircleProgressBar;
    @Bind(R.id.textLoadingFailed)
    TextView mTextLoadingFailed;

    private View footerView;

    public FooterViewWrapper(Context context, ViewGroup parent) {
        footerView = LayoutInflater.from(context)
                .inflate(R.layout.list_footer_item_news, parent, false);
        ButterKnife.bind(this, footerView);
    }

    public View getFooterView() {
        return footerView;
    }

    public void showLoading() {
        mCircleProgressBar.setVisibility(View.VISIBLE);
        mTextLoadingFailed.setVisibility(View.GONE);
    }

    public void showLoadingFailed() {
        mCircleProgressBar.setVisibility(View.INVISIBLE);
        mTextLoadingFailed.setVisibility(View.VISIBLE);
        mTextLoadingFailed.setText("加载失败，点击重新加载!");
    }

    public void showLoadingNoMoreData() {
        mCircleProgressBar.setVisibility(View.INVISIBLE);
        mTextLoadingFailed.setVisibility(View.VISIBLE);
        mTextLoadingFailed.setText("没有更多内容啦，你来发布吧！");
    }

    public void showLoadingNoData() {
        mCircleProgressBar.setVisibility(View.INVISIBLE);
        mTextLoadingFailed.setVisibility(View.VISIBLE);
        mTextLoadingFailed.setText("还没有数据哟,点击发送吧！");
    }

    public void onFailedClick(View.OnClickListener onClickListener) {
        mTextLoadingFailed.setOnClickListener(onClickListener::onClick);
    }

    public CircleProgressBar getCircleProgressBar() {
        return mCircleProgressBar;
    }
}
