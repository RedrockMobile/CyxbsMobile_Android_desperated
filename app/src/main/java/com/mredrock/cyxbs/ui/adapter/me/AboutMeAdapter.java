package com.mredrock.cyxbs.ui.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.AboutMe;
import com.mredrock.cyxbs.ui.adapter.BaseRecyclerViewAdapter;
import com.mredrock.cyxbs.util.ImageLoader;
import com.mredrock.cyxbs.util.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skylineTan on 2016/4/28 01:08.
 */
public class AboutMeAdapter extends BaseRecyclerViewAdapter<AboutMe, AboutMeAdapter.ViewHolder> {

    private static final String TYPE_PRAISE = "praise";
    private static final String TYPE_COMMENT = "remark";

    private OnItemClickListener mOnItemClickListener;

    public AboutMeAdapter(List<AboutMe> mDatas, Context context) {
        super(mDatas, context);
    }

    /**
     * 删除content中的回复xxx
     *
     * @param content
     * @return
     */
    private String filterContent(String content) {
        if (content == null) {
            return "";
        } else if (content.contains("回复")) {
            return content.replaceAll("(回复).*?([:：])", "").trim();
        } else {
            return content;
        }
    }

    @Override
    protected void bindData(ViewHolder holder, AboutMe data, int position) {
        holder.aboutMeNickName.setText(data.nickname.equals("") ? "来自一位没有名字的同学" : data.nickname);
        holder.aboutMeContent.setText(filterContent(data.content));
        holder.aboutMeTime.setText(TimeUtils.getTimeDetail(data.created_time));
        String myNickName = BaseAPP.getUser(BaseAPP.getContext()).nickname;
//        SpannableStringBuilder aboutMeNewContentSpanText = new SpannableStringBuilder(myNickName + "：" + data.article_content);
//        aboutMeNewContentSpanText.setSpan(new ForegroundColorSpan(BaseAPP.getContext().getResources().getColor(R.color.link_blue)), 0, myNickName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.aboutMeNewContent.setText(data.article_content);
        holder.author.setText(myNickName + "：");
        ImageLoader.getInstance().loadAvatar(data.photo_src, holder.aboutMeAvatar);
        String url = data.article_photo_src.split(",")[0];

        if (data.article_photo_src.equals("")) {
            holder.aboutMeNewImg.setVisibility(View.GONE);
        } else {
            holder.aboutMeNewImg.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadRedrockImage(url, holder.aboutMeNewImg);
        }
        //if (data.type.equals(TYPE_PRAISE)) {
        //ImageLoader.getInstance().loadRedrockImage(url, holder.aboutMeNewImg);
        if (data.type.equals(TYPE_PRAISE)) {
            holder.aboutMeType.setText("赞了我");
            holder.aboutMeContent.setVisibility(View.GONE);
        } else {
            holder.aboutMeType.setText("评论了我");
            holder.aboutMeContent.setVisibility(View.VISIBLE);
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView,
                    position, data));
        }
        //}
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_relate_me,
                        parent, false));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, AboutMe aboutMe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.about_me_avatar)
        ImageView aboutMeAvatar;
        @BindView(R.id.about_me_nick_name)
        TextView aboutMeNickName;
        @BindView(R.id.about_me_type)
        TextView aboutMeType;
        @BindView(R.id.about_me_time)
        TextView aboutMeTime;
        @BindView(R.id.about_me_content)
        TextView aboutMeContent;
        @BindView(R.id.about_me_new_img)
        ImageView aboutMeNewImg;
        @BindView(R.id.about_me_new_content)
        TextView aboutMeNewContent;
        @BindView(R.id.about_me_new_author)
        TextView author;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
