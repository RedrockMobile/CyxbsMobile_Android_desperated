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
import com.mredrock.cyxbsmobile.model.social.BBDDNews;
import com.mredrock.cyxbsmobile.model.social.HotNews;
import com.mredrock.cyxbsmobile.model.social.HotNewsContent;
import com.mredrock.cyxbsmobile.model.social.Image;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.social.ImageActivity;
import com.mredrock.cyxbsmobile.ui.activity.social.PersonInfoActivity;
import com.mredrock.cyxbsmobile.util.ImageLoader;
import com.mredrock.cyxbsmobile.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<HotNews> mNews;
    private OnItemOnClickListener onItemOnClickListener;

    public NewsAdapter(List<HotNews> mNews) {
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
        HotNewsContent mDataBean = mNews.get(position).data;
        setupOnItemClick(holder, position, mDataBean);
        holder.setData(mDataBean, false);
        setDate(holder, mDataBean);
    }

    public void setDate(NewsAdapter.ViewHolder holder, HotNewsContent mDataBean) {

    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }


    protected void setupOnItemClick(final NewsAdapter.ViewHolder viewHolder, final int position, HotNewsContent dataBean) {
        if (onItemOnClickListener != null)
            viewHolder.itemView.setOnClickListener(v -> onItemOnClickListener.onItemClick(viewHolder.itemView, position, dataBean));
    }


    public void addDatas(List<HotNews> datas) {
        mNews.addAll(datas);
        notifyDataSetChanged();
    }

    public void replaceDatas(List<HotNews> datas) {
        mNews = datas;
        notifyDataSetChanged();
    }

    public void addToFirst(HotNews hotNews) {
        mNews.add(0, hotNews);
        notifyDataSetChanged();
    }

    public interface OnItemOnClickListener {
        void onItemClick(View itemView, int position, HotNewsContent dataBean);
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

        public View itemView;

        HotNewsContent mHotNewsContent;


        public boolean enableClick = true;

        public static final String TAG = "NewsAdapter.ViewHolder";

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public static void like(HotNewsContent dataBean, TextView textView) {
            RequestManager.getInstance()
                    .addThumbsUp(dataBean.id, dataBean.type_id)
                    .subscribe(new SimpleSubscriber<>(textView.getContext(), new SubscriberListener<String>() {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            dataBean.is_my_Like = true;
                            textView.setText(Integer.parseInt(textView.getText().toString()) + 1 + "");
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    }));
        }

        public static void dislike(HotNewsContent dataBean, TextView textView) {
            RequestManager.getInstance()
                    .cancelThumbsUp(dataBean.id, dataBean.type_id)
                    .subscribe(new SimpleSubscriber<>(textView.getContext(), new SubscriberListener<String>() {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            dataBean.is_my_Like = false;
                            textView.setText(Integer.parseInt(textView.getText().toString()) - 1 + "");
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    }));

        }

        public final static String[] getUrls(String url) {
            return url != null ? url.split(",") : new String[]{""};
        }

        private List<Image> getImgs(String[] urls) {
            List<Image> mImgs = new ArrayList<>();
            for (String url : urls)
                if (!url.equals("")) mImgs.add(new Image(url, Image.TYPE_ADD));
            return mImgs;
        }

        public void setData(HotNewsContent hotNewsContent, boolean isSingle) {
            mHotNewsContent = hotNewsContent;
            mTextName.setText(hotNewsContent.type_id < BBDDNews.BBDD ?/*dataBean.content.title*/ hotNewsContent.geType_id() : hotNewsContent.nick_name);
            mTextTime.setText(TimeUtils.getTimeDetail(hotNewsContent.getTime()));
            mBtnFavor.setText(hotNewsContent.like_num);
            mBtnMsg.setText(hotNewsContent.remark_num);


            if (isSingle) {
                mExpandableTextView.setText(Html.fromHtml(hotNewsContent.content != null ? hotNewsContent.content.content : ""));
                mExpandableTextView.setmMaxCollapsedLines(1000000);
            } else if (hotNewsContent.type_id < BBDDNews.BBDD) {
                mExpandableTextView.setText(hotNewsContent.content.title != null ? hotNewsContent.content.title : "");
            } else {
                mExpandableTextView.setText(hotNewsContent.content != null ? hotNewsContent.content.content : "");
            }

            ImageLoader.getInstance().loadAvatar(hotNewsContent.user_head, mImgAvatar);

            if (hotNewsContent.content.address != null && !hotNewsContent.content.address.equals(""))
                mTextView_ex.setVisibility(View.VISIBLE);
            else mTextView_ex.setVisibility(View.INVISIBLE);

            mBtnFavor.setOnClickListener(view -> {
                if (hotNewsContent.is_my_Like)
                    NewsAdapter.ViewHolder.dislike(hotNewsContent, mBtnFavor);
                else NewsAdapter.ViewHolder.like(hotNewsContent, mBtnFavor);
            });

            mAutoNineGridlayout.setImagesData(getImgs(getUrls(hotNewsContent.img.img_small_src)));

            mAutoNineGridlayout.setOnAddImagItemClickListener((v, position) ->
                    ImageActivity.startWithData(itemView.getContext(), hotNewsContent, position)
            );

        }

        @OnClick(R.id.list_news_img_avatar)
        public void takeToPersonInfoActivity(View view) {
            if (enableClick)
                PersonInfoActivity.StartActivityWithData(view.getContext(), mHotNewsContent.user_head, mHotNewsContent.nick_name, mHotNewsContent.user_id);
        }

    }


}
