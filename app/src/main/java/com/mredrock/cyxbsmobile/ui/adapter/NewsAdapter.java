package com.mredrock.cyxbsmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.AutoNineGridlayout;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.util.ScreenTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mNews;
    private List<Image> mImgs;
    private OnItemOnClickLIstener onItemOnClickLIstener;

    public void setOnItemOnClickLIstener(OnItemOnClickLIstener onItemOnClickLIstener) {
        this.onItemOnClickLIstener = onItemOnClickLIstener;
    }

    public NewsAdapter(List<News> mNews) {
        this.mNews = mNews;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        News.DataBean mDataBean = mNews.get(position).getData();
        setupOnItemClick(holder, position, mDataBean);
        mImgs = new ArrayList<>();
        for (String url : getUrls(mDataBean.getImg().getImg_small_src())) {
            mImgs.add(new Image(url, Image.ADDIMAG));
        }
        holder.mAutoNineGridlayout.setImagesData(mImgs);


        holder.mTextName.setText(mDataBean.getUser_name() != null ? mDataBean.getUser_name().toString() : "罗武侠");
        holder.mTextTime.setText(mDataBean.getTime());
        holder.mTextContent.setText(mDataBean.getContent());
        holder.mBtnMsg.setText(mDataBean.getLike_num());
        holder.mBtnFavor.setText(mDataBean.getRemark_num());
    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_news_img_avatar)
        CircleImageView mImgAvatar;
        @Bind(R.id.list_news_text_nickname)
        TextView mTextName;
        @Bind(R.id.list_news_text_time)
        TextView mTextTime;
        @Bind(R.id.textContennt)
        TextView mTextContent;
        @Bind(R.id.autoNineLayout)
        AutoNineGridlayout mAutoNineGridlayout;
        @Bind(R.id.list_news_btn_message)
        Button mBtnMsg;
        @Bind(R.id.list_news_btn_favorites)
        Button mBtnFavor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    protected void setupOnItemClick(final NewsAdapter.ViewHolder viewHolder, final int position, News.DataBean dataBean) {
        if (onItemOnClickLIstener != null)
            viewHolder.itemView.setOnClickListener(v -> onItemOnClickLIstener.onItemClick(viewHolder.itemView, position, dataBean));
    }

    public String[] getUrls(String url) {
        return url.split(",");
    }

    public interface OnItemOnClickLIstener {
        void onItemClick(View itemView, int position, News.DataBean dataBean);
    }

}
