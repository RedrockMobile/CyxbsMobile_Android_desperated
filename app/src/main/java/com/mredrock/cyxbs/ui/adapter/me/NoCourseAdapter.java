package com.mredrock.cyxbs.ui.adapter.me;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.activity.me.NoCourseActivity;
import com.mredrock.cyxbs.ui.widget.NoCourseAddDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skylineTan on 2016/4/12 23:31.
 */
public class NoCourseAdapter extends RecyclerView.Adapter {
    public static final int TYPE_END = 0;
    public static final int TYPE_NORMAL = 1;

    private List<String> mNameList;
    private OnItemButtonClickListener mListener;
    private Activity mActivity;

    public NoCourseAdapter(Activity activity, List<String> nameList) {
        mNameList = nameList;
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 1 || position == mNameList.size()) {
            return TYPE_END;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_course_user, parent, false);
                return new NormalViewHolder(view);

            case TYPE_END:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_course_add, parent, false);
                return new EndViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_NORMAL:
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                String name = mNameList.get((mNameList.size() - 1) - position);
                name = name.length() > 2 ? name : name.substring(0, 1) + " " + name.substring(1);
                normalViewHolder.noCourseName.setText(name);
                normalViewHolder.noCourseDelete.setOnClickListener(view -> {
                    mNameList.remove((mNameList.size() - 1) - position);
                    notifyDataSetChanged();
                    if (mListener != null) {
                        mListener.onClickEnd(position);
                    }
                });
                break;

            case TYPE_END:
                EndViewHolder endViewHolder = (EndViewHolder) holder;
                endViewHolder.mAdd.setOnClickListener(v -> {
                    NoCourseAddDialog dialog = new NoCourseAddDialog(mActivity);
                    dialog.setListener(new NoCourseAddDialog.OnClickListener() {
                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onSure(EditText editText) {
                            if (!editText.getText().toString().equals("")) {
                                ((NoCourseActivity) mActivity).doAddAction(editText.getText().toString());
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                });
        }
    }


    @Override
    public int getItemCount() {
        return mNameList.size() + 1;
    }


    public void setOnItemButtonClickListener(OnItemButtonClickListener listener) {
        mListener = listener;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.no_course_name)
        TextView noCourseName;
        @BindView(R.id.no_course_delete)
        ImageView noCourseDelete;


        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemButtonClickListener {
        void onClickEnd(int position);
    }

    public class EndViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add)
        FrameLayout mAdd;

        public EndViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
