package com.mredrock.cyxbsmobile.ui.fragment.explore;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbsmobile.APP;
import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.component.widget.RevealBackgroundView;
import com.mredrock.cyxbsmobile.component.widget.TextDrawableView;
import com.mredrock.cyxbsmobile.component.widget.recycler.EndlessRecyclerViewScrollListener;
import com.mredrock.cyxbsmobile.config.Const;
import com.mredrock.cyxbsmobile.model.FoodComment;
import com.mredrock.cyxbsmobile.model.FoodDetail;
import com.mredrock.cyxbsmobile.model.RedrockApiWrapper;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.explore.BaseExploreActivity;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.RestaurantCommentsAdapter;
import com.mredrock.cyxbsmobile.util.LogUtils;
import com.mredrock.cyxbsmobile.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Stormouble on 16/4/16.
 */
public class SurroundingFoodDetailFragment extends BaseExploreFragment {

    private static final String TAG = LogUtils.makeLogTag(SurroundingFoodDetailFragment.class);

    private static final String RESTAURANT_KEY = "shop_key";

    private static final long ANIMATION_DURATION = 700;

    @Bind(R.id.reveal_background)
    RevealBackgroundView mRevealBackground;
    @Bind(R.id.surrounding_food_detail_rv)
    RecyclerView mFoodDetailRv;
    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    private LinearLayout mHeaderLayout;
    private ImageView mFoodRestaurantImage;
    private TextView mFoodRestaurantName;
    private TextView mFoodRestaurantRecommend;
    private TextDrawableView mFoodRestaurantPhone;
    private TextDrawableView mFoodRestaurantLocation;
    private TextDrawableView mFoodRestaurantPromotion;
    private TextDrawableView mFoodRestaurantComment;

    private String mRestaurantKey;
    private String mPhoneNumber;

    private List<FoodComment> mComments;
    private HeaderViewRecyclerAdapter mWrapperAdapter;
    private RestaurantCommentsAdapter mAdapter;

    private int[] mDrawingStartLocation;

    public SurroundingFoodDetailFragment() {
        //Requires empty public constructor
    }

    public static SurroundingFoodDetailFragment newInstance(String restaurantKey, int[] startLocation) {
        SurroundingFoodDetailFragment fragment = new SurroundingFoodDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RESTAURANT_KEY, restaurantKey);
        bundle.putIntArray(BaseExploreActivity.ARG_DRAWING_START_LOCATION, startLocation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_surrounding_food_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestaurantKey = getArguments().getString(RESTAURANT_KEY);
        mDrawingStartLocation = getArguments().getIntArray(BaseExploreActivity.ARG_DRAWING_START_LOCATION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHeaderLayout =
                (LinearLayout) inflater.inflate(R.layout.food_detail_header, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mComments = new ArrayList<>();

        mFoodRestaurantName = (TextView) mHeaderLayout.findViewById(R.id.restaurant_name);
        mFoodRestaurantImage = (ImageView) mHeaderLayout.findViewById(R.id.restaurant_photo);
        mFoodRestaurantRecommend = (TextView) mHeaderLayout.findViewById(R.id.restaurant_recommend);
        mFoodRestaurantPhone = (TextDrawableView) mHeaderLayout.findViewById(R.id.restaurant_phone);
        mFoodRestaurantLocation = (TextDrawableView) mHeaderLayout.findViewById(R.id.restaurant_location);
        mFoodRestaurantPromotion = (TextDrawableView) mHeaderLayout.findViewById(R.id.restaurant_promotion);
        mFoodRestaurantComment = (TextDrawableView) mHeaderLayout.findViewById(R.id.restaurant_comment);

        mFoodRestaurantPhone.setOnClickListener(v -> onRestaurantPhoneCall());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFoodDetailRv.setLayoutManager(layoutManager);
        mAdapter = new RestaurantCommentsAdapter(mComments, getActivity());
        mWrapperAdapter = new HeaderViewRecyclerAdapter(mAdapter);
        mWrapperAdapter.addHeaderView(mHeaderLayout);
        mFoodDetailRv.setAdapter(mWrapperAdapter);
        mFoodDetailRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                getFoodCommentList(page);
            }
        });

        mFloatingActionButton.setVisibility(View.VISIBLE);
        mFloatingActionButton.setImageResource(R.drawable.ic_add);
        mFloatingActionButton.setOnClickListener(v -> onFabClick());


        enableRevealBackground(mRevealBackground, mDrawingStartLocation,
                savedInstanceState, state -> {
            if (RevealBackgroundView.STATE_FINISHED == state) {
                onMainContentVisibleChanged(true);
                mFloatingActionButton.setVisibility(View.VISIBLE);

                animateFab();

                getFoodCommentList(1);
            } else {
                onMainContentVisibleChanged(false);
                mFloatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onRefresh() {
        getFoodAndCommentList(1);
    }

    private void getFoodAndCommentList(int page) {
        Subscription subscription = RequestManager.getInstance().getFoodAndCommentList(
                new SimpleSubscriber<FoodDetail>(getActivity(), new SubscriberListener<FoodDetail>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
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
                    public void onNext(FoodDetail foodDetail) {
                        mPhoneNumber = foodDetail.shop_tel;

                        mFoodRestaurantName.setText(foodDetail.shop_name);
                        mFoodRestaurantLocation.setText(foodDetail.shop_address);
                        mFoodRestaurantPhone.setText(foodDetail.shop_tel);
                        mFoodRestaurantRecommend.setText(foodDetail.shop_sale_content);
                        mGlideHelper.loadImage(foodDetail.shop_image[0], mFoodRestaurantImage);

                        if (page == 1) {
                            mAdapter.updateData(foodDetail.foodComments);
                        } else {
                            mAdapter.updateDataWhenPagination(foodDetail.foodComments);
                        }

                    }

                }), mRestaurantKey, String.valueOf(page));

        mCompositeSubscription.add(subscription);
    }

    private void getFoodCommentList(int page) {
        Subscription subscription = RequestManager.getInstance().getFoodCommentList(
                new SimpleSubscriber<List<FoodComment>>(getActivity(), new SubscriberListener<List<FoodComment>>() {
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
                    public void onNext(List<FoodComment> foodCommentList) {

                    }
                }), mRestaurantKey, String.valueOf(page));

        mCompositeSubscription.add(subscription);
    }

    private void sendCommentAndRefresh(String content) {
        User user = APP.getUser(getActivity());
        if (user != null) {
            Subscription subscription = RequestManager.getInstance().sendComment(
                    new SimpleSubscriber<RedrockApiWrapper<Object>>(getActivity(), true, new SubscriberListener<RedrockApiWrapper<Object>>() {
                        @Override
                        public void onNext(RedrockApiWrapper<Object> data) {
                            if (data.status == Const.REDROCK_API_STATUS_SUCCESS) {
                                FoodComment comment = new FoodComment();
                                comment.comment_content = content;
                                comment.comment_author_name = user.name;
                                comment.comment_date = "";

                                mComments.add(comment);
                                Collections.sort(mComments, (Comparator<? super FoodComment>) new FoodComment());
                            } else {
                                Snackbar.make(mFloatingActionButton, "", Snackbar.LENGTH_SHORT);
                            }
                        }
                    }), mRestaurantKey, user.stuNum, user.idNum, content, user.name);

            mCompositeSubscription.add(subscription);
        }
    }

    private void animateFab() {
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) mFloatingActionButton.getLayoutParams();
        mFloatingActionButton.setTranslationY(
                mFloatingActionButton.getHeight() + params.bottomMargin);
        mFloatingActionButton.setAlpha(0.f);
        mFloatingActionButton.animate()
                .alpha(1f)
                .translationY(0.f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getFoodAndCommentList(1);
                    }
                });
    }

    private void onFabClick() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.send_comment_dialog_title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.send_comment_dialog_hint, R.string.send_comment_dialog_prefill, false, (dialog, input) -> {
                    sendCommentAndRefresh(input.toString());
                })
                .positiveText(R.string.send_comment_dialog_positive_text)
                .negativeText(R.string.send_comment_dialog_negative_text)
                .show();
    }

    private void onRestaurantPhoneCall() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.phone_call_dialog_title)
                .content(R.string.phone_call_dialog_content)
                .theme(Theme.LIGHT)
                .positiveText(R.string.phone_call_dialog_positive)
                .negativeText(R.string.phone_call_dialog_negative)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPhoneNumber));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getContext().checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                                    PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                        }
                        startActivity(intent);
                    }
                })
                .show();
    }
}
