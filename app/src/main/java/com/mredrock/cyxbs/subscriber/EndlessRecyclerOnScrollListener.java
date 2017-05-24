package com.mredrock.cyxbs.subscriber;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mathiasluo on 16-4-13.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private boolean mIsShow = true;
    private int mScrollOffset = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int scrollDistance = 20, currentPage = 0;
        if (mScrollOffset > scrollDistance && mIsShow) {
            onHide();
            mIsShow = false;
            mScrollOffset = 0;
        } else if (mScrollOffset < -scrollDistance && !mIsShow) {
            onShow();
            mIsShow = true;
            mScrollOffset = 0;
        }
        if ((mIsShow && dy > 0) || (!mIsShow && dy < 0)) {
            mScrollOffset += dy;
        }
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);

    public abstract void onShow();

    public abstract void onHide();

}

