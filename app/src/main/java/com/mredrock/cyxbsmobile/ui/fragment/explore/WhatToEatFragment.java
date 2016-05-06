package com.mredrock.cyxbsmobile.ui.fragment.explore;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.FoodDetail;
import com.mredrock.cyxbsmobile.model.Shake;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.subscriber.SimpleSubscriber;
import com.mredrock.cyxbsmobile.subscriber.SubscriberListener;
import com.mredrock.cyxbsmobile.ui.activity.explore.BaseExploreActivity;
import com.mredrock.cyxbsmobile.ui.activity.explore.SurroundingFoodActivity;
import com.mredrock.cyxbsmobile.ui.activity.explore.WhatToEatActivity;
import com.mredrock.cyxbsmobile.util.LogUtils;
import com.mredrock.cyxbsmobile.util.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Stormouble on 16/4/27.
 */
public class WhatToEatFragment extends BaseExploreFragment
        implements SensorEventListener, BaseExploreFragment.Listener {

    private static final String TAG = LogUtils.makeLogTag(WhatToEatFragment.class);

    private static final int MAIN_CONTENT_SCALE_DURATION = 200;

    private static final int SHAKE_FORCE = 350;

    private static final int MIN_SHAKE_DURATION = 1000;

    @Bind(R.id.shake_photo)
    ImageView mShakeImageView;
    @Bind(R.id.result_stub)
    ViewStub mShakeResultLayout;

    private int[] mDrawingStartLocation;
    private String mRestaurantKey;
    private long mLastTime;
    private boolean mIsResultLayoutInflate = false;

    private ImageView mRestaurantImageView;
    private TextView mRestaurantName;
    private TextView mRestaurantAddress;
    private TextView mRestaurantAgain;

    private Vibrator mVibrator;
    private SensorManager mSensorManager;

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

    @OnClick(R.id.shake_photo)
    public void onClickShakeImage() {
        tryShake();
    }

    @Override
    public int getLayoutID() {
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }

    @Override
    public void onFragmentSetup() {
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        enableDisableSwipeRefresh(false);
    }

    @Override
    public void onFragmentIntroAnimation(Bundle savedInstanceState) {
        ViewCompat.setElevation(((WhatToEatActivity) getActivity()).getToolbar(), 0);
        mMainContent.setScaleX(0.1f);
        mMainContent.setScaleY(0.1f);
        mMainContent.setPivotX(mDrawingStartLocation[0]);
        mMainContent.setPivotY(mDrawingStartLocation[1]);

        mMainContent.animate()
                .scaleX(1.f)
                .scaleY(1.f)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(MAIN_CONTENT_SCALE_DURATION);
    }

    @Override
    public void onFragmentLoadData(Bundle savedInstanceState) {
        //No need
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
                        public void onCompleted() {
                            enableDisableSwipeRefresh(true);
                        }

                        @Override
                        public void onNext(Shake data) {
                            setFoodData(data);
                        }
                    }));

            mCompositeSubscription.add(subscription);
        }
    }

    private void setFoodData(final Shake data) {
        mRestaurantKey = data.id;

        if (!mIsResultLayoutInflate) {
            mShakeImageView.setVisibility(View.GONE);

            final View contentView = mShakeResultLayout.inflate();
            mRestaurantImageView = (ImageView) contentView.findViewById(R.id.restaurant_photo);
            mRestaurantName = (TextView) contentView.findViewById(R.id.restaurant_name);
            mRestaurantAddress = (TextView) contentView.findViewById(R.id.restaurant_location);
            mRestaurantAgain = (TextView) contentView.findViewById(R.id.shake_again);

            mIsResultLayoutInflate = true;
        }

        mRestaurantName.setText(data.name);
        mRestaurantAddress.setText(data.address);
        mGlideHelper.loadImage(data.img, mRestaurantImageView);
        mRestaurantAgain.setOnClickListener(v -> tryShake());
        mRestaurantImageView.setOnClickListener(v ->
                UIUtils.startAnotherFragment(getFragmentManager(), WhatToEatFragment.this,
                        SurroundingFoodDetailFragment.newInstance(data.id),
                        R.id.what_to_eat_contentFrame));
    }

    private void getFood() {
        Subscription subscription = RequestManager.getInstance().getFood(new SimpleSubscriber<FoodDetail>(getActivity(), new SubscriberListener<FoodDetail>() {
            @Override
            public void onCompleted() {
                onRefreshingStateChanged(false);
            }

            @Override
            public void onError(Throwable e) {
                onRefreshingStateChanged(false);
            }

            @Override
            public void onNext(FoodDetail data) {
                mRestaurantName.setText(data.shop_name);
                mRestaurantAddress.setText(data.shop_address);
                mGlideHelper.loadImage(data.shop_image[0], mRestaurantImageView);
            }
        }), mRestaurantKey);

        mCompositeSubscription.add(subscription);
    }
}
