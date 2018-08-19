package com.mredrock.cyxbs.freshman.ui.adapter.frecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * 要抽离ViewHolder 并且逻辑要在BaseAdapter的子类实现
 */

public abstract class

BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    Context context;
    List<T> data;
    int[] layoutIds;

    public BaseAdapter(Context context, List<T> data, int layoutId) {
        this.context = context;
        this.data = data;
        this.layoutIds = new int[]{layoutId};
    }

    public BaseAdapter(Context context, List<T> data, int[] layoutIds) {
        this.context = context;
        this.data = data;
        this.layoutIds = layoutIds;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //从holder基类中获取holder 根据viewType来返回不同的layout
        //若加载多布局，需要覆写getItemView
        return BaseHolder.getHolder(context, parent, layoutIds[viewType]);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position, List<Object> payloads) {
        onBind(holder, data.get(position), position);
    }

    public abstract void onBind(BaseHolder holder, T t, int position);
}
