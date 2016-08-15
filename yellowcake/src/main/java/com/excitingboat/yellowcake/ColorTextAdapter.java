package com.excitingboat.yellowcake;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by PinkD on 2016/8/8.
 * ColorTextAdapter
 */
public abstract class ColorTextAdapter implements Adapter {
    private List<ColorTextListView.ViewHolder> viewHolders;
    private DataSetObservable dataSetObservable = new DataSetObservable();

    protected List<ColorText> colorTexts;

    public ColorTextAdapter() {
        colorTexts = new ArrayList<>();
    }

    public void add(ColorText colorText) {
        colorTexts.add(colorText);
    }

    public void addAll(Collection<? extends ColorText> collection) {
        colorTexts.addAll(collection);
        notifyDataSetChanged();
    }

    public void setColorTexts(List<ColorText> colorTexts) {
        this.colorTexts = colorTexts;
        notifyDataSetChanged();
    }

    public void clear() {
        colorTexts.clear();
        notifyDataSetChanged();
    }

    public ColorTextAdapter(List<ColorText> colorTexts) {
        this.colorTexts = colorTexts;
    }

    public void createViewHolder() {
        viewHolders = new ArrayList<>();
    }

    public void saveViewHolder(ColorTextListView.ViewHolder viewHolder) {
        viewHolders.add(viewHolder);
    }

    public abstract void setData(ColorTextListView.ViewHolder viewHolder, int position);

    public List<ColorTextListView.ViewHolder> getViewHolders() {
        return viewHolders;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        //TODO  add Observer
        dataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        dataSetObservable.unregisterObserver(observer);
        viewHolders.clear();
    }

    public void notifyDataSetChanged() {
        dataSetObservable.notifyChanged();
    }

    @Override
    public int getCount() {
        return colorTexts.size();
    }

    @Override
    public boolean isEmpty() {
        return colorTexts.size() == 0;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public ColorText getItem(int position) {
        return colorTexts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }


}
