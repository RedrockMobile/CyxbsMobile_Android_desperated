package com.mredrock.cyxbs.freshman.ui.adapter.frecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mac on 2018/4/22.
 */

public class BaseHolder extends RecyclerView.ViewHolder {
    View itemview;
    SparseArray<View> views;

    public BaseHolder(View itemView) {
        super(itemView);
        this.itemview = itemView;
        views = new SparseArray<View>();
    }

    //暴露给adapter 得到holder
    public static <T extends BaseHolder> T getHolder(Context context, ViewGroup parent, int layoutId) {
        return (T) new BaseHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    //根据item中控件的id拿到控件
    public <T extends View> T getView(int viewId) {
        View childView = views.get(viewId);
        if (childView == null) {
            childView = itemView.findViewById(viewId);
            views.put(viewId, childView);
        }
        return (T) childView;
    }

    //根据Item中的控件Id向控件添加事件监听
    public BaseHolder setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
        return this;
    }

}
