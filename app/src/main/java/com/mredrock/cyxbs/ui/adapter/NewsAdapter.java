package com.mredrock.cyxbs.ui.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ExpandableTextView;
import com.mredrock.cyxbs.component.widget.ninelayout.AutoNineGridlayout;
import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.event.ItemChangedEvent;
import com.mredrock.cyxbs.model.social.BBDDNews;
import com.mredrock.cyxbs.model.social.HotNews;
import com.mredrock.cyxbs.model.social.HotNewsContent;
import com.mredrock.cyxbs.model.social.Image;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleObserver;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.social.ImageActivity;
import com.mredrock.cyxbs.ui.activity.social.PersonInfoActivity;
import com.mredrock.cyxbs.ui.activity.social.SpecificNewsActivity;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.RxBus;
import com.mredrock.cyxbs.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

/**
 * Created by mathiasluo on 16-4-4.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<HotNews> mNews;

    public NewsAdapter(List<HotNews> mNews) {
        if (mNews == null) {
            mNews = new ArrayList<>(0);
        }
        this.mNews = mNews;
    }

    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsAdapter.NewsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
        HotNewsContent mDataBean = mNews.get(position).data;
        holder.setData(mDataBean, false);
        setDate(holder, mDataBean);
    }

    public void setDate(NewsAdapter.NewsViewHolder holder, HotNewsContent mDataBean) {
    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }


    public void addDataList(List<HotNews> dataList) {
        mNews.addAll(dataList);
        notifyItemRangeInserted(mNews.size(), dataList.size());
    }

    public void replaceDataList(List<HotNews> dataList) {
        //   mNews = dataList;
        mNews.clear();
        mNews.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addToFirst(HotNews hotNews) {
        mNews.add(0, hotNews);
        notifyDataSetChanged();
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        public static final String TAG = "NewsAdapter.NormalViewHolder";

        @BindView(R.id.list_news_img_avatar)
        public ImageView mImgAvatar;
        @BindView(R.id.list_news_text_nickname)
        public TextView mTextName;
        @BindView(R.id.list_news_text_time)
        public TextView mTextTime;
        @BindView(R.id.expandable_text)
        public TextView mTextContent;
        @BindView(R.id.list_news_btn_message)
        public TextView mBtnMsg;
        @BindView(R.id.list_news_btn_favorites)
        public TextView mBtnFavor;
        @BindView(R.id.textView_ex)
        public TextView mTextViewEx;
        @BindView(R.id.expand_text_view)
        public ExpandableTextView mExpandableTextView;
        @BindView(R.id.autoNineLayout)
        public AutoNineGridlayout mAutoNineGridlayout;
        @BindView(R.id.singleImg)
        public ImageView mImageView;
        @BindView(R.id.divider)
        public View mDivider;
        @BindView(R.id.news_item_card_view)
        public CardView mCardView;

        public View itemView;
        HotNewsContent mHotNewsContent;

        public boolean enableAvatarClick = true;
        public boolean isFromPersonInfo = false;
        public boolean isFromMyTrend = false;

        private boolean isSingle = false;
        private Disposable mDisposable;

        public NewsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.list_news_img_avatar)
        public void takeToPersonInfoActivity(View view) {
            if (enableAvatarClick && !isFromPersonInfo) {
                registerObservable();
                PersonInfoActivity.StartActivityWithData(view.getContext(), mHotNewsContent.userHead,
                        mHotNewsContent.getNickName(), mHotNewsContent.user_id);
            }
        }

        @OnClick(R.id.news_item_card_view)
        public void onItemClick(View view) {
            if (!isSingle) {
                registerObservable();
                SpecificNewsActivity.startActivityWithDataBean(view.getContext(), mHotNewsContent,
                        mHotNewsContent.articleId, isFromPersonInfo, isFromMyTrend);
            }
        }

        @OnClick(R.id.list_news_btn_favorites)
        public void clickLikeAndDisLike() {
            if (BaseAPP.isLogin())
                mBtnFavor.setClickable(false);
            if (mHotNewsContent.isMyLike) {
                NewsAdapter.NewsViewHolder.this.dislike(mBtnFavor);
            } else {
                NewsAdapter.NewsViewHolder.this.like(mBtnFavor);
            }
        }

        private void registerObservable() {
            mDisposable = RxBus.getDefault()
                    .toFlowable(HotNewsContent.class)
                    .subscribe(hotNewsContent -> {
//                        setData(hotNewsContent, false);
                        unregisterObservable();
                    }, throwable -> unregisterObservable());
        }

        private void unregisterObservable() {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }

        public void like(TextView textView) {
            RequestManager.getInstance().addThumbsUp(new SimpleObserver<>(textView.getContext()
                            , new SubscriberListener<Unit>() {
                        @Override
                        public boolean onError(Throwable e) {
                            super.onError(e);
                            LogUtils.LOGE(TAG, "like", e);
                            //disLikeToSetDataAndView(textView);
                            mBtnFavor.setClickable(true);
                            return false;
                        }

                        @Override
                        public void onNext(Unit s) {
                            super.onNext(s);
                            //Log.i(TAG, "赞成功");
                            String likeNumber = Integer.parseInt(textView.getText().toString()) + 1 + "";

                            EventBus.getDefault().post(new ItemChangedEvent(likeNumber,
                                    mHotNewsContent.articleId, true));
                            likeToSetDataAndView(textView, likeNumber);

//                    if (isSingle) RxBus.getDefault().post(mHotNewsContent);
//                            if (isSingle) setData(mHotNewsContent, false);
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            mBtnFavor.setClickable(true);
                        }
                    }),
                    mHotNewsContent.articleId, mHotNewsContent.typeId,
                    BaseAPP.getUser(textView.getContext()).stuNum,
                    BaseAPP.getUser(textView.getContext()).idNum);
        }

        public void dislike(TextView textView) {
            RequestManager.getInstance().cancelThumbsUp(new SimpleObserver<>(textView.getContext()
                            , new SubscriberListener<Unit>() {
                        @Override
                        public boolean onError(Throwable e) {
                            super.onError(e);
                            // likeToSetDataAndView(textView);
                            e.printStackTrace();
                            mBtnFavor.setClickable(true);
                            return false;
                        }

                        @Override
                        public void onNext(Unit s) {
                            super.onNext(s);
                            String likeNumber = Integer.parseInt(textView.getText().toString()) - 1 + "";

                            EventBus.getDefault().post(new ItemChangedEvent(likeNumber,
                                    mHotNewsContent.articleId, false));
                            disLikeToSetDataAndView(textView, likeNumber);
//                    if (isSingle) RxBus.getDefault().post(mHotNewsContent);
//                            if (isSingle) setData(mHotNewsContent, false);
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            mBtnFavor.setClickable(true);
                        }
                    }), mHotNewsContent.articleId, mHotNewsContent.typeId,
                    BaseAPP.getUser(textView.getContext()).stuNum,
                    BaseAPP.getUser(textView.getContext()).idNum);
        }

        public void disLikeToSetDataAndView(TextView textView, String likeNumber) {

            mHotNewsContent.isMyLike = false;
            mHotNewsContent.likeNum = likeNumber;
            textView.setText(likeNumber);
            textView.setTextColor(isSingle ? Color.parseColor("#788EFA") : Color.parseColor("#666666"));
            textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat
                            .getDrawable(textView.getContext(), isSingle ? R.drawable.ic_favor_blue_white : R.drawable.ic_favor),
                    null, null, null);
        }

        public void likeToSetDataAndView(TextView textView, String likeNumber) {
            mHotNewsContent.likeNum = likeNumber;
            mHotNewsContent.isMyLike = true;

            textView.setText(likeNumber);
            textView.setTextColor(Color.parseColor("#788EFA"));
            textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat
                            .getDrawable(textView.getContext(), isSingle ? R.drawable.ic_favor_blue_comment : R.drawable.ic_favor_blue),
                    null, null, null);

        }

        private void hideLayoutAndView() {
            mAutoNineGridlayout.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);
        }

        private void showSingleImageView() {
            mAutoNineGridlayout.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadOffcialImg(getImageList(getUrls(mHotNewsContent.img.smallImg)).get(0).url, mImageView, itemView);
            mImageView.setOnClickListener(view -> ImageActivity.startWithData(itemView.getContext(), mHotNewsContent, 0));//恩，这里是点击图片放大的逻辑
        }

        private void showNineLayout() {
            mAutoNineGridlayout.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            mAutoNineGridlayout.setImagesData(getImageList(getUrls(mHotNewsContent.img.smallImg)));
            mAutoNineGridlayout.setOnAddImagItemClickListener((v, position) -> ImageActivity
                    .startWithData(itemView.getContext(), mHotNewsContent, position));
        }

        public final static String[] getUrls(String url) {
            return url != null ? url.split(",") : new String[]{""};
        }

        public final static List<Image> getImageList(String[] urls) {
            List<Image> mImgList = new ArrayList<>();
            for (String url : urls)
                if (!url.equals("")) mImgList.add(new Image(url, Image.TYPE_ADD));
            return mImgList;
        }

        public void setData(HotNewsContent hotNewsContent, boolean isSingleItem) {
            this.isSingle = isSingleItem;
            mHotNewsContent = hotNewsContent;
            mTextName.setText(mHotNewsContent.getNickName());
            mTextTime.setText(TimeUtils.getTimeDetail(hotNewsContent.getTime()));
            mBtnMsg.setText(hotNewsContent.remarkNum);
            mBtnFavor.setText(mHotNewsContent.likeNum);
            mBtnFavor.setTextColor(hotNewsContent.isMyLike ? Color.parseColor("#788EFA") : Color.parseColor("#666666"));
            mBtnFavor.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mBtnFavor.getContext(), hotNewsContent.isMyLike ? R.drawable.ic_favor_blue : R.drawable.ic_favor), null, null, null);
            mExpandableTextView.setmMaxCollapsedLines(4);
            if (isSingle) {
                mExpandableTextView.setText(hotNewsContent.officeNewsContent.content);
                mExpandableTextView.setmMaxCollapsedLines(1000);
            } else if (hotNewsContent.typeId < BBDDNews.BBDD) {
                mExpandableTextView.setText(hotNewsContent.officeNewsContent.title);
            } else {
                mExpandableTextView.setText(hotNewsContent.officeNewsContent.content);
            }

            // official news from school or red rock
            if (hotNewsContent.typeId == Const.TypeId.CYXW
                    || hotNewsContent.typeId == Const.TypeId.JWZX
                    || hotNewsContent.typeId == Const.TypeId.XSJZ
                    || hotNewsContent.typeId == Const.TypeId.XWGG
                    || hotNewsContent.typeId == Const.TypeId.NOTICE) {
                enableAvatarClick = false;
                mImgAvatar.setImageResource(R.drawable.ic_official_notification);
            }

            if (!(mHotNewsContent.typeId < BBDDNews.BBDD || (mHotNewsContent.typeId == 6 && mHotNewsContent.user_id == null)))
                ImageLoader.getInstance().loadAvatar(hotNewsContent.userHead, mImgAvatar);
            else ImageLoader.getInstance().loadDefaultNewsAvatar(mImgAvatar);

            if (hotNewsContent.officeNewsContent.address != null && !hotNewsContent.officeNewsContent.address.equals(""))
                mTextViewEx.setVisibility(View.VISIBLE);
            else mTextViewEx.setVisibility(View.INVISIBLE);

            if (hotNewsContent.typeId == 6 && hotNewsContent.user_id != null && hotNewsContent.user_id.equals("0")) {
                isFromPersonInfo = true;
                mExpandableTextView.setText(hotNewsContent.officeNewsContent.content);
            } else if (hotNewsContent.typeId == 6) {
                isFromPersonInfo = true;
                mExpandableTextView.setText(hotNewsContent.officeNewsContent.title);
                mTextName.setText(hotNewsContent.officeNewsContent.getOfficeName());
            }

            if (mExpandableTextView.getText().toString().equals(""))
                mExpandableTextView.setVisibility(View.GONE);

            List<Image> url = getImageList(getUrls(hotNewsContent.img.smallImg));
            hideLayoutAndView();
            //来自官方
            if (hotNewsContent.typeId == 6 && hotNewsContent.user_id != null && hotNewsContent.user_id.equals("0")) {
                if (url.size() == 1) {
                    showSingleImageView();
                } else if (url.size() > 1) {
                    showNineLayout();
                }
            }
            //不是官方
            else {
                if (url.size() != 0) {
                    showNineLayout();
                }
            }
        }
    }
}
