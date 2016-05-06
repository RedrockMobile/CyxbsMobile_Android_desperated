package com.mredrock.cyxbsmobile.ui.fragment.explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.RevealBackgroundView;
import com.mredrock.cyxbsmobile.ui.fragment.BaseFragment;
import com.mredrock.cyxbsmobile.util.GlideHelper;
import com.mredrock.cyxbsmobile.util.LogUtils;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Stormouble on 16/5/3.
 */
public abstract class BaseExploreFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = LogUtils.makeLogTag(BaseExploreFragment.class);

    protected View mMainContent;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected CompositeSubscription mCompositeSubscription;
    protected GlideHelper mGlideHelper;

    public interface Listener {
        void onFragmentSetup();

        void onFragmentIntroAnimation(Bundle savedInstanceState);

        void onFragmentLoadData(Bundle savedInstanceState);
    }

    public abstract int getLayoutID();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, view);
        mMainContent = view.findViewById(R.id.main_content);
        mSwipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCompositeSubscription = new CompositeSubscription();
        mGlideHelper = new GlideHelper(getActivity(), R.drawable.img_placeholder);

        trySetupSwipeRefreshLayout();

        if (this instanceof Listener) {
            Listener listener = (Listener) this;

            listener.onFragmentSetup();
            listener.onFragmentIntroAnimation(savedInstanceState);
            listener.onFragmentLoadData(savedInstanceState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mCompositeSubscription.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected void enableRevealBackground(RevealBackgroundView revealBackground
            , int[] startLocation, RevealBackgroundView.OnStateChangeListener listener) {
        if (startLocation != null) {
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
