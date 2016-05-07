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
import com.mredrock.cyxbsmobile.component.widget.TextDrawableView;
import com.mredrock.cyxbsmobile.model.FoodComment;
import com.mredrock.cyxbsmobile.model.FoodDetail;
import com.mredrock.cyxbsmobile.model.User;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.adapter.HeaderViewRecyclerAdapter;
import com.mredrock.cyxbsmobile.ui.adapter.RestaurantCommentsAdapter;
import com.mredrock.cyxbsmobile.util.LogUtils;
import com.mredrock.cyxbsmobile.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Stormouble on 16/4/16.
 */
public class SurroundingFoodDetailFragment extends BaseExploreFragment
        implements BaseExploreFragment.Listener {

    private static final String TAG = LogUtils.makeLogTag(SurroundingFoodDetailFragment.class);

    private static final String RESTAURANT_KEY = "shop_key";

    private static final int PRELOAD_SIZE = 3;

    private static final long ANIMATION_DURATION = 700;

    @Bind(R.id.surrounding_food_detail_rv)
    RecyclerView mFoodDetailRv;

    private LinearLayout mHeaderLayout;
    private ImageView mFoodRestaurantImage;
    private TextView mFoodRestaurantName;
    private TextView mFoodRestaurantRecommend;
    private TextDrawableView mFoodRestaurantPhone;
    private TextDrawableView mFoodRestaurantLocation;
    private TextDrawableView mFoodRestaurantPromotion;
    private TextDrawableView mFoodRestaurantComment;
    private FloatingActionButton mFloatingActionButton;

    private int mPage = 1;
    private int mLastItemPosition;
    private String mRestaurantKey;
    private String mPhoneNumber;
    private boolean mFirstTimeTouchBottom = false;

    private List<FoodComment> mComments = new ArrayList<>();
    private HeaderViewRecyclerAdapter mWrapperAdapter;
    private RestaurantCommentsAdapter mAdapter;


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
    public int getLayoutID() {
        return R.layout.fragment_surrounding_food_detail;
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        getFoodAndCommentList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestaurantKey = getArguments().getString(RESTAURANT_KEY);

        Log.d(TAG, "Restaurant Key " + mRestaurantKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHeaderLayout =
                (LinearLayout) inflater.inflate(R.layout.food_detail_header, container, false);
        mFloatingActionButton =
                (FloatingActionButton) getActivity().findViewById(R.id.fab);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFragmentSetup() {
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
        mFoodDetailRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!mSwipeRefreshLayout.isRefreshing()
                        && mLastItemPosition >= mAdapter.getItemCount() - PRELOAD_SIZE) {
                    if (!mFirstTimeTouchBottom) {
                        mPage++;
                        mSwipeRefreshLayout.setRefreshing(true);
                        getFoodAndCommentList();
                    } else {
                        mFirstTimeTouchBottom = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mLastItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });


        mFloatingActionButton.setVisibility(View.VISIBLE);
        mFloatingActionButton.setImageResource(R.drawable.ic_add);
        mFloatingActionButton.setOnClickListener(v -> onFabClick());
    }

    @Override
    public void onFragmentIntroAnimation(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
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
                            getFoodAndCommentList();
                        }
                    });
        } else {
            mAdapter.updateDataWithAnimation();
        }
    }


    @Override
    public void onFragmentLoadData(Bundle savedInstanceState) {
        //Load data on animation end;
    }

    private void getFoodAndCommentList() {
        Subscription subscription = RequestManager.getInstance().getFoodAndCommentList(
                new SimpleSubscriber<FoodDetail>(getActivity(), new SubscriberListener<FoodDetail>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
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
                    public void onNext(FoodDetail foodDetail) {
                        mPhoneNumber = foodDetail.shop_tel;

                        mFoodRestaurantName.setText(foodDetail.shop_name);
                        mFoodRestaurantLocation.setText(foodDetail.shop_address);
                        mFoodRestaurantPhone.setText(foodDetail.shop_tel);
                        mFoodRestaurantRecommend.setText(foodDetail.shop_sale_content);
                        mGlideHelper.loadImage(foodDetail.shop_image[0], mFoodRestaurantImage);

                        if (Utils.checkNotNullAndNotEmpty(foodDetail.foodComments)) {
                            if (mPage == 1) {
                                mAdapter.updateData(foodDetail.foodComments);
                            } else {
                                mAdapter.addData(foodDetail.foodComments);
                            }
                        }
                    }

                }), mRestaurantKey, String.valueOf(mPage));

        mCompositeSubscription.add(subscription);
    }

    private void getFoodCommentList() {
        Subscription subscription = RequestManager.getInstance().getFoodCommentList(
                new SimpleSubscriber<List<FoodComment>>(getActivity(), new SubscriberListener<List<FoodComment>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(List<FoodComment> foodCommentList) {
                        super.onNext(foodCommentList);
                    }
                }), mRestaurantKey, String.valueOf(mPage));

        mCompositeSubscription.add(subscription);
    }

    private void sendCommentAndRefresh(String content) {
        User user = APP.getUser(getActivity());
        if (user != null) {
            Subscription subscription = RequestManager.getInstance().sendCommentAndRefresh(
                    new SimpleSubscriber<List<FoodComment>>(getActivity(), true, new SubscriberListener<List<FoodComment>>() {
                        @Override
                        public void onNext(List<FoodComment> foodCommentList) {

                        }
                    }), mRestaurantKey, user.stuNum, user.idNum, content, user.name);

            mCompositeSubscription.add(subscription);
        }
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
