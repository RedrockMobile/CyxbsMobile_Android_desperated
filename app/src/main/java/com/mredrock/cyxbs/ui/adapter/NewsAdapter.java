package com.mredrock.cyxbs.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.ExpandableTextView;
import com.mredrock.cyxbs.component.widget.ninelayout.AutoNineGridlayout;
import com.mredrock.cyxbs.component.widget.ninelayout.CustomImageView;
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
import com.mredrock.cyxbs.util.Utils;

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
        holder.setData(mDataBean, false, getItemViewType(position));
        setDate(holder, mDataBean);
        setSdata(holder, mDataBean);
    }

    public void setDate(NewsAdapter.ViewHolder holder, HotNewsContent mDataBean) {

    }
    public void setSdata(NewsAdapter.ViewHolder holder, HotNewsContent mDataBean) {

    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        HotNewsContent hotNewsContent = mNews.get(position).data;
        String[] urls = NewsAdapter.ViewHolder.getUrls(hotNewsContent.img.img_src);
        if (urls.length > 1)
            return NewsAdapter.ViewHolder.TYPE_NINE_IMG;
        else return NewsAdapter.ViewHolder.TYPE_SINGLE_IMG;
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
        public static final int TYPE_SINGLE_IMG = 1;
        public static final int TYPE_NINE_IMG = 2;


        @Bind(R.id.list_news_img_avatar)
        public ImageView mImgAvatar;
        @Bind(R.id.list_news_text_nickname)
        public TextView mTextName;
        @Bind(R.id.list_news_text_time)
        public TextView mTextTime;
        @Bind(R.id.expandable_text)
        public TextView mTextContent;
        @Bind(R.id.list_news_btn_message)
        public TextView mBtnMsg;
        @Bind(R.id.list_news_btn_favorites)
        public TextView mBtnFavor;
        @Bind(R.id.textView_ex)
        public TextView mTextView_ex;
        @Bind(R.id.expand_text_view)
        public ExpandableTextView mExpandableTextView;
        @Bind(R.id.autoNineLayout)
        public AutoNineGridlayout mAutoNineGridlayout;
        @Bind(R.id.singleImg)
        public ImageView mImageView;


        public View itemView;
        HotNewsContent mHotNewsContent;

        public boolean enableAvatarClick = true;
        public boolean isFromPersonInfo = false;
        public boolean isFromMyTrend = false;
        private boolean isSingle = false;
        public int mImgType;


        private Subscription mSubscription;

        @OnClick(R.id.list_news_img_avatar)
        public void takeToPersonInfoActivity(View view) {
            if (enableAvatarClick && !isFromPersonInfo) {
                registerObservable();
                PersonInfoActivity.StartActivityWithData(view.getContext(), mHotNewsContent.user_head, mHotNewsContent.getNick_name(), mHotNewsContent.user_id);
            }
        }

        @OnClick(R.id.news_item_card_view)
        public void onItemClick(View view) {
            if (!isSingle) {
                registerObservable();
                SpecificNewsActivity.startActivityWithDataBean(view.getContext(), mHotNewsContent, mHotNewsContent.article_id, isFromPersonInfo, isFromMyTrend);
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
                    .subscribe(hotNewsContent -> {
                        setData(hotNewsContent, false, hotNewsContent.getType());
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
            likeToSetDataAndView(textView);
            RequestManager.getInstance()
                    .addThumbsUp(mHotNewsContent.article_id, mHotNewsContent.type_id)
                    .subscribe(s -> {
                        Log.i(TAG, "赞成功");
                        if (isSingle) RxBus.getDefault().post(mHotNewsContent);
                    }, throwable -> {
                        Log.e(TAG, throwable.toString());
                        disLikeToSetDataAndView(textView);
                    });
        }

        public void dislike(TextView textView) {
            disLikeToSetDataAndView(textView);
            RequestManager.getInstance()
                    .cancelThumbsUp(mHotNewsContent.article_id, mHotNewsContent.type_id)
                    .subscribe(s -> {
                        Log.i(TAG, "取消赞成功");
                        if (isSingle) RxBus.getDefault().post(mHotNewsContent);
                    }, throwable -> {
                        Log.e(TAG, throwable.toString());
                        likeToSetDataAndView(textView);
                    });
        }

        public void disLikeToSetDataAndView(TextView textView) {
            String likeNumber = Integer.parseInt(textView.getText().toString()) - 1 + "";

            mHotNewsContent.is_my_Like = false;
            mHotNewsContent.like_num = likeNumber;
            textView.setText(likeNumber);
            textView.setCompoundDrawablesWithIntrinsicBounds(textView.getResources()
                            .getDrawable(mHotNewsContent.is_my_Like ? R.drawable.ic_support_like : R.drawable.ic_support_unlike),
                    null, null, null);
        }


        public void likeToSetDataAndView(TextView textView) {
            String like_Number = Integer.parseInt(textView.getText().toString()) + 1 + "";
            mHotNewsContent.like_num = like_Number;
            mHotNewsContent.is_my_Like = true;

            textView.setText(like_Number);
            textView.setCompoundDrawablesWithIntrinsicBounds(textView.getResources()
                            .getDrawable(mHotNewsContent.is_my_Like ? R.drawable.ic_support_like : R.drawable.ic_support_unlike),
                    null, null, null);

        }


        public void setData(HotNewsContent hotNewsContent, boolean isSingleItem, int type) {
            this.isSingle = isSingleItem;
            mHotNewsContent = hotNewsContent;
            mImgType = type;


            mTextName.setText(hotNewsContent.type_id < BBDDNews.BBDD ? hotNewsContent.geType_id() : hotNewsContent.getNick_name());
            mTextTime.setText(TimeUtils.getTimeDetail(hotNewsContent.getTime()));

            mBtnMsg.setText(hotNewsContent.remark_num);
            mBtnFavor.setText(mHotNewsContent.like_num);

            mBtnFavor.setCompoundDrawablesWithIntrinsicBounds(mBtnFavor.getResources()
                            .getDrawable(hotNewsContent.is_my_Like ? R.drawable.ic_support_like : R.drawable.ic_support_unlike),
                    null, null, null);

            mExpandableTextView.setmMaxCollapsedLines(4);

            if (isSingle) {
                mExpandableTextView.setText(Html.fromHtml(hotNewsContent.content != null ? hotNewsContent.content.content : ""));
                mExpandableTextView.setmMaxCollapsedLines(1000);
            } else if (hotNewsContent.type_id < BBDDNews.BBDD) {
                mExpandableTextView.setText(hotNewsContent.content.title != null ? hotNewsContent.content.title : "");
            } else {
                mExpandableTextView.setText(hotNewsContent.content != null ? hotNewsContent.content.content : "");
            }

            if (!(mHotNewsContent.type_id < BBDDNews.BBDD || (mHotNewsContent.type_id == 6 && mHotNewsContent.user_id == null)))
                ImageLoader.getInstance().loadAvatar(hotNewsContent.user_head, mImgAvatar);
            else ImageLoader.getInstance().loadDefaltNewsAvatar(mImgAvatar);


            if (hotNewsContent.content.address != null && !hotNewsContent.content.address.equals(""))
                mTextView_ex.setVisibility(View.VISIBLE);
            else mTextView_ex.setVisibility(View.INVISIBLE);

            mBtnFavor.setOnClickListener(view -> {
                if (hotNewsContent.is_my_Like)
                    NewsAdapter.ViewHolder.this.dislike(mBtnFavor);
                else NewsAdapter.ViewHolder.this.like(mBtnFavor);
            });

            if (hotNewsContent.type_id == 6 && hotNewsContent.user_id != null && hotNewsContent.user_id.equals("0")) {
                isFromPersonInfo = true;
                mExpandableTextView.setText(hotNewsContent.content.content);
            } else if (hotNewsContent.type_id == 6) {
                isFromPersonInfo = true;
                mExpandableTextView.setText(hotNewsContent.content.title);
                mTextName.setText(hotNewsContent.content.getOfficeName());
            } else isFromPersonInfo = false;

            if (mExpandableTextView.getText().toString().equals(""))
                mExpandableTextView.setVisibility(View.GONE);

            List<Image> url = getImgs(getUrls(hotNewsContent.img.img_small_src));
            if (mImgType == TYPE_NINE_IMG) {
                mAutoNineGridlayout.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);

                if (url.size() != 0) {
                    mAutoNineGridlayout.setImagesData(getImgs(getUrls(hotNewsContent.img.img_small_src)));
                    mAutoNineGridlayout.setOnAddImagItemClickListener((v, position) -> ImageActivity
                            .startWithData(itemView.getContext(), hotNewsContent, position));
                } else
                    mAutoNineGridlayout.setVisibility(View.GONE);

            } else if (mImgType == TYPE_SINGLE_IMG) {
                mAutoNineGridlayout.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);

                if (url.size() != 0) {
                    String realUrl = url.get(0).url;
                    Context context = mImageView.getContext();
                    Glide.with(APP.getContext())
                            .load(realUrl.charAt(0) < 48 || realUrl.charAt(0) > 57 ? url : CustomImageView.BASE_IMG_URL + realUrl)
                            .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5")))
                            .error(R.drawable.img_placeholder)
                            .crossFade()
                            .transform(new BitmapTransformation(context) {
                                @Override
                                protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
                                    int width = itemView.getWidth();
                                    float resize;
                                    int w;
                                    if (width == 0) {
                                        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                        w = wm.getDefaultDisplay()
                                                .getWidth() - Utils.dip2px(context, 16);
                                        resize = (float) (w * 1.0 / toTransform.getWidth());
                                    } else {
                                        resize = (float) (width * 1.0 / toTransform.getWidth());
                                        w = width;
                                    }
                                    int h = (int) (toTransform.getHeight() * resize);
                                    if (h <= 0 || w <= 0) {
                                        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        return toTransform;
                                    }
                                    Bitmap res = pool.get(w, h, Bitmap.Config.ARGB_8888);
                                    if (res == null) {
                                        res = Bitmap.createScaledBitmap(toTransform, w, h, false);
                                    }
                                    if (res != toTransform) {
                                        toTransform.recycle();
                                    }
                                    return res;
                                }

                                @Override
                                public String getId() {
                                    return "item.news.single.image";
                                }
                            })
                            .into(mImageView);
                    mImageView.setOnClickListener(view -> ImageActivity.startWithData(itemView.getContext(), hotNewsContent, 0));
                } else
                    mImageView.setVisibility(View.GONE);

            }
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


    }


}
