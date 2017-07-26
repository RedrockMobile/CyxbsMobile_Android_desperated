package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2017/7/25.
 */

public class ListBottomSheetDialog extends BottomSheetDialog implements View.OnClickListener {
    public interface OnButtonClickListener {
        void onCancel(ListBottomSheetDialog dialog);

        void onSure(ListBottomSheetDialog dialog);
    }

    private TextView mSure;
    private ImageView mCancel;
    private RecyclerView mRv;
    private ListBottomAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<String> mDataList;

    private OnButtonClickListener mListener;

    public ListBottomSheetDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        mAdapter = new ListBottomAdapter();
        mDataList = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mListener = new OnButtonClickListener() {
            @Override
            public void onCancel(ListBottomSheetDialog dialog) {
            }

            @Override
            public void onSure(ListBottomSheetDialog dialog) {
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_bottom_sheet);

        mSure = (TextView) findViewById(R.id.sure);
        mCancel = (ImageView) findViewById(R.id.cancel);
        mRv = (RecyclerView) findViewById(R.id.rv);

        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        initRv();
    }

    private void initRv() {
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRv);
        mRv.setLayoutManager(mLayoutManager);
        mRv.setAdapter(mAdapter);
    }

    public void setData(List<String> list) {
        mDataList = list;
        refresh();
    }

    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                mListener.onCancel(this);
                dismiss();
                break;

            case R.id.sure:
                mListener.onSure(this);
                dismiss();
                break;
        }
    }

    private class ListBottomAdapter extends RecyclerView.Adapter<ListBottomViewHolder> {

        @Override
        public ListBottomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflate = LayoutInflater.from(parent.getContext());
            return new ListBottomViewHolder(inflate.inflate(R.layout.item_dialog_list_bottom_sheet, parent, false));
        }

        @Override
        public void onBindViewHolder(ListBottomViewHolder holder, int position) {
            if (mDataList != null && !mDataList.isEmpty()) {
                holder.mTv.setText(mDataList.get(position % mDataList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    private class ListBottomViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;

        public ListBottomViewHolder(View itemView) {
            super(itemView);

            mTv = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
