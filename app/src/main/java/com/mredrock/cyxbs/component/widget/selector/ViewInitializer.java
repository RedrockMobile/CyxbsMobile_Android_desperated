package com.mredrock.cyxbs.component.widget.selector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Jay on 2017/10/4.
 * 对MultiSelector中的RecyclerView进行初始化
 */

public final class ViewInitializer {
    private RecyclerView.LayoutManager mLayoutManager;
    private MultiSelector.Adapter mAdapter;
    private RecyclerView.ItemDecoration mItemDecoration;

    private ViewInitializer() {
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public MultiSelector.Adapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return mItemDecoration;
    }

    public static final class Builder {
        private final ViewInitializer mInitializer;
        private final Context mContext;

        public Builder(Context context) {
            mInitializer = new ViewInitializer();
            mContext = context;
        }

        public Builder layoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
            mInitializer.mLayoutManager = layoutManager;
            return this;
        }

        public Builder adapter(@NonNull MultiSelector.Adapter adapter) {
            mInitializer.mAdapter = adapter;
            return this;
        }

        public Builder itemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
            mInitializer.mItemDecoration = itemDecoration;
            return this;
        }

        public Builder horizontalLinearLayoutManager() {
            return linearLayoutManager(LinearLayoutManager.HORIZONTAL);
        }

        public Builder verticalLinearLayoutManager() {
            return linearLayoutManager(LinearLayoutManager.VERTICAL);
        }

        public Builder stringAdapter(MultiSelector selector, StringAdapter.LayoutWrapper layoutWrapper) {
            mInitializer.mAdapter = new StringAdapter(selector, layoutWrapper);
            return this;
        }

        public Builder gap(@Px int head, @Px int middle, @Px int tail) {
            mInitializer.mItemDecoration = new DefaultItemDecoration(head, middle, tail);
            return this;
        }

        public ViewInitializer build() {
            if (mInitializer.mAdapter == null) {
                throw new IllegalArgumentException("Must create an adapter");
            }

            if (mInitializer.mLayoutManager == null) {
                horizontalLinearLayoutManager();
            }
            return mInitializer;
        }

        private Builder linearLayoutManager(int orientation) {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(orientation);
            mInitializer.mLayoutManager = layoutManager;
            return this;
        }
    }
}