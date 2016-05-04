package com.mredrock.cyxbsmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.AutoNineGridlayout;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.component.widget.ExpandableTextView;
import com.mredrock.cyxbsmobile.model.community.BBDDNews;
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.social.ImageActivity;
import com.mredrock.cyxbsmobile.util.ImageLoader;
import com.mredrock.cyxbsmobile.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mNews;
    private OnItemOnClickListener onItemOnClickListener;

    public NewsAdapter(List<News> mNews) {
        this.mNews = mNews;
    }

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickLIstener) {
        this.onItemOnClickListener = onItemOnClickLIstener;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        News.DataBean mDataBean = mNews.get(position).data;
        setupOnItemClick(holder, position, mDataBean);
        holder.setData(mDataBean, false);
        setDate(holder, mDataBean);
    }

    public void setDate(NewsAdapter.ViewHolder holder, News.DataBean mDataBean) {

    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }


    protected void setupOnItemClick(final NewsAdapter.ViewHolder viewHolder, final int position, News.DataBean dataBean) {
        if (onItemOnClickListener != null)
            viewHolder.itemView.setOnClickListener(v -> onItemOnClickListener.onItemClick(viewHolder.itemView, position, dataBean));
    }


    public void addDatas(List<News> datas) {
        mNews.addAll(datas);
        notifyDataSetChanged();
    }

    public void replaceDatas(List<News> datas) {
        mNews = datas;
        notifyDataSetChanged();
    }

    public void addToFirst(News news) {
        mNews.add(0, news);
        notifyDataSetChanged();
    }

    public interface OnItemOnClickListener {
        void onItemClick(View itemView, int position, News.DataBean dataBean);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_news_img_avatar)
        public CircleImageView mImgAvatar;
        @Bind(R.id.list_news_text_nickname)
        public TextView mTextName;
        @Bind(R.id.list_news_text_time)
        public TextView mTextTime;
        @Bind(R.id.expandable_text)
        public TextView mTextContent;
        @Bind(R.id.autoNineLayout)
        public AutoNineGridlayout mAutoNineGridlayout;
        @Bind(R.id.list_news_btn_message)
        public TextView mBtnMsg;
        @Bind(R.id.list_news_btn_favorites)
        public TextView mBtnFavor;
        @Bind(R.id.textView_ex)
        public TextView mTextView_ex;
        @Bind(R.id.expand_text_view)
        public ExpandableTextView mExpandableTextView;

        public static final String TAG = "NewsAdapter.ViewHolder";

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public static void like(News.DataBean dataBean, TextView textView) {
            RequestManager.getInstance()
                    .addThumbsUp(dataBean.id, dataBean.type_id)
                    .subscribe(new SimpleSubscriber<>(textView.getContext(), new SubscriberListener<OkResponse>() {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            dataBean.is_my_Like = true;
                            textView.setText(Integer.parseInt(textView.getText().toString()) + 1 + "");
                        }
                    }));
        }

        public static void dislike(News.DataBean dataBean, TextView textView) {
            RequestManager.getInstance()
                    .cancelThumbsUp(dataBean.id, dataBean.type_id)
                    .subscribe(new SimpleSubscriber<>(textView.getContext(), new SubscriberListener<OkResponse>() {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            dataBean.is_my_Like = false;
                            textView.setText(Integer.parseInt(textView.getText().toString()) - 1 + "");
                        }
                    }));
        }

        public final static String[] getUrls(String url) {
            return url != null ? url.split(",") : new String[]{""};
        }

        private List<Image> getImgs(String[] urls) {
            List<Image> mImgs = new ArrayList<>();
            for (String url : urls)
                if (!url.equals("")) mImgs.add(new Image(url, Image.ADDIMAG));
            return mImgs;
        }

        public void setData(News.DataBean dataBean, boolean isSingle) {

            mTextName.setText(dataBean.type_id < BBDDNews.BBDD ? dataBean.content.title : dataBean.nick_name);
            mTextTime.setText(TimeUtils.getTimeDetail(dataBean.time));
            mBtnFavor.setText(dataBean.like_num);
            mBtnMsg.setText(dataBean.remark_num);


            if (isSingle)
            // mTextContent.setText(Html.fromHtml(dataBean.getContentBean() != null ? dataBean.getContentBean().getContent() : ""));
            {
                mExpandableTextView.setText(Html.fromHtml(dataBean.content != null ? dataBean.content.content : ""));
                mExpandableTextView.setmMaxCollapsedLines(1000000);
            } else if (dataBean.type_id < BBDDNews.BBDD) {
                //mTextContent.setText(dataBean.getContentBean().getTitle() != null ? dataBean.getContentBean().getTitle() : "");
                mExpandableTextView.setText(dataBean.content.title != null ? dataBean.content.title : "");
            } else {
                //mTextContent.setText(dataBean.getContentBean() != null ? dataBean.getContentBean().getContent() : "");
                mExpandableTextView.setText(dataBean.content != null ? dataBean.content.content : "");
            }

            ImageLoader.getInstance().loadAvatar(dataBean.user_head, mImgAvatar);


            if (dataBean.content.address != null && !dataBean.content.address.equals(""))
                mTextView_ex.setVisibility(View.VISIBLE);
            else mTextView_ex.setVisibility(View.INVISIBLE);

            mBtnFavor.setOnClickListener(view -> {
                if (dataBean.is_my_Like)
                    NewsAdapter.ViewHolder.dislike(dataBean, mBtnFavor);
                else NewsAdapter.ViewHolder.like(dataBean, mBtnFavor);
            });

            mAutoNineGridlayout.setImagesData(getImgs(getUrls(dataBean.img.img_small_src)));

            mAutoNineGridlayout.setOnAddImagItemClickListener((v, position) ->
                    ImageActivity.startWithData(itemView.getContext(), dataBean, position)
            );

        }


    }


}
