package com.mredrock.cyxbs.ui.fragment.explore;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.FoodDetail;
import com.mredrock.cyxbs.model.Shake;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.ui.activity.explore.BaseExploreActivity;
import com.mredrock.cyxbs.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbs.ui.activity.explore.WhatToEatActivity;
import com.mredrock.cyxbs.util.LogUtils;
import com.mredrock.cyxbs.util.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Stormouble on 16/4/27.
 */
public class WhatToEatFragment extends BaseExploreFragment implements SensorEventListener {

    private static final String TAG = LogUtils.makeLogTag(WhatToEatFragment.class);

    private static final int MIN_SHAKE_DURATION = 1000;
    private static final int MAIN_CONTENT_SCALE_DURATION = 200;
    private static final int SHAKE_FORCE = 350;

    @Bind(R.id.what_to_eat_container)
    FrameLayout mContainerLayout;

    private int[] mDrawingStartLocation;
    private long mLastTime;

    private Vibrator mVibrator;
    private SensorManager mSensorManager;

    private ImageView mShakePhoto;
    private ResultViewWrapper mResultViewWrapper;

    public WhatToEatFragment() {
        // Requires ic_empty public constructor
    }

    public static WhatToEatFragment newInstance(int[] startLocation) {
        WhatToEatFragment fragment = new WhatToEatFragment();
        Bundle bundle = new Bundle();
        bundle.putIntArray(SurroundingFoodActivity.ARG_DRAWING_START_LOCATION, startLocation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_what_to_eat;
    }

    @Override
    public void onRefresh() {
        getFood();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawingStartLocation = getArguments().getIntArray(BaseExploreActivity.ARG_DRAWING_START_LOCATION);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mShakePhoto = new ImageView(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mShakePhoto.setLayoutParams(layoutParams);
        mShakePhoto.setPadding((int) getResources().getDimension(R.dimen.padding_xxlarge),
                (int) getResources().getDimension(R.dimen.padding_xxlarge),
                (int) getResources().getDimension(R.dimen.padding_xxlarge),
                (int) getResources().getDimension(R.dimen.padding_xxlarge));
        mShakePhoto.setImageResource(R.drawable.img_shake);
        mShakePhoto.setAdjustViewBounds(true);
        mShakePhoto.setOnClickListener(v -> tryShake());
        mContainerLayout.addView(mShakePhoto);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        enableDisableSwipeRefresh(false);

        if (savedInstanceState == null) {
            startIntroAnimation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSensorManager != null) {
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        if (x * x + y * y + z * z > SHAKE_FORCE) {
            tryShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void tryShake() {
        if (System.currentTimeMillis() - mLastTime > MIN_SHAKE_DURATION) {
            mVibrator.vibrate(500);
            mLastTime = System.currentTimeMillis();

            Subscription subscription = RequestManager.getInstance().getShake(
                    new SimpleSubscriber<Shake>(getActivity(), new SubscriberListener<Shake>() {
                        @Override
                        public void onNext(Shake data) {
                            setFoodData(data);
                        }
                    }));

            mCompositeSubscription.add(subscription);
        }
    }

    private void setFoodData(final Shake data) {
        if (mResultViewWrapper == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.explore_shake_result, mContainerLayout, false);
            mResultViewWrapper = new ResultViewWrapper(contentView, data.id);

            mContainerLayout.removeView(mShakePhoto);
            mContainerLayout.addView(contentView);

            enableDisableSwipeRefresh(true);
        } 
        mResultViewWrapper.mRestaurantName.setText(data.name);
        mResultViewWrapper.mRestaurantAddress.setText(data.address);
        mGlideHelper.loadImage(data.img, mResultViewWrapper.mRestaurantImageView);
    }


    private void getFood() {
        Subscription subscription = RequestManager.getInstance().getFood(new SimpleSubscriber<>(getActivity(), new SubscriberListener<FoodDetail>() {
            @Override
            public void onCompleted() {
                onRefreshingStateChanged(false);
                onErrorLayoutVisibleChanged(mContainerLayout, false);
            }

            @Override
            public boolean onError(Throwable e) {
                onRefreshingStateChanged(false);
                onErrorLayoutVisibleChanged(mContainerLayout, true);
                return true;
            }

            @Override
            public void onNext(FoodDetail data) {
                mResultViewWrapper.mRestaurantName.setText(data.shop_name);
                mResultViewWrapper.mRestaurantAddress.setText(data.shop_address);
                mGlideHelper.loadImage(data.shop_image[0], mResultViewWrapper.mRestaurantImageView);
            }
        }), mResultViewWrapper.mRestaurantKey);

        mCompositeSubscription.add(subscription);
    }

    private void startIntroAnimation() {
        ViewCompat.setElevation(((WhatToEatActivity) getActivity()).getToolbar(), 0);
        mShakePhoto.setScaleX(0.1f);
        mShakePhoto.setScaleY(0.1f);
        mShakePhoto.setPivotX(mDrawingStartLocation[0]);
        mShakePhoto.setPivotY(mDrawingStartLocation[1]);

        mShakePhoto.animate()
                .scaleX(1.f)
                .scaleY(1.f)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(MAIN_CONTENT_SCALE_DURATION);

    }

    class ResultViewWrapper {
        @Bind(R.id.restaurant_photo)
        ImageView mRestaurantImageView;
        @Bind(R.id.restaurant_name)
        TextView mRestaurantName;
        @Bind(R.id.restaurant_location)
        TextView mRestaurantAddress;
        @Bind(R.id.shake_again)
        TextView mAgainText;
        
        @OnClick(R.id.restaurant_photo)
        public void onPhotoClick() {
            UIUtils.startAnotherFragment(WhatToEatFragment.this.getFragmentManager(), WhatToEatFragment.this,
                    SurroundingFoodDetailFragment.newInstance(mRestaurantKey),
                    R.id.what_to_eat_contentFrame);
        }

        @OnClick(R.id.shake_again)
        public void onShakeAgainClick() {
            tryShake();
        }
        
        String mRestaurantKey;
        
        public ResultViewWrapper(View contentView, String restaurantKey) {
            ButterKnife.bind(this, contentView);
            mRestaurantKey = restaurantKey;
        }
    }
}
