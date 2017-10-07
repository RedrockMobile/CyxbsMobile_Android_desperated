package com.mredrock.cyxbs.component.widget.selector;

import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jay on 2017/10/4.
 * 默认的ItemDecoration
 */

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    private int mHead;
    private int mMiddle;
    private int mTail;

    public DefaultItemDecoration(@Px int head, @Px int middle, @Px int tail) {
        mHead = head;
        mMiddle = middle;
        mTail = tail;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager
                && ((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
            horizontalLinearLayoutManager(outRect, view, parent, state);
        }
    }

    private void horizontalLinearLayoutManager(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildLayoutPosition(view);
        if (position == 0) {
            outRect.set(mHead, 0, mMiddle, 0);
        } else if (position == state.getItemCount() - 1) {
            outRect.set(mMiddle, 0, mTail, 0);
        } else {
            outRect.set(mMiddle, 0, mMiddle, 0);
        }
    }

    //更多布局的支持需要时再说。。。
}
