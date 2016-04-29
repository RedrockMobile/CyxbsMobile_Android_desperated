package com.mredrock.cyxbsmobile.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.recycler.AroundDishesItemAnimator;
import com.mredrock.cyxbsmobile.component.widget.recycler.DividerItemDecoration;
import com.mredrock.cyxbsmobile.model.Restaurant;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.adapter.AroundDishesAdapter;
import com.mredrock.cyxbsmobile.util.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Stormouble on 16/4/16.
 */
public class AroundDishesFragment extends Fragment {
    private static final int PRELOAD_SIZE = 6;

    @Bind(R.id.around_dishes_rv)
    RecyclerView mAroundFoodRecyclerView;
    @Bind(R.id.around_dishes_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int mLastItemPosition;
    private int mPage = 1;
    private boolean mFirstTimeTouchBottom = false;

    private ProgressBar mProgressBar;
    private AroundDishesAdapter mAdapter;
    private CompositeSubscription mCompositeSubscription;

    public AroundDishesFragment() {
        // Requires empty public constructor
    }

    public static AroundDishesFragment newInstance() {
        return new AroundDishesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_around_dishes, container, false);
        ButterKnife.bind(this, root);
        mProgressBar =
                (ProgressBar) getActivity().findViewById(R.id.progress);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCompositeSubscription = new CompositeSubscription();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPage = 1;
            loadAroundFoodList();
        });
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAroundFoodRecyclerView.setLayoutManager(layoutManager);
        mAroundFoodRecyclerView.setLayoutManager(layoutManager);
        mAroundFoodRecyclerView.setItemAnimator(new AroundDishesItemAnimator());
        mAroundFoodRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        mAdapter = new AroundDishesAdapter(getActivity(), new ArrayList<Restaurant>());
        mAdapter.setOnItemClickListener((parent, view1, position, id) ->
                FragmentUtils.startAnotherFragment(getFragmentManager(), AroundDishesFragment.this,
                    RestaurantDetailFragment.newInstance(mAdapter.getItem(position).id),
                    R.id.around_dishes_contentFrame));
        mAroundFoodRecyclerView.setAdapter(mAdapter);
        mAroundFoodRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!mSwipeRefreshLayout.isRefreshing()
                        && mLastItemPosition >= mAdapter.getItemCount() - PRELOAD_SIZE) {
                    if (!mFirstTimeTouchBottom) {
                        mPage++;
                        mSwipeRefreshLayout.setRefreshing(true);
                        loadAroundFoodList();
                    } else  {
                        mFirstTimeTouchBottom = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mLastItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

        loadAroundFoodList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mCompositeSubscription.clear();
    }

    private void loadAroundFoodList() {
        Subscription subscription = RequestManager.getInstance().getRestaurantList(new Subscriber<List<Restaurant>>() {

            @Override
            public void onStart() {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCompleted() {
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Restaurant> restaurants) {
                if (mPage == 1) {
                    mAdapter.updateItems(restaurants);
                } else {
                    mAdapter.addData(restaurants);
                }

            }
        }, String.valueOf(mPage));

        mCompositeSubscription.add(subscription);
    }
}
