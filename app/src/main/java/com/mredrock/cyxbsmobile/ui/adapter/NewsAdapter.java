package com.mredrock.cyxbsmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private News mNews;
    AdapterView.OnItemClickListener mItemClickListener;
    private List<Image> mImgs;
    private int singlePicX;


    public NewsAdapter(News mNews) {
        this.mNews = mNews;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        setupOnItemClick(holder, position);

        mImgs = new ArrayList<>();
        singlePicX = (int) new ScreenTools(APP.getContext()).getSinglePicX();
        mImgs.add(new Image("file:///android_asset/add_news.jpg", 500, 500, Image.ADDIMAG));

        holder.mAutoNineGridlayout.setImagesData(mImgs);
        holder.mTextName.setText("Luo12345");
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_news_img_avatar)
        CircleImageView mImgAvatar;
        @Bind(R.id.list_news_text_nickname)
        TextView mTextName;
        @Bind(R.id.list_news_text_time)
        TextView mTextTime;
        @Bind(R.id.autoNineLayout)
        AutoNineGridlayout mAutoNineGridlayout;
        @Bind(R.id.list_news_btn_message)
        Button mBtnMsg;
        @Bind(R.id.list_news_btn_favorites)
        Button mBtnFavor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTextName.setText("127893471209847");
        }
    }

    protected void setupOnItemClick(final NewsAdapter.ViewHolder viewHolder, final int position) {
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(null, viewHolder.itemView, position, position);
                }
            });
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }


}
