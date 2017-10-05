package com.mredrock.cyxbs.component.widget.selector;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.mredrock.cyxbs.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Jay on 2017/10/4.
 * 可自由定制item的选择器（单选/多选）
 */

public class MultiSelector extends FrameLayout {
    public interface OnItemSelectedChangeListener {
        void onItemClickListener(MultiSelector selector, RecyclerView.ViewHolder viewHolder, int position);

        void onItemSelectedChange(MultiSelector selector, RecyclerView.ViewHolder viewHolder,
                                  int value, boolean checked, int position);
    }

    public final int NO_LIMIT;

    private List mDisplayValues = new ArrayList();
    private List<Integer> mValues = new ArrayList<>();
    private final Set<Integer> mSelectedNumbers = new HashSet<>();

    private int mMaxSelectedNum;
    private int mMinSelectedNum;

    private RecyclerView mRecyclerView;
    private ViewInitializer mInitializer;
    private OnItemSelectedChangeListener mListener;

    public MultiSelector(@NonNull Context context) {
        this(context, null);
    }

    public MultiSelector(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiSelector(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        NO_LIMIT = context.getResources().getInteger(R.integer.no_limit);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MultiSelector);
        mMaxSelectedNum = array.getInteger(R.styleable.MultiSelector_max_num, NO_LIMIT);
        mMinSelectedNum = array.getInteger(R.styleable.MultiSelector_min_num, NO_LIMIT);
        boolean radioButtonMode = array.getBoolean(R.styleable.MultiSelector_radio_button_mode, false);
        int valuesId = array.getResourceId(R.styleable.MultiSelector_values, -1);
        int displayValuesId = array.getResourceId(R.styleable.MultiSelector_display_values, -1);
        array.recycle();

        if (radioButtonMode) {
            mMinSelectedNum = mMaxSelectedNum = 1;
        }
        if (valuesId != -1) {
            setValues(getResources().getIntArray(valuesId));
        }
        if (displayValuesId != -1) {
            setDisplayValues(getResources().getStringArray(displayValuesId));
        }

        inflate();
    }

    private void inflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_multi_selector, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
    }

    @SuppressWarnings("unchecked")
    public <T> void setDisplayValues(@NonNull T[] displayValues) {
        mDisplayValues.clear();
        setDisplayValues(Arrays.asList(displayValues));
    }

    public <T> void setDisplayValues(@NonNull List<T> displayValues) {
        mDisplayValues = displayValues;
        if (mInitializer != null) {
            mInitializer.getAdapter().notifyDataSetChanged();
        }
    }

    public void setValues(@NonNull int[] values) {
        mValues.clear();
        List<Integer> list = new ArrayList<>();
        for (int i : values) {
            list.add(i);
        }
        setValues(list);
    }

    public void setValues(@NonNull List<Integer> values) {
        mValues = values;
    }

    public boolean setSelected(int position, boolean selected) {
        boolean changed = false;

        if (isRadioButtonMode() && !isPositionSelected(position)) {
            mSelectedNumbers.clear();
            mSelectedNumbers.add(position);
            changed = true;
        } else if (allowChange(position, selected)) {
            if (selected) {
                mSelectedNumbers.add(position);
            } else {
                mSelectedNumbers.remove(position);
            }
            changed = true;
        }

        if (changed && mInitializer != null) {
            mInitializer.getAdapter().notifyDataSetChanged();
        }
        return changed;
    }

    public boolean toggle(int position) {
        return setSelected(position, !isPositionSelected(position));
    }

    private boolean allowChange(int position, boolean selected) {
        int size = selectedSize();

        //选中状态与当前状态一致时不改变
        if (selected == isPositionSelected(position)) return false;

        if (!selected && size > mMinSelectedNum) return true;   //选中数量不允许小于最小值
        else if (selected && mMaxSelectedNum == NO_LIMIT) return true;  //选中数量不允许大于最大值
        else if (selected && size < mMaxSelectedNum) return true;
        else return false;
    }

    public void clearSelected() {
        mSelectedNumbers.clear();
    }

    public void setViewInitializer(ViewInitializer initializer) {
        mInitializer = initializer;
        mRecyclerView.setLayoutManager(mInitializer.getLayoutManager());
        mRecyclerView.setAdapter(mInitializer.getAdapter());
        if (mInitializer.getItemDecoration() != null) {
            mRecyclerView.addItemDecoration(mInitializer.getItemDecoration());
        }
    }

    public void setOnItemSelectedChangeListener(OnItemSelectedChangeListener listener) {
        mListener = listener;
    }

    public void setMaxSelectedNum(int maxSelectedNum) {
        mMaxSelectedNum = maxSelectedNum;
    }

    public void setMinSelectedNum(int minSelectedNum) {
        mMinSelectedNum = minSelectedNum;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getDisplayValues() {
        return mDisplayValues;
    }

    @SuppressWarnings("unchecked")
    public <T> T getDisplayValue(int position) {
        T value = null;
        if (position >= 0 && position < mDisplayValues.size()) {
            value = (T) mDisplayValues.get(position);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getSelectedDisplayValue() {
        List<T> values = new ArrayList<>();
        for (int selectedNumber : mSelectedNumbers) {
            values.add(getDisplayValue(selectedNumber));
        }
        return values;
    }

    public List<Integer> getValues() {
        return mValues;
    }

    public int getValue(int position) {
        int value = position;
        if (position >= 0 && position < mValues.size()) {
            value = mValues.get(position);
        }
        return value;
    }

    public int[] getSelectedValues() {
        int[] values = new int[mSelectedNumbers.size()];
        Iterator<Integer> iterator = mSelectedNumbers.iterator();
        for (int i = 0; i < mSelectedNumbers.size(); i++) {
            values[i] = getValue(iterator.next());
        }
        return values;
    }

    public int getMaxSelectedNum() {
        return mMaxSelectedNum;
    }

    public int getMinSelectedNum() {
        return mMinSelectedNum;
    }

    public int selectedSize() {
        return mSelectedNumbers.size();
    }

    public boolean isPositionSelected(int position) {
        return mSelectedNumbers.contains(position);
    }

    public boolean isValueSelected(int values) {
        return isPositionSelected(mValues.indexOf(values));
    }

    public <T> boolean isDisplayValueSelected(T displayValues) {
        return isPositionSelected(mDisplayValues.indexOf(displayValues));
    }

    public boolean isRadioButtonMode() {
        return mMaxSelectedNum == mMinSelectedNum && mMaxSelectedNum == 1;
    }

    public abstract static class Adapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
        protected final MultiSelector mSelector;

        public Adapter(MultiSelector selector) {
            mSelector = selector;
        }

        protected abstract void bindView(VH holder, T displayValue, boolean selected, int position);

        @Override
        @SuppressWarnings("unchecked")
        public final void onBindViewHolder(VH holder, int position) {
            bindView(holder, (T) mSelector.getDisplayValue(position),
                    mSelector.isPositionSelected(position), position);

            bindClickListener(holder, position);
        }

        private void bindClickListener(final VH holder, final int position) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OnItemSelectedChangeListener listener = mSelector.mListener;
                    if (listener != null) {
                        listener.onItemClickListener(mSelector, holder, position);
                    }
                    if (mSelector.toggle(position)) {
                        performSelectedChange(position, holder);
                    }
                }
            });
        }

        private void performSelectedChange(int position, VH holder) {
            final OnItemSelectedChangeListener listener = mSelector.mListener;
            if (listener != null) {
                int value = mSelector.getValue(position);
                boolean selected = mSelector.isPositionSelected(position);
                listener.onItemSelectedChange(mSelector, holder, value, selected, position);
            }
        }

        @Override
        public int getItemCount() {
            return mSelector.mDisplayValues.size();
        }
    }
}
