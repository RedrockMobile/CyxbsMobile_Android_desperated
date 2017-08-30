package com.mredrock.cyxbs.ui.fragment.explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.RevealBackgroundView;
import com.mredrock.cyxbs.ui.fragment.BaseFragment;
import com.mredrock.cyxbs.util.GlideHelper;
import com.mredrock.cyxbs.util.LogUtils;

import butterknife.ButterKnife;

/**
 * Created by Stormouble on 16/5/3.
 */
abstract class BaseExploreFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = LogUtils.makeLogTag(BaseExploreFragment.class);

    ViewStub mErrorLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;

    CompositeSubscription mCompositeSubscription;
    GlideHelper mGlideHelper;

    public abstract int layoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(layoutId(), container, false);
        ButterKnife.bind(this, view);
        mErrorLayout = (ViewStub) view.findViewById(R.id.error_stub);
        mSwipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCompositeSubscription = new CompositeSubscription();
        mGlideHelper = new GlideHelper(getActivity(), R.drawable.place_holder);

        trySetupSwipeRefreshLayout();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCompositeSubscription.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    protected void enableRevealBackground(RevealBackgroundView revealBackground
            , int[] startLocation, Bundle savedInstanceState, RevealBackgroundView.OnStateChangeListener listener) {
        if (savedInstanceState == null) {
            if (startLocation != null && startLocation.length == 2) {
                revealBackground.setOnStateChangeListener(listener);
                revealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        revealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                        revealBackground.startFromLocation(startLocation);
                        return true;
                    }
                });
            }
        } else {
            revealBackground.setToFinishedFrame();
        }

    }

    protected void onErrorLayoutVisibleChanged(View contentView, boolean shouldVisible) {
        if (mErrorLayout != null && contentView != null) {
            if (shouldVisible) {
                contentView.setVisibility(View.INVISIBLE);
                mErrorLayout.setVisibility(View.VISIBLE);
            } else {
                contentView.setVisibility(View.VISIBLE);
                mErrorLayout.setVisibility(View.GONE);
            }
        }
    }

    protected void onRefreshingStateChanged(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    protected void enableDisableSwipeRefresh(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    private void trySetupSwipeRefreshLayout() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent),
                    ContextCompat.getColor(getContext(), R.color.colorPrimary));
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
    }
}
