package com.mredrock.cyxbs.ui.activity.explore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.SchoolCarLocation;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.activity.Interface.SchoolCarInterface;
import com.mredrock.cyxbs.ui.widget.ExploreSchoolCarDialog;
import com.mredrock.cyxbs.ui.widget.SchoolCarMap;
import com.mredrock.cyxbs.ui.widget.SchoolcarsSmoothMove;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by glossimar on 2018/1/29.
 */

public class ExploreSchoolCarActivity extends BaseActivity {
    private static final String TAG = "ExploreSchoolCar";
    public static final int TIME_OUT = 1;
    public static final int LOST_SERVICES = 2;
    public static final int NO_GPS = 3;

    public static final int HOLE_SCHOOL = 0;
    public static final int ME = 1;
    public static final int SCHOOL_CAR = 2;

    @BindView(R.id.explore_schoolcar_load)
    ImageView loadImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.explore_schoolcar_linearLayout)
    RelativeLayout layout;
    @BindView(R.id.explore_schoolcar_toolbar_back)
    ImageView back;
    @BindView(R.id.explore_schoolcar_toolbar_learnmore)
    ImageButton learnMore;

    private int locationStatus;
    public boolean ifLocation = true;
    private boolean firstEnter = true;

    private Bundle savedInstanceState;
    private Bitmap makerBitmap;
    private ImageButton holeSchoolButton;
    private SchoolCarMap schoolCarMap;
    private SchoolcarsSmoothMove smoothMoveData;
    private List<SmoothMoveMarker> smoothMoveMarkers;

    private AMapLocationClient locationClient;
    private Disposable disposable;
    private List<SchoolCarLocation.Data> dataList;

    private ExploreSchoolCarDialog dialog = new ExploreSchoolCarDialog();


    public static void startSchoolCarActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, ExploreSchoolCarActivity.class);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        initSchoolCarMap();

        setContentView(R.layout.activity_explore_school_car);
        ButterKnife.bind(this);
        checkActivityPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);
        checkActivityPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 2);
    }

    private boolean checkBeforeEnter(LatLng carLocation) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int AM_PM = calendar.get(Calendar.AM_PM);
//        Log.d(TAG, "checkBeforeEnter: .....,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," + String.valueOf(((AM_PM == Calendar.AM && hour < 11) || (AM_PM == Calendar.PM && ((hour > 1 && hour < 5) || (hour > 9))))));
        if (((AM_PM == Calendar.AM && hour < 11) || (AM_PM == Calendar.PM && ((hour > 1 && hour < 5) || (hour > 9))))) {
            dialog.show(this, TIME_OUT);
            return false;
        } else {
            if (carLocation != null) {
                float carDistance = AMapUtils.calculateLineDistance(new LatLng(29.531876, 106.606789), carLocation);
                if (carDistance > 1300) {
//                    dialog.show(this, LOST_SERVICES);
                    return false;
                }
            }
        }
        return true;
    }

    private void initSchoolCarMap() {
        schoolCarMap = new SchoolCarMap(this, savedInstanceState, new SchoolCarInterface() {
            @Override
            public void initLocationMapButton(AMap aMap, MyLocationStyle locationStyle) {
                RelativeLayout relativeLayoutDown = new RelativeLayout(ExploreSchoolCarActivity.this);
                RelativeLayout.LayoutParams layoutParamsDown = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.addView(relativeLayoutDown, layoutParamsDown);

                holeSchoolButton = new ImageButton(ExploreSchoolCarActivity.this);
                holeSchoolButton.setBackgroundColor(Color.TRANSPARENT);
                holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_hole_school));
                locationStatus = ExploreSchoolCarActivity.HOLE_SCHOOL;
                holeSchoolButton.setOnClickListener((View v) -> {
                    switch (locationStatus) {
                        case ExploreSchoolCarActivity.HOLE_SCHOOL:
                            holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_me));
                            if (ifLocation) {
                                locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
                                aMap.setMyLocationStyle(locationStyle);
                            }
                            locationStatus = ME;
                            break;
                        case ME:
                            holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_hole_school));
                            if (ifLocation) {
                                locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
                                aMap.setMyLocationStyle(locationStyle);
                            }
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(29.531876, 106.606789), 17f);
                            aMap.animateCamera(update);
                            locationStatus = HOLE_SCHOOL;
                            break;
//                        case SCHOOL_CAR:
//                            holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_hole_school));
//                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(29.531876, 106.606789), 17f);
//                            aMap.animateCamera(update);
//                            locationStatus = HOLE_SCHOOL;
//                            break;
                    }
                });

                RelativeLayout.LayoutParams lPHoleSchool = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lPHoleSchool.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lPHoleSchool.rightMargin = 25;
                lPHoleSchool.topMargin = 0;
                relativeLayoutDown.addView(holeSchoolButton, lPHoleSchool);
                if (ifLocation) {
                    schoolCarMap.initLocationType();
                    initData();
                }
                schoolCarMap.initAMap(ifLocation);
            }

            @Override
            public void processLocationInfo(SchoolCarLocation carLocationInfo, long aLong, int carID) {

            }
        });
    }

    private void initView() {
        dialog = new ExploreSchoolCarDialog();
        makerBitmap = getSmoothMakerBitmap();
        smoothMoveData = new SchoolcarsSmoothMove(schoolCarMap);
        smoothMoveData.setCarMapInterface(new SchoolCarInterface() {
            @Override
            public void initLocationMapButton(AMap aMap, MyLocationStyle locationStyle) {
            }

            @Override
            public void processLocationInfo(SchoolCarLocation carLocationInfo, long aLong, int carID) {
                dataList = carLocationInfo.getData();
                if (aLong == 3) {
                    timer();
                }

                if (firstEnter && aLong > 5) {
                    Log.d(TAG, "processLocationInfo: aaaaaaaaaaa");
                    if (disposable != null) disposable.dispose();
                    timer();
                    schoolCarMap.showMap(carLocationInfo.getStatus(), layout, loadImage);
                    firstEnter = false;
                }

//                if (locationStatus == SCHOOL_CAR) {
//                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(dataList.get(1).getLat(), dataList.get(1).getLon()), 17f);
//                    schoolCarMap.getaMap().animateCamera(update);
//                }
            }
        });

//        Glide.with(this ).load( R.drawable.ic_school_car_search_load).asGif().into(loadImage) ;
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }

        Observable.timer(10, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        loadImage.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onNext: 校车正在初始化..." + aLong);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        smoothMoveMarkers = new ArrayList<>();
                        smoothMoveData.checkBeforeEnter(ExploreSchoolCarActivity.this, dialog);
//                       dialog for (int i = 0; i < 3; i ++) {
                        smoothMoveData.loadCarLocation(55, 0);
//                        }
                    }
                });
    }



    private void initData() {
        AMapLocationListener locationListener = aMapLocation -> {
            if (firstEnter) {
                float myDistance = AMapUtils.calculateLineDistance(new LatLng(29.531876, 106.606789), new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                if (myDistance > 1300) {
                    schoolCarMap.getaMap().setMyLocationEnabled(false);
                }
            }
        };
        locationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();

        locationClient.setLocationOption(locationClientOption);
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    private void timer() {
        Observable.interval(2, TimeUnit.SECONDS)
                .doOnNext(aLong -> {
//                    Log.d(TAG, "timer:  + smoothMoveMarkers.size" + String.valueOf( smoothMoveMarkers.size() != 0 && smoothMoveMarkers != null));
                    if (smoothMoveMarkers != null) {
                        for (int i = 0; i < smoothMoveMarkers.size(); i++) {
                            smoothMoveMarkers.get(i).removeMarker();
                        }
                    }
                    smoothMoveMarkers = new ArrayList<>();
                    for (int i = 0; i < dataList.size() && dataList.get(i).getLat() != 0; i++) {
                        if (!firstEnter) {
                            smoothMoveData.smoothMove(smoothMoveMarkers, makerBitmap);
                        }
                        smoothMoveData.loadCarLocation(0, i + 1);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private Bitmap getSmoothMakerBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(ExploreSchoolCarActivity.this.getResources(), R.drawable.ic_school_car);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) 70) / width;
        float scaleHeight = ((float) 140) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap bitmapChanged = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return  bitmapChanged;
    }

    @OnClick(R.id.explore_schoolcar_toolbar_learnmore)
    public void learnMore(View v) {
        if (v.getId() == R.id.explore_schoolcar_toolbar_learnmore) {
            Intent intent = new Intent(this, SchoolCarLearnMoreActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.explore_schoolcar_toolbar_back)
    public void back(View v) {
        finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (schoolCarMap.getMapView() != null) {
            schoolCarMap.getMapView().onSaveInstanceState(outState);
        }
    }

    public void checkActivityPermission(String permission, int processingMethod) {
        if (permission == null) {
            Toast.makeText(this, "No permissions are passed in", Toast.LENGTH_SHORT).show();
            throw new RuntimeException("The corresponding permission access failed");
        }
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, processingMethod);
        } else {
            switch (processingMethod) {
                case 1:
                    break;
                case 2:
                    initView();
//                    for (int i = 0; i < 3; i++)
                    smoothMoveData.loadCarLocation(3, 0);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 1:
                    break;
                case 2:
//                    checkBeforeEnter(null);
                    initView();
//                    for (int i = 0; i < 3; i++)
//                        smoothMoveData.loadCarLocation(3, i + 1);
                    smoothMoveData.loadCarLocation(3, 0);
                    break;
            }
        } else {
            switch (requestCode) {
                case 1:
                    ifLocation = false;
                    break;
                case 2:
                    if (dialog == null) {
                        dialog = new ExploreSchoolCarDialog();
                    }
                    dialog.show(this, NO_GPS);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.cancleDialog();
        }
        if (schoolCarMap != null) {
            schoolCarMap.distroyMap(locationClient);
        }
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.cancleDialog();
        }
        if (schoolCarMap != null) {
            schoolCarMap.pauseMap();
        }
        if (disposable != null) {
            if (disposable.isDisposed()){
                Log.d(TAG, "onRestart: disposed!!!" );
            } else {
                Log.d(TAG, "onRestart: not disposed!!!");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (schoolCarMap != null) {
            schoolCarMap.resumeMap();
        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        smoothMoveData.clearAllList();
    }
}
