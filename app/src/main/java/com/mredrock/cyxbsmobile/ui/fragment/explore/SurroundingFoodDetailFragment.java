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
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    private String mRestaurantKey;

    private List<FoodComment> mComments;
    private HeaderViewRecyclerAdapter mWrapperAdapter;
    private RestaurantCommentsAdapter mAdapter;
    private HeaderViewWrapper mHeaderViewWrapper;

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
        mHeaderViewWrapper = new HeaderViewWrapper(inflater, container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mComments = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFoodDetailRv.setLayoutManager(layoutManager);
        mAdapter = new RestaurantCommentsAdapter(mComments, getActivity());
        mWrapperAdapter = new HeaderViewRecyclerAdapter(mAdapter);
        mWrapperAdapter.addHeaderView(mHeaderViewWrapper.contentView);
        mFoodDetailRv.setAdapter(mWrapperAdapter);
        mFoodDetailRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager, 2) {
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
                getFoodAndCommentList(1);
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

    /**
     * Get food data and comments
     */
    private void getFoodAndCommentList(int page) {
        Subscription subscription = RequestManager.getInstance().getFoodAndCommentList(
                new SimpleSubscriber<FoodDetail>(getActivity(), true, new SubscriberListener<FoodDetail>() {
                    @Override
                    public void onCompleted() {
                        onRefreshingStateChanged(false);
                        onMainContentVisibleChanged(true);
                        onErrorLayoutVisibleChanged(false);

                        animateFab();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onRefreshingStateChanged(false);
                        onMainContentVisibleChanged(false);
                        onErrorLayoutVisibleChanged(true);
                    }

                    @Override
                    public void onNext(FoodDetail foodDetail) {
                        mHeaderViewWrapper.setData(foodDetail.shop_name, foodDetail.shop_content,
                                foodDetail.shop_tel, foodDetail.shop_address,
                                foodDetail.shop_sale_content, foodDetail.shop_image[0]);

                        if (page == 1) {
                            mAdapter.updateData(foodDetail.foodComments);
                        } else {
                            mAdapter.updateDataWhenPagination(foodDetail.foodComments);
                        }

                    }

                }), mRestaurantKey, String.valueOf(page));

        mCompositeSubscription.add(subscription);
    }

    /**
     * Get comments
     */
    private void getFoodCommentList(int page) {
        Subscription subscription = RequestManager.getInstance().getFoodCommentList(
                new SimpleSubscriber<List<FoodComment>>(getActivity(), new SubscriberListener<List<FoodComment>>() {
                    @Override
                    public void onNext(List<FoodComment> foodCommentList) {
                        if (page == 1) {
                            mAdapter.updateData(foodCommentList);
                        } else {
                            mAdapter.updateDataWhenPagination(foodCommentList);
                        }
                    }
                }), mRestaurantKey, String.valueOf(page));

        mCompositeSubscription.add(subscription);
    }

    private void sendCommentAndRefresh(String content, boolean shouldRetry) {
        User user = APP.getUser(getActivity());
        if (user != null) {
            Subscription subscription = RequestManager.getInstance().sendCommentAndRefresh(
                    new SimpleSubscriber<List<FoodComment>>(getActivity(), true, new SubscriberListener<List<FoodComment>>() {
                        @Override
                        public void onNext(List<FoodComment> commentList) {
                            if (commentList != null) {
                                mAdapter.updateData(commentList);
                            } else {
                                if (shouldRetry) {
                                    Snackbar.make(mFloatingActionButton, getResources().getString(R.string.send_comment_fail), Snackbar.LENGTH_LONG)
                                            .setAction(getResources().getString(R.string.send_comment_again), v -> {
                                                sendCommentAndRefresh(content, false);
                                            }).show();
                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.send_comment_fail_again), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }), mRestaurantKey, user.stuNum, user.idNum, content, user.name);

            mCompositeSubscription.add(subscription);
        }


    }

    private void animateFab() {
        mFloatingActionButton.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) mFloatingActionButton.getLayoutParams();
        mFloatingActionButton.setTranslationY(
                mFloatingActionButton.getHeight() + params.bottomMargin);
        mFloatingActionButton.setAlpha(0.f);
        mFloatingActionButton.animate()
                .alpha(1f)
                .translationY(0.f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(ANIMATION_DURATION);
    }

    private void onFabClick() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.send_comment_dialog_title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.send_comment_dialog_hint, R.string.send_comment_dialog_prefill, false, (dialog, input) -> {
                    sendCommentAndRefresh(input.toString(), true);
                })
                .positiveText(R.string.send_comment_dialog_positive_text)
                .negativeText(R.string.send_comment_dialog_negative_text)
                .show();
    }

    class HeaderViewWrapper {
        @Bind(R.id.restaurant_name)
        TextView mRestaurantName;
        @Bind(R.id.restaurant_photo)
        ImageView mRestaurantPhoto;
        @Bind(R.id.restaurant_introduction)
        TextView mRestaurantIntroduction;
        @Bind(R.id.restaurant_phone)
        TextDrawableView mRestaurantPhone;
        @Bind(R.id.restaurant_location)
        TextDrawableView mRestaurantLocation;
        @Bind(R.id.restaurant_promotion)
        TextDrawableView mRestaurantPromotion;

        public final View contentView;

        private String phone;

        @OnClick(R.id.restaurant_phone)
        public void onRestaurantPhoneCall() {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.phone_call_dialog_title)
                    .content(R.string.phone_call_dialog_content)
                    .theme(Theme.LIGHT)
                    .positiveText(R.string.phone_call_dialog_positive)
                    .negativeText(R.string.phone_call_dialog_negative)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            if (phone == null || phone.isEmpty()) {
                                return;
                            }

                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
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


        public HeaderViewWrapper(LayoutInflater inflater, ViewGroup container) {
            contentView = inflater.inflate(R.layout.food_detail_header, container, false);
            ButterKnife.bind(this, contentView);
        }

        public void setData(String name, String introduction, String phone,
                            String location, String promotion, String imageUrl) {
            mRestaurantName.setText(name);
            mRestaurantIntroduction.setText(introduction.replaceAll("\t", "").replaceAll("\r\n", ""));
            mRestaurantPhone.setText(phone);
            mRestaurantLocation.setText(location);
            mRestaurantPromotion.setText(promotion);
            mGlideHelper.loadImage(imageUrl, mRestaurantPhoto);

            this.phone = phone;
        }
    }
}
