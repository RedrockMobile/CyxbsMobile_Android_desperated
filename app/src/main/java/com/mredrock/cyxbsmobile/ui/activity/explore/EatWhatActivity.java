package com.mredrock.cyxbsmobile.ui.activity.explore;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mredrock.cyxbsmobile.R;
import com.mredrock.cyxbsmobile.model.EatWhat;
import com.mredrock.cyxbsmobile.network.RequestManager;
import com.mredrock.cyxbsmobile.ui.activity.BaseActivity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class EatWhatActivity extends BaseActivity implements SensorEventListener {
    private static final int SHAKE_FORCE = 350;

    private static final int MIN_SHAKE_DURATION = 1000;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.eat_what_shake_image_view)
    ImageView mShakeImageView;

    //上一次摇一摇的时间
    private long mLastTime;

    private Vibrator mVibrator;
    private SensorManager mSensorManager;

    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        if (x * x + y * y + z * z > SHAKE_FORCE) {
            tryShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_what);
        ButterKnife.bind(this);

        initToolbar(mToolbar);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    @OnClick(R.id.eat_what_shake_image_view)
    void clickShakeImage() {
        tryShake();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        Log.d("TAG", "Here is work");
        super.onTitleChanged(title, color);
        if (mToolbar != null) {
            mToolbarTitle.setText(title);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager != null) {
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mCompositeSubscription.clear();
    }

    private void tryShake() {
        if (System.currentTimeMillis() - mLastTime > MIN_SHAKE_DURATION) {
            mVibrator.vibrate(500);
            mLastTime = System.currentTimeMillis();

            Subscription subscription = RequestManager.getInstance()
                    .getEatWhat(new Subscriber<EatWhat>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(EatWhatActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(EatWhat eatWhat) {
                    setData(eatWhat);
                }
            });

            mCompositeSubscription.add(subscription);
        }
    }

    private void setData(final EatWhat data) {
        final View contentView = LayoutInflater.from(this)
                .inflate(R.layout.layout_eat_what_shake_item, null);
        setContentView(contentView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        ImageView shopImage = (ImageView) findViewById(R.id.item_eat_what_shake_result_img);
        TextView shopName = (TextView) findViewById(R.id.item_eat_what_shake_result_name);
        TextView shopAddress = (TextView) findViewById(R.id.item_eat_what_shake_result_address);
        TextView shakeAgain = (TextView) findViewById(R.id.item_eat_what_shake_result_again);

        initToolbar(toolbar);
        if (toolbarTitle != null) {
            toolbarTitle.setText(getResources().getString(R.string.eat_what_activity_name));
        }
        if (shopName != null) {
            shopName.setText(data.name);
        }
        if (shopAddress != null) {
            shopAddress.setText(data.address);
        }
        if (shopImage != null) {
            Picasso.with(this)
                    .load(data.img)
                    .into(shopImage);
        }

        if (shakeAgain != null) {
            shakeAgain.setOnClickListener(v -> tryShake());
        }
        contentView.setOnClickListener(v -> {
            Intent i = new Intent(EatWhatActivity.this, AroundDishesActivity.class);
            i.putExtra("id", data.id);
            startActivity(i);
        });
    }


    private void initToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
