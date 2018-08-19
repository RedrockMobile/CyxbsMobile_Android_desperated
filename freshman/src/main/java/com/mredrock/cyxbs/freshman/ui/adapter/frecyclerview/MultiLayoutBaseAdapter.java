package com.mredrock.cyxbs.freshman.ui.adapter.frecyclerview;

import android.content.Context;

import java.util.List;

/**
 * Created by fxy on 2018/4/22.
 * fxy自用的RecyclerView封装 省略了重复写的Holder
 */

public abstract class MultiLayoutBaseAdapter<T> extends BaseAdapter<T> {
    public MultiLayoutBaseAdapter(Context context, List<T> data, int[] layoutIds) {
        super(context, data, layoutIds);
    }

    @Override
    public int getItemViewType(int position) {
        //获得指定布局的抽象方法
        return getItemType(position);
    }

    public abstract int getItemType(int position);//获取指定布局的类型的方法 子类实现


    @Override
    public void onBind(BaseHolder holder, T t, int position) {
        //子类继承的时候可以传入viewType来加载多布局
        onBinds(holder, t, position, getItemViewType(position));
    }

    public abstract void onBinds(BaseHolder holder, T t, int position, int viewType);//加载特定布局的控件


    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {

    }


}
