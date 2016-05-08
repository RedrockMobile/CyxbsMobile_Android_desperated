package com.mredrock.cyxbsmobile.ui.fragment.explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.RevealBackgroundView;
import com.mredrock.cyxbsmobile.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbsmobile.component.widget.recycler.EndlessRecyclerViewScrollListener;
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
        mSurroundingFoodListRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager, 5) {
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
