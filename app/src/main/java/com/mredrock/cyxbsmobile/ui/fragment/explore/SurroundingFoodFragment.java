package com.mredrock.cyxbsmobile.ui.fragment.explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.RevealBackgroundView;
import com.mredrock.cyxbsmobile.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbsmobile.component.widget.recycler.RestaurantsItemAnimator;
import com.mredrock.cyxbsmobile.model.Food;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.explore.BaseExploreActivity;
import com.mredrock.cyxbsmobile.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbsmobile.ui.adapter.FoodListAdapter;
import com.mredrock.cyxbsmobile.util.LogUtils;
import com.mredrock.cyxbsmobile.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Stormouble on 16/5/4.
 */
public class SurroundingFoodFragment extends BaseExploreFragment
        implements BaseExploreFragment.Listener{

    private static final String TAG = LogUtils.makeLogTag(SurroundingFoodFragment.class);

    private static final int PRELOAD_SIZE = 6;

    @Bind(R.id.reveal_background)
    RevealBackgroundView mRevealBackground;
    @Bind(R.id.surrounding_food_rv)
    RecyclerView mSurroundingFoodListRv;

    private int[] mDrawingStartLocation;

    private int mLastItemPosition;
    private int mPage = 1;
    private boolean mFirstTimeTouchBottom = false;

    private FoodListAdapter mAdapter;

    public SurroundingFoodFragment() {
        //Requires empty public constructor
    }

    public static SurroundingFoodFragment newInstance(int[] startLocation) {
        SurroundingFoodFragment fragment = new SurroundingFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putIntArray(BaseExploreActivity.ARG_DRAWING_START_LOCATION, startLocation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_surrounding_food;
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadFoodList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawingStartLocation = getArguments().getIntArray(BaseExploreActivity.ARG_DRAWING_START_LOCATION);
    }


    @Override
    public void onFragmentSetup() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mSurroundingFoodListRv.setLayoutManager(layoutManager);
        mSurroundingFoodListRv.setItemAnimator(new RestaurantsItemAnimator());
        mSurroundingFoodListRv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        mAdapter = new FoodListAdapter(getActivity(), new ArrayList<Food>());
        mAdapter.setOnItemClickListener((parent, view1, position, id) ->
                UIUtils.startAnotherFragment(getFragmentManager(), SurroundingFoodFragment.this,
                        SurroundingFoodDetailFragment.newInstance(mAdapter.getItem(position).id),
                        R.id.surrounding_food_contentFrame));
        mSurroundingFoodListRv.setAdapter(mAdapter);
        mSurroundingFoodListRv.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mSurroundingFoodListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!mSwipeRefreshLayout.isRefreshing()
                        && mLastItemPosition >= mAdapter.getItemCount() - PRELOAD_SIZE) {
                    if (!mFirstTimeTouchBottom) {
                        mPage++;
                        onRefreshingStateChanged(true);
                        loadFoodList();
                    } else {
                        mFirstTimeTouchBottom = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mLastItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            }
        });
    }

    @Override
    public void onFragmentIntroAnimation(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Log.d(TAG, "onFragmentIntroAnimation() invoked");
            enableRevealBackground(mRevealBackground, mDrawingStartLocation, state -> {
                if (RevealBackgroundView.STATE_FINISHED == state) {
                    mMainContent.setVisibility(View.VISIBLE);

                    loadFoodList();
                } else {
                    mMainContent.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            mRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public void onFragmentLoadData(Bundle savedInstanceState) {
        //Load data on animation end;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }

    private void loadFoodList() {
        Subscription subscription = RequestManager.getInstance().getFoodList(
                new SimpleSubscriber<List<Food>>(getActivity(), new SubscriberListener<List<Food>>() {

            @Override
            public void onStart() {
                mSwipeRefreshLayout.post(() -> onRefreshingStateChanged(true));
            }

            @Override
            public void onCompleted() {
                onRefreshingStateChanged(false);
            }

            @Override
            public void onError(Throwable e) {
                onRefreshingStateChanged(false);
            }

            @Override
            public void onNext(List<Food> foodList) {
                if (mPage == 1) {
                    mAdapter.updateDataWithAnimation(foodList);
                } else {
                    mAdapter.addData(foodList);
                }
            }
        }), String.valueOf(mPage));

        mCompositeSubscription.add(subscription);
    }
}
