package com.mredrock.cyxbsmobile.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.mredrock.cyxbsmobile.util.Utils;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-5.
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mDatas;
    protected Context mContext;

    AdapterView.OnItemClickListener mItemClickListener;

    public BaseRecyclerViewAdapter(List<T> mDatas, Context context) {
        this.mDatas = mDatas;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        bindData(holder, mDatas.get(position), position);
        setupOnItemClick(holder, position);
    }

    protected abstract void bindData(VH holder, T data, int position);

    public T getItem(int position) {
        position = Math.max(0, position);
        return mDatas.get(position);
    }

    protected void setupOnItemClick(final VH viewHolder, final int position) {
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(null, viewHolder.itemView, position, position);
                }
            });
        }
    }

    public List<T> getDataSource() {
        return mDatas;
    }

    public void addData(List<T> newItems) {
        if (newItems != null) {
            mDatas.addAll(newItems);
            notifyDataSetChanged();
        }
    }

    public void updateData(List<T> data) {
        mDatas.clear();
        if (data != null) {
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void updateDataWhenPagination(List<T> data) {
        if (data != null) {
            mDatas.addAll(data);

            int currentSize = getItemCount();
            notifyItemRangeChanged(currentSize, mDatas.size() - 1);
        }
    }

    public void updateDataWithAnimation(List<T> data) {
        mDatas.clear();
        if (data != null) {
            mDatas = data;
            notifyItemRangeChanged(0, data.size() - 1);
        }
    }

    public void updateDataWithAnimation() {
        notifyItemRangeChanged(0, mDatas.size() - 1);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

}
