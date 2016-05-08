package com.mredrock.cyxbs.ui.fragment.explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.RevealBackgroundView;
import com.mredrock.cyxbs.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbs.component.widget.recycler.EndlessRecyclerViewScrollListener;
import com.mredrock.cyxbs.component.widget.recycler.RestaurantsItemAnimator;
import com.mredrock.cyxbs.model.Food;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.explore.BaseExploreActivity;
import com.mredrock.cyxbs.ui.adapter.FoodListAdapter;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscription;

/**
 * Created by Stormouble on 16/5/4.
 */
public class SurroundingFoodFragment extends BaseExploreFragment {

    private static final String TAG = LogUtils.makeLogTag(SurroundingFoodFragment.class);

    @Bind(R.id.reveal_background)
    RevealBackgroundView mRevealBackground;
    @Bind(R.id.surrounding_food_rv)
    RecyclerView mSurroundingFoodListRv;

    private int[] mDrawingStartLocation;

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
    public int layoutId() {
        return R.layout.fragment_surrounding_food;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawingStartLocation = getArguments().getIntArray(BaseExploreActivity.ARG_DRAWING_START_LOCATION);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mSurroundingFoodListRv.setLayoutManager(layoutManager);
        mSurroundingFoodListRv.setItemAnimator(new RestaurantsItemAnimator());
        mSurroundingFoodListRv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        mAdapter = new FoodListAdapter(getActivity(), new ArrayList<Food>());
        mAdapter.setOnItemClickListener((parent, view, position, id) -> {
            int[] startLocation = new int[2];
            view.getLocationOnScreen(startLocation);
            startLocation[0] += view.getWidth() / 2;
            UIUtils.startAnotherFragment(SurroundingFoodFragment.this.getFragmentManager(), SurroundingFoodFragment.this,
                    SurroundingFoodDetailFragment.newInstance(mAdapter.getItem(position).id, startLocation),
                    R.id.surrounding_food_contentFrame);
        });
        mSurroundingFoodListRv.setAdapter(mAdapter);
        mSurroundingFoodListRv.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        mSurroundingFoodListRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                loadFoodList(page);
            }
        });

        enableRevealBackground(mRevealBackground, mDrawingStartLocation,
                savedInstanceState, state -> {
            if (RevealBackgroundView.STATE_FINISHED == state) {
                onMainContentVisibleChanged(true);

                loadFoodList(1);
            } else {
                onMainContentVisibleChanged(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadFoodList(1);
    }


    private void loadFoodList(int page) {
        Subscription subscription = RequestManager.getInstance().getFoodList(
                new SimpleSubscriber<List<Food>>(getActivity(), new SubscriberListener<List<Food>>() {

            @Override
            public void onStart() {
                mSwipeRefreshLayout.post(() -> onRefreshingStateChanged(true));
            }

            @Override
            public void onCompleted() {
                onRefreshingStateChanged(false);
                onErrorLayoutVisibleChanged(false);
            }

            @Override
            public void onError(Throwable e) {
                onRefreshingStateChanged(false);
                onErrorLayoutVisibleChanged(true);
            }

            @Override
            public void onNext(List<Food> foodList) {
                if (page == 1) {
                    mAdapter.updateDataWithAnimation(foodList);
                } else {
                    mAdapter.updateDataWhenPagination(foodList);
                }
            }
        }), String.valueOf(page));

        mCompositeSubscription.add(subscription);
    }
}
