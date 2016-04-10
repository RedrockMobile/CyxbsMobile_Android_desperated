package com.mredrock.cyxbsmobile.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.News;
import com.mredrock.cyxbsmobile.ui.activity.SpecificNewsActivity;

import java.util.List;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<News, NewsAdapter.ViewHolder> {


    public NewsAdapter(List<News> mDatas, Context context) {
        super(mDatas, context);
    }

    @Override
    protected void bindData(ViewHolder holder, News data, int position) {
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mContext.startActivity(new Intent(mContext, SpecificNewsActivity.class));
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
