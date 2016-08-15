package com.excitingboat.freshmanspecial.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by PinkD on 2016/8/12.
 * LoadMoreListener
 */
public class LoadMoreListener extends RecyclerView.OnScrollListener {

    //TODO 明天去看看朱大的库怎么写的吧
 /*   @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

            boolean isRefreshing = swipeRefreshLayout.isRefreshing();
            if (isRefreshing) {
                adapter.notifyItemRemoved(adapter.getItemCount());
                return;
            }
            if (!isLoading) {
                isLoading = true;
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }*/
}
