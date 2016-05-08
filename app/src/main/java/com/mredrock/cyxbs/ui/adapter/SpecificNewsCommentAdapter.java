package com.mredrock.cyxbs.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.CircleImageView;
import com.mredrock.cyxbs.model.social.CommentContent;
import com.mredrock.cyxbs.ui.activity.social.PersonInfoActivity;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.RxBus;
import com.mredrock.cyxbs.util.TimeUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mathiasluo on 16-4-5.
 */
public class SpecificNewsCommentAdapter extends BaseRecyclerViewAdapter<CommentContent, SpecificNewsCommentAdapter.ViewHolder> {


    public SpecificNewsCommentAdapter(List<CommentContent> mDatas, Context context) {
        super(mDatas, context);
    }

    @Override
    protected void bindData(ViewHolder holder, CommentContent data, int position) {
        holder.mTextContent.setText(data.content);
        holder.mTextTime.setText(TimeUtils.getTimeDetail(data.created_time));
        holder.mTextViewNickName.setText(data.nickname);
        ImageLoader.getInstance().loadAvatar(data.photo_thumbnail_src, holder.mCircleImageView);
        holder.mCircleImageView.setOnClickListener(view -> PersonInfoActivity.StartActivityWithData(view.getContext(), data.photo_src, data.nickname, data.stunum));
        holder.mLinearLayout.setOnLongClickListener(view -> {
            RxBus.getDefault().post(data);
            return false;
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_comment_item, parent, false));
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_item_comment)
        LinearLayout mLinearLayout;
        @Bind(R.id.list_information_img_avatar)
        CircleImageView mCircleImageView;
        @Bind(R.id.list_information_text_nickname)
        TextView mTextViewNickName;
        @Bind(R.id.list_information_text_time)
        TextView mTextTime;
        @Bind(R.id.list_information_text_content)
        TextView mTextContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
