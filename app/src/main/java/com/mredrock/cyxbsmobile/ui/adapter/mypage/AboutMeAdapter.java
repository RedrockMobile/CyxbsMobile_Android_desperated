package com.mredrock.cyxbsmobile.ui.adapter.mypage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.CircleImageView;
import com.mredrock.cyxbsmobile.model.AboutMe;
import com.mredrock.cyxbsmobile.ui.adapter.BaseRecyclerViewAdapter;
import com.mredrock.cyxbsmobile.util.ImageLoader;
import com.mredrock.cyxbsmobile.util.TimeUtils;
import java.util.List;

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


    @Override
    protected void bindData(ViewHolder holder, AboutMe data, int position) {
        holder.aboutMeNickName.setText(data.nickname.equals("") ? data.stunum : data.nickname);
        holder.aboutMeContent.setText(data.content);
        holder.aboutMeTime.setText(TimeUtils.getTimeDetail(data.created_time));
        holder.aboutMeNewContent.setText(data.article_content);
        ImageLoader.getInstance().loadAvatar(data.photo_src,holder.aboutMeAvatar);
        if(data.article_photo_src.equals("")){
            holder.aboutMeNewImg.setVisibility(View.GONE);
        }else {
            holder.aboutMeNewImg.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadImage(data.article_photo_src, holder.aboutMeNewImg);
        }
        if(data.type.equals(TYPE_PRAISE)){
            holder.aboutMeType.setText("赞了我");
            holder.aboutMeContent.setVisibility(View.GONE);
        }else {
            holder.aboutMeType.setText("");
            holder.aboutMeContent.setVisibility(View.VISIBLE);
        }
        if(mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, position, data));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_relate_me,
                                                    parent, false));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position, AboutMe aboutMe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.about_me_avatar) CircleImageView aboutMeAvatar;
        @Bind(R.id.about_me_nick_name) TextView aboutMeNickName;
        @Bind(R.id.about_me_type) TextView aboutMeType;
        @Bind(R.id.about_me_time) TextView aboutMeTime;
        @Bind(R.id.about_me_content) TextView aboutMeContent;
        @Bind(R.id.about_me_new_img) ImageView aboutMeNewImg;
        @Bind(R.id.about_me_new_content) TextView aboutMeNewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
