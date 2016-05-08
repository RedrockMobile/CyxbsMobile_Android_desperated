package com.mredrock.cyxbs.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.AutoNineGridlayout;
import com.mredrock.cyxbs.component.widget.CircleImageView;
import com.mredrock.cyxbs.component.widget.ExpandableTextView;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.model.social.Image;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.social.ImageActivity;
import com.mredrock.cyxbs.ui.activity.social.PersonInfoActivity;
import com.mredrock.cyxbs.ui.activity.social.SpecificNewsActivity;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.RxBus;
import com.mredrock.cyxbs.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<HotNews> mNews;

    public NewsAdapter(List<HotNews> mNews) {
        this.mNews = mNews;
    }


    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        HotNewsContent mDataBean = mNews.get(position).data;
        holder.setData(mDataBean, false);
        setDate(holder, mDataBean);
    }

    public void setDate(NewsAdapter.ViewHolder holder, HotNewsContent mDataBean) {

    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
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


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public static final String TAG = "NewsAdapter.ViewHolder";

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
        public boolean enableAvatarClick = true;

        private boolean isSingle = false;

        private Subscription mSubscription;

        @OnClick(R.id.list_news_img_avatar)
        public void takeToPersonInfoActivity(View view) {
            if (enableAvatarClick) {
                registerObservable();
                PersonInfoActivity.StartActivityWithData(view.getContext(), mHotNewsContent.user_head, mHotNewsContent.nick_name, mHotNewsContent.user_id);
            }
        }

        @OnClick(R.id.news_item_card_view)
        public void onItemClick(View view) {
            if (!isSingle) {

                registerObservable();
                SpecificNewsActivity.startActivityWithDataBean(view.getContext(), mHotNewsContent, mHotNewsContent.article_id);
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }


        private void registerObservable() {
            mSubscription = RxBus.getDefault()
                    .toObserverable(HotNewsContent.class)
                    .subscribe(s -> {
                        setData(s, false);
                        unregisterObservable();
                    }, throwable -> {
                        unregisterObservable();
                    });
        }

        private void unregisterObservable() {
            if (mSubscription != null && !mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
        }


        public void like(TextView textView) {
            mHotNewsContent.is_my_Like = true;
            String likeNumber = Integer.parseInt(textView.getText().toString()) + 1 + "";
            textView.setText(likeNumber);
            mHotNewsContent.like_num = likeNumber;
            RequestManager.getInstance()
                    .addThumbsUp(mHotNewsContent.article_id, mHotNewsContent.type_id)
                    .subscribe(s -> {
                        Log.i(TAG, "赞成功");
                        if (isSingle) RxBus.getDefault().post(mHotNewsContent);
                    }, throwable -> {
                        Log.e(TAG, throwable.toString());
                        String like_Number = Integer.parseInt(textView.getText().toString()) - 1 + "";
                        mHotNewsContent.is_my_Like = false;
                        textView.setText(like_Number);
                        mHotNewsContent.like_num = like_Number;
                    });
        }

        public void dislike(TextView textView) {
            String likeNumber = Integer.parseInt(textView.getText().toString()) - 1 + "";
            mHotNewsContent.is_my_Like = false;
            textView.setText(likeNumber);
            mHotNewsContent.like_num = likeNumber;

            RequestManager.getInstance()
                    .cancelThumbsUp(mHotNewsContent.article_id, mHotNewsContent.type_id)
                    .subscribe(s -> {
                        Log.i(TAG, "取消赞成功");
                        if (isSingle) RxBus.getDefault().post(mHotNewsContent);
                    }, throwable -> {
                        Log.e(TAG, throwable.toString());
                        String like_Number = Integer.parseInt(textView.getText().toString()) + 1 + "";
                        mHotNewsContent.like_num = like_Number;
                        mHotNewsContent.is_my_Like = true;
                        textView.setText(like_Number);
                    });
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

        public void setData(HotNewsContent hotNewsContent, boolean isSingleItem) {
            this.isSingle = isSingleItem;
            mHotNewsContent = hotNewsContent;
            mTextName.setText(hotNewsContent.type_id < BBDDNews.BBDD ? hotNewsContent.geType_id() : hotNewsContent.nick_name);
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
                    NewsAdapter.ViewHolder.this.dislike(mBtnFavor);
                else NewsAdapter.ViewHolder.this.like(mBtnFavor);
            });

            if (hotNewsContent.type_id == 6) {
                mTextContent.setText(hotNewsContent.content.title);
                mTextName.setText(hotNewsContent.content.getOfficeName());
            }

            mAutoNineGridlayout.setImagesData(getImgs(getUrls(hotNewsContent.img.img_small_src)));

            mAutoNineGridlayout.setOnAddImagItemClickListener((v, position) ->
                    ImageActivity.startWithData(itemView.getContext(), hotNewsContent, position)
            );


        }

    }


}
