package com.mredrock.cyxbs.freshman.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.freshman.R;
import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.ui.activity.App;
import com.mredrock.cyxbs.freshman.utils.SPHelper;
import com.mredrock.cyxbs.freshman.utils.ToastUtils;

import java.util.List;

/**
 * 只是因为懒。。就没有把Adapter和ViewHolder解耦了
 * 也是因为懒，不想写动画了
 */

public class AdmissionRequestAdapter extends RecyclerView.Adapter<AdmissionRequestAdapter.AdmissionRequestViewHolder> {

    private List<Description.DescribeBean> mDataList;
    private Boolean isEdit;
    private OnDeleteDataListener mListener;
    private int deleteNum = 0;

    private String TAG = "AdmissionRequestAdapter";

    public AdmissionRequestAdapter(List<Description.DescribeBean> mDataList, OnDeleteDataListener mListener) {
        this.mDataList = mDataList;
        isEdit = false;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public AdmissionRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(App.getContext()).inflate(R.layout.freshman_recycle_item_admission, parent, false);
        return new AdmissionRequestViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdmissionRequestViewHolder holder, int position) {
        holder.initData(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setDataList(List<Description.DescribeBean> mDataList) {
        this.mDataList = mDataList;
        notifyItemRangeInserted(0, mDataList.size() - 1);
    }

    public void changeData(Boolean isEdit) {
        this.isEdit = isEdit;
        deleteNum = 0;
        if (isEdit) {
            for (int i = 0; i < mDataList.size(); i++) {
                mDataList.get(i).setCheck(false);
            }
        }
        notifyDataSetChanged();
    }

    public void add(Description.DescribeBean bean) {
        if (mDataList.size() != 0) {
            mDataList.add(bean);
            notifyItemInserted(mDataList.size());
            SPHelper.putBean("admission", "admission", getDatas());
        } else {
            ToastUtils.show("还未成功加载数据哦~");
        }
    }

    public void deleteDatas() {
//      删除时候从大到小，不然会删不干净...
        for (int i = mDataList.size() - 1; i >= 0; i--) {
            if (mDataList.get(i).isDelete()) {
                mDataList.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public Description getDatas() {
        Description temp = new Description();
        temp.setIndex("入学必备");
        temp.setDescribe(mDataList);
        return temp;
    }


    public class AdmissionRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView item;
        private ImageView more;
        private ImageView delete;
        private TextView title;
        private TextView description;

        AdmissionRequestViewHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.iv_admission_check);
            title = itemView.findViewById(R.id.tv_admission_count);
            delete = itemView.findViewById(R.id.iv_admission_delete);
            more = itemView.findViewById(R.id.iv_admission_more);
            description = itemView.findViewById(R.id.tv_admission_description);

            item.setOnClickListener(this);
            itemView.setOnClickListener(v -> {
                if (isEdit) {
                    if (mDataList.get(getLayoutPosition()).getProperty().equals("必需"))
                        return;
                    if (mDataList.get(getLayoutPosition()).isDelete()) {
                        mDataList.get(getLayoutPosition()).setDelete(false);
                        delete.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_check_delete_normal));
                        deleteNum = deleteNum - 1;
                        mListener.getTotalNum(deleteNum);
                    } else {
                        mDataList.get(getLayoutPosition()).setDelete(true);
                        delete.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_check_delete_pressed));
                        deleteNum = deleteNum + 1;
                        mListener.getTotalNum(deleteNum);
                    }
                } else {
                    if (mDataList.get(getLayoutPosition()).getProperty().equals("用户自定义") || mDataList.get(getLayoutPosition()).getContent().equals(""))
                        return;
                    if (!mDataList.get(getLayoutPosition()).isOpen()) {
                        more.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_icon_see_simple));
                        notifyItemChanged(getLayoutPosition(), 0);
                        mDataList.get(getLayoutPosition()).setOpen(true);
                    } else {
                        more.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_icon_see_more));
                        notifyItemChanged(getLayoutPosition(), 0);
                        mDataList.get(getLayoutPosition()).setOpen(false);
                    }
                }
            });
        }

        void initData(Description.DescribeBean mData) {
            title.setText(mData.getName());
            int colorTitleId = mData.isCheck() ? R.color.freshmen_finish_black : R.color.freshmen_title_black;
            int colorContentId = mData.isCheck() ? R.color.freshman_content_finish_black : R.color.freshman_content_black;
            int drawableIdCheck = mData.isCheck() ? R.drawable.freshman_check_pressed : R.drawable.freshman_check_normal;
            int drawableIdDelete = mData.isDelete() ? R.drawable.freshman_check_delete_pressed : R.drawable.freshman_check_delete_normal;
            title.setTextColor(App.getContext().getResources().getColor(colorTitleId));
            description.setTextColor(App.getContext().getResources().getColor(colorContentId));
            item.setImageDrawable(App.getContext().getResources().getDrawable(drawableIdCheck));
            delete.setImageDrawable(App.getContext().getResources().getDrawable(drawableIdDelete));
            if (mData.getProperty().equals("用户自定义") || mData.getContent().equals("")) {
                more.setVisibility(View.GONE);
            } else if (mData.getProperty().equals("必需") || mData.getProperty().equals("非必需")) {
                more.setVisibility(View.VISIBLE);
            }
            if (mData.isOpen()) {
                description.setVisibility(View.VISIBLE);
                description.setText(mData.getContent());
                more.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_icon_see_simple));
            } else {
                description.setVisibility(View.GONE);
                more.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_icon_see_more));
            }
            if (!isEdit) {
                delete.setVisibility(View.GONE);
                item.setVisibility(View.VISIBLE);
            } else {
                item.setVisibility(View.INVISIBLE);
                if (mData.getProperty().equals("必需")) {
                    delete.setVisibility(View.GONE);
                } else {
                    delete.setVisibility(View.VISIBLE);
                }
            }
        }

        private void from2(int from, int to) {
            Description.DescribeBean temp = mDataList.get(from);
            mDataList.remove(from);
            mDataList.add(to, temp);
            if (from == to)
                return;
            notifyItemRemoved(from);
            notifyItemInserted(to);
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.iv_admission_check) {
                if (!mDataList.get(getLayoutPosition()).isCheck()) {
                    item.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_check_pressed));
                    title.setTextColor(App.getContext().getResources().getColor(R.color.freshmen_finish_black));
                    description.setTextColor(App.getContext().getResources().getColor(R.color.freshman_content_finish_black));
                    mDataList.get(getLayoutPosition()).setCheck(true);
                    from2(getLayoutPosition(), 0);
                    mListener.scrollToTop(true);
                } else {
                    item.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.freshman_check_normal));
                    title.setTextColor(App.getContext().getResources().getColor(R.color.freshmen_title_black));
                    mDataList.get(getLayoutPosition()).setCheck(false);
                    from2(getLayoutPosition(), getItemCount() - 1);
                }

            }
        }
    }

    public interface OnDeleteDataListener {
        void getTotalNum(int count);

        void scrollToTop(boolean isGoTo);
    }
}
