package com.mredrock.cyxbs.component.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.util.Utils;

/**
 * Created by simonla on 2017/4/4.
 * 12:44
 */

public class SearchView extends FrameLayout{

    public static final String TAG = SearchView.class.getSimpleName();

    ImageView mImageView;
    android.support.v7.widget.SearchView mSearchView;
    SearchSwitchListener mSearchSwitchListener;

    public SearchView(@NonNull Context context) {
        this(context, null);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mImageView = new ImageView(getContext());
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mImageView.setImageResource(R.drawable.bg_topic_search);
        mImageView.setPadding(0, 0, (int) Utils.dp2Px(getContext(), 30), 0);
        addView(mImageView);
        mSearchView = new android.support.v7.widget.SearchView(getContext());
        mSearchView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mSearchView);
        mSearchView.setQueryHint("搜索更多话题");
        mSearchView.setIconified(false);
        mSearchView.setVisibility(GONE);
        mImageView.setOnClickListener(v -> {
            mSearchView.setVisibility(VISIBLE);
            mImageView.setVisibility(GONE);
            mSearchView.setIconified(false);
            if(mSearchSwitchListener!=null) mSearchSwitchListener.onSearch();
        });
        mSearchView.setOnCloseListener(() -> {
            mSearchView.setVisibility(GONE);
            mImageView.setVisibility(VISIBLE);
            mSearchView.setIconified(false);
            if(mSearchSwitchListener!=null) mSearchSwitchListener.close();
            return false;
        });
    }

    public void addQueryListener(android.support.v7.widget.SearchView.OnQueryTextListener listener) {
        mSearchView.setOnQueryTextListener(listener);
    }

    public void addSearchSwitchListener(SearchSwitchListener searchSwitchListener) {
        mSearchSwitchListener = searchSwitchListener;
    }

    public interface SearchSwitchListener {
        void close();

        void onSearch();
    }
}
