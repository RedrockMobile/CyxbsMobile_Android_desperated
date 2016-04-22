package com.mredrock.cyxbsmobile.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.AutoNineGridlayout;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.model.community.Image;
import com.mredrock.cyxbsmobile.model.community.News;
import com.mredrock.cyxbsmobile.model.community.OkResponse;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.activity.ImageActivity;
import com.mredrock.cyxbsmobile.util.ImageLoader;

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
        News.DataBean mDataBean = mNews.get(position).getData();
        setupOnItemClick(holder, position, mDataBean);
        holder.setData(mDataBean);
    }

    @Override
    public int getItemCount() {
        return mNews != null ? mNews.size() : 0;
    }


    protected void setupOnItemClick(final NewsAdapter.ViewHolder viewHolder, final int position, News.DataBean dataBean) {
        if (onItemOnClickListener != null)
            viewHolder.itemView.setOnClickListener(v -> onItemOnClickListener.onItemClick(viewHolder.itemView, position, dataBean));
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
        @Bind(R.id.textContennt)
        public TextView mTextContent;
        @Bind(R.id.autoNineLayout)
        public AutoNineGridlayout mAutoNineGridlayout;
        @Bind(R.id.list_news_btn_message)
        public TextView mBtnMsg;
        @Bind(R.id.list_news_btn_favorites)
        public TextView mBtnFavor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public static void addThumbsUp(News.DataBean dataBean, TextView textView) {
            RequestManager.getInstance().addThumbsUp(dataBean.getId(), dataBean.getType_id())
                    .subscribe(okResponse -> {
                        if (okResponse.getState() == OkResponse.RESPONSE_OK) {
                            dataBean.setIs_my_Like(true);
                            textView.setText(Integer.parseInt(textView.getText().toString()) + 1 + "");
                        }
                    }, throwable -> {
                        Log.e("============>>>>>addThumbsUp", throwable.toString());
                    });
        }

        public static void cancelTHumbsUp(News.DataBean dataBean, TextView textView) {
            RequestManager.getInstance().cancelThumbsUp(dataBean.getId(), dataBean.getType_id())
                    .subscribe(okResponse -> {
                        if (okResponse.getState() == OkResponse.RESPONSE_OK) {
                            dataBean.setIs_my_Like(false);
                            textView.setText(Integer.parseInt(textView.getText().toString()) - 1 + "");
                        }
                    }, throwable -> {
                        Log.e("============>>>>>cancelTHumbsUp", throwable.toString());
                    });
        }

        public final static String[] getUrls(String url) {
            return url != null ? url.split(",") : new String[]{""};
        }

        public void setData(News.DataBean dataBean) {

            mTextName.setText(dataBean.getUser_name() != "" ? dataBean.getUser_name() + "" : "没有名字就显示我了");
            mTextTime.setText(dataBean.getTime());
            //mTextContent.setText(dataBean.getContentBean() != null ? dataBean.getContentBean().getContent() : "");
            mTextContent.setText(Html.fromHtml(dataBean.getContentBean() != null ? dataBean.getContentBean().getContent() : ""));
            mBtnFavor.setText(dataBean.getLike_num());
            mBtnMsg.setText(dataBean.getRemark_num());
            ImageLoader.getInstance().loadAvatar(dataBean.getUser_head(), mImgAvatar);

            mBtnFavor.setOnClickListener(view -> {
                if (dataBean.isIs_my_Like())
                    NewsAdapter.ViewHolder.cancelTHumbsUp(dataBean, mBtnFavor);
                else NewsAdapter.ViewHolder.addThumbsUp(dataBean, mBtnFavor);
            });

            List<Image> mImgs = new ArrayList<>();
            for (String url : getUrls(dataBean.getImg().getImg_small_src()))
                if (!url.equals("")) mImgs.add(new Image(url, Image.ADDIMAG));
            mAutoNineGridlayout.setImagesData(mImgs);
            mAutoNineGridlayout.setOnAddImagItemClickListener((v, position) -> {
                Intent intent = new Intent(itemView.getContext(), ImageActivity.class);
                intent.putExtra("dataBean", dataBean);
                intent.putExtra("position", position);
                Log.e("===============>>>>>>>", position + dataBean.getImg().getImg_small_src());
                itemView.getContext().startActivity(intent);
                ((Activity) itemView.getContext()).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            });


        }

    }

}
