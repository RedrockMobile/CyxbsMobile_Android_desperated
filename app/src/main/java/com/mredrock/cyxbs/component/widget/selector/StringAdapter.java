package com.mredrock.cyxbs.component.widget.selector;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jay on 2017/10/4.
 * 专为字符串设计的Adapter
 */

public class StringAdapter extends MultiSelector.Adapter<String, StringAdapter.TextViewViewHolder> {
    private LayoutWrapper mLayoutWrapper;

    public StringAdapter(MultiSelector selector, LayoutWrapper layoutWrapper) {
        super(selector);
        mLayoutWrapper = layoutWrapper;
    }

    @Override
    protected void bindView(TextViewViewHolder holder, String displayValue, boolean selected, int position) {
        mLayoutWrapper.onBindView(holder.mTextView, displayValue, selected, position);
    }

    @Override
    public TextViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mLayoutWrapper.getLayoutId(), parent, false);
        return new TextViewViewHolder(itemView, mLayoutWrapper.getTextViewId());
    }

    static class TextViewViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private TextViewViewHolder(View itemView, int textViewId) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(textViewId);
        }
    }

    public abstract static class LayoutWrapper {
        @LayoutRes
        public abstract int getLayoutId();

        @IdRes
        public abstract int getTextViewId();

        public void onBindView(TextView textView, String displayValue, boolean selected, int position) {
            textView.setText(displayValue);
        }
    }
}
