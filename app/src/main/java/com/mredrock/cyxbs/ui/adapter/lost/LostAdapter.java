package com.mredrock.cyxbs.ui.adapter.lost;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.lost.Lost;
import com.mredrock.cyxbs.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wusui on 2017/1/20.
 */

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.ViewHolder> implements View.OnClickListener {

    private List<Lost>mList = new ArrayList<>();
    private Context mContext;
    public LostAdapter(List<Lost> list,Context context){
        replaceDataList(list);
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance().loadAvatar(mList.get(position).avatar,holder.mAvator);
        holder.mNickName.setText(mList.get(position).connectName);
        holder.mType.setText(mList.get(position).category);
        holder.mTime.setText(mList.get(position).createdAt);
        holder.mContent.setText(mList.get(position).description);

        holder.itemView.setTag(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void replaceDataList(List<Lost> dataList) {
        mList.clear();
        if (dataList != null) {
            mList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void addDataList(List<Lost> dataList) {
        if (dataList != null) {
            for (Lost d: dataList) {
                if (!mList.contains(d)) {
                    mList.add(d);
                }
            }
            notifyItemRangeInserted(mList.size(),dataList.size());
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_lost_img_avatar)CircularImageView mAvator;
        @Bind(R.id.list_lost_text_nickname)TextView mNickName;
        @Bind(R.id.list_lost_text_kind)TextView mType;
        @Bind(R.id.list_lost_text_time)TextView mTime;
        @Bind(R.id.textView_content)TextView mContent;
        public static final String TAG = "LostAdapter.NormalViewHolder";

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view ,Lost lost);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view,(Lost) view.getTag());
        }
    }

}
