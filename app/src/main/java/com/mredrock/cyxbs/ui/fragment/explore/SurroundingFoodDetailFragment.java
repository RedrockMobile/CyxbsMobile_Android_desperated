package com.mredrock.cyxbs.ui.fragment.explore;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mredrock.cyxbs.APP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.component.widget.recycler.EndlessRecyclerViewScrollListener;
import com.mredrock.cyxbs.model.FoodComment;
import com.mredrock.cyxbs.model.FoodDetail;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.adapter.FoodCommentsAdapter;
import com.mredrock.cyxbs.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.permission.AfterPermissionGranted;
import com.mredrock.cyxbs.util.permission.EasyPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Stormouble on 16/4/16.
 */
public class SurroundingFoodDetailFragment extends BaseExploreFragment
        implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = LogUtils.makeLogTag(SurroundingFoodDetailFragment.class);
    private static final String RESTAURANT_KEY = "shop_key";

    private static final int RC_PHONE = 124;
    private static final long ANIMATION_DURATION = 700;

    @Bind(R.id.surrounding_food_detail_rv)
    RecyclerView mFoodDetailRv;
    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    private String mRestaurantKey;
    private List<FoodComment> mComments;
    private HeaderViewRecyclerAdapter mWrapperAdapter;
    private FoodCommentsAdapter mAdapter;
    private HeaderViewWrapper mHeaderViewWrapper;


    public SurroundingFoodDetailFragment() {
        //Requires empty public constructor
    }

    public static SurroundingFoodDetailFragment newInstance(String restaurantKey) {
        SurroundingFoodDetailFragment fragment = new SurroundingFoodDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RESTAURANT_KEY, restaurantKey);
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHeaderViewWrapper = new HeaderViewWrapper(this, inflater, container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mComments = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFoodDetailRv.setLayoutManager(layoutManager);
        mAdapter = new FoodCommentsAdapter(mComments, getActivity());
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

        mFoodDetailRv.setVisibility(View.INVISIBLE);
        mFloatingActionButton.setVisibility(View.INVISIBLE);
        getFoodAndCommentList(1, true);
    }


    @Override
    public void onRefresh() {
        getFoodAndCommentList(1, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(getActivity(), getResources().getString(R.string.phone_permission_explanation),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Get food data and comments
     */
    private void getFoodAndCommentList(int page, boolean firstLoaded) {
        Subscription subscription = RequestManager.getInstance().getFoodAndCommentList(
                new SimpleSubscriber<FoodDetail>(getActivity(), firstLoaded, new SubscriberListener<FoodDetail>() {
                    @Override
                    public void onStart() {
                        if (!firstLoaded) {
                            mSwipeRefreshLayout.post(() -> onRefreshingStateChanged(true));
                        }
                    }

                    @Override
                    public void onCompleted() {
                        onRefreshingStateChanged(false);
                        onErrorLayoutVisibleChanged(mFoodDetailRv,false);

                        if (firstLoaded) {
                            animateFab();
                        }
                    }

                    @Override
                    public boolean onError(Throwable e) {
                        onRefreshingStateChanged(false);
                        onErrorLayoutVisibleChanged(mFoodDetailRv, true);
                        return true;
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
        TextView mRestaurantPhone;
        @Bind(R.id.restaurant_location)
        TextView mRestaurantLocation;
        @Bind(R.id.restaurant_promotion)
        TextView mRestaurantPromotion;
        @Bind(R.id.restaurant_comment)
        TextView mRestaurantComment;

        private String phone;
        private Fragment fragment;
        public final View contentView;

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

                            phoneCall();
                        }
                    })
                    .show();
        }


        public HeaderViewWrapper(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
            this.fragment = fragment;
            contentView = inflater.inflate(R.layout.food_detail_header, container, false);
            ButterKnife.bind(this, contentView);

            setupTextLeftDrawable();
        }

        public void setupTextLeftDrawable() {
            Drawable phoneDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_restaurant_phone);
            Drawable locationDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_restaurant_location);
            Drawable promotionDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_restaurant_promotion);
            Drawable commentDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_restaurant_comment);

            setDrawable(mRestaurantPhone, phoneDrawable);
            setDrawable(mRestaurantLocation, locationDrawable);
            setDrawable(mRestaurantPromotion, promotionDrawable);
            setDrawable(mRestaurantComment, commentDrawable);
        }

        private void setDrawable(TextView textView, Drawable leftDrawable) {
            leftDrawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.restaurant_icon_width), (int)getResources().getDimension(R.dimen.restaurant_icon_height));
            textView.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.padding_normal));
            textView.setCompoundDrawables(leftDrawable, null, null, null);
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


        @AfterPermissionGranted(RC_PHONE)
        private void phoneCall() {
            if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CALL_PHONE)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);
            } else {
                EasyPermissions.requestPermissions(fragment, getResources().getString(R.string.phone_permission_explanation),
                        RC_PHONE, Manifest.permission.CALL_PHONE);
            }
        }
    }
}
