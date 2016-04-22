package com.mredrock.cyxbsmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.model.community.ContentBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-4-22.
 */
public class OfficialNewAdapter extends RecyclerView.Adapter<OfficialNewAdapter.ViewHolder> {


    private List<ContentBean> mNews;
    private OnItemOnClickListener onItemOnClickListener;

    public OfficialNewAdapter(List<ContentBean> mNews) {
        this.mNews = mNews;
    }

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickLIstener) {
        this.onItemOnClickListener = onItemOnClickLIstener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_annex_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContentBean mDataBean = mNews.get(position);
        setupOnItemClick(holder, position, mDataBean);
        ContentBean contentBean = mNews.get(position);
        holder.mTextContent.setText(contentBean.getTitle());
        holder.mTextNickName.setText(mDataBean.getUnit().equals("") ? "教务在线" : mDataBean.getUnit());
    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }


    protected void setupOnItemClick(final OfficialNewAdapter.ViewHolder viewHolder, final int position, ContentBean dataBean) {
        if (onItemOnClickListener != null)
            viewHolder.itemView.setOnClickListener(v -> onItemOnClickListener.onItemClick(viewHolder.itemView, position, dataBean));
    }

    public interface OnItemOnClickListener {
        void onItemClick(View itemView, int position, ContentBean dataBean);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_news_img_avatar)
        CircleImageView mCircleImageView;
        @Bind(R.id.list_news_text_nickname)
        TextView mTextNickName;
        @Bind(R.id.list_news_text_time)
        TextView mTextTime;
        @Bind(R.id.list_news_btn_message)
        TextView mBtnMsg;
        @Bind(R.id.list_news_btn_favorites)
        TextView mBtbFro;
        @Bind(R.id.textView_content)
        TextView mTextContent;
        @Bind(R.id.textView_ex)
        TextView mTextEx;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
