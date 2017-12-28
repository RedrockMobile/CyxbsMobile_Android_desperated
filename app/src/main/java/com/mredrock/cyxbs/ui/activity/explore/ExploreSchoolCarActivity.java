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
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.bumptech.glide.Glide;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.SchoolCarLocation;
import com.mredrock.cyxbs.model.User;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.activity.BaseActivity;
import com.mredrock.cyxbs.ui.widget.ExploreSchoolCarDialog;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ExploreSchoolCarActivity extends BaseActivity {

    private static final String TAG = "ExploreSchoolCar";
    public static final int TIME_OUT = 1;
    public static final int LOST_SERVICES = 2;
    public static final int NO_GPS = 3;

    private static final int HOLE_SCHOOL = 0;
    private static final int ME = 1;
    private static final int SCHOOL_CAR = 2;

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

//    private int i = 0;
//    List<LatLng> testList = setPoints();
    private Bundle savedInstanceState;
    private boolean firstEnter = true;
    private int locationStatus;
    private double carAngle;
    private Disposable disposable;
    private List<SchoolCarLocation.Data> dataList;
    private List<LatLng> smoothMoveList = new ArrayList<>();

    private AMap aMap;
    private MapView mapView;
    private ExploreSchoolCarDialog dialog = new ExploreSchoolCarDialog();
    private ImageButton  holeSchoolButton;
    private SmoothMoveMarker smoothMarker;
    private MyLocationStyle locationStyle;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationClientOption;
    private AMapLocationListener locationListener;


    public static void startSchoolCarActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, ExploreSchoolCarActivity.class);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        setContentView(R.layout.activity_explore_school_car);
        ButterKnife.bind(this);
        cheakActivityPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 2);
        cheakActivityPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);
//
//        loadCarLocation(savedInstanceState);
    }

    private boolean checkBeforeEnter(LatLng carLocation){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int AM_PM = calendar.get(Calendar.AM_PM);
        if(((AM_PM == Calendar.AM && hour < 11 )||(AM_PM == Calendar.PM && ((hour >1 && hour < 5) || (hour > 9))))) {
            dialog.show(this, TIME_OUT);
            return false;
        } else {
            float carDistance = AMapUtils.calculateLineDistance(new LatLng(29.531876 ,106.606789), carLocation);
            if (carDistance > 1300) {
                dialog.show(this, LOST_SERVICES);
                return false;
            }
        }
        return true;
    }

    private void initView(){
        dialog = new ExploreSchoolCarDialog();
        Glide.with(this ).load( R.drawable.ic_school_car_search_load).asGif().into(loadImage) ;
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }

        Observable.timer(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
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
                        loadCarLocation(55);
                    }
                });

    }

    private void initLocationType() {
        BitmapDescriptor descriptor;
        locationStyle = new MyLocationStyle();

        User user = BaseAPP.getUser(this);
        if (user.gender.equals("女")){
            descriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_school_car_search_girl);
        } else {
            descriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_school_car_search_boy);
        }
        locationStyle.interval(2000);
        locationStyle.strokeWidth(0);
        locationStyle.radiusFillColor(Color.alpha(0));
        locationStyle.myLocationIcon(descriptor);
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        }

    private void initAMap(){

        aMap = mapView.getMap();
        
        if (aMap == null) {
            aMap = mapView.getMap();
            Log.d(TAG, "initAMap: .........");
            if (mapView == null){
                Log.d(TAG, "initAMap: gggggggggggggg");
            }
        }

        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(locationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(29.531876 ,106.606789), 17f);
        aMap.animateCamera(update);
        smoothMarker = new SmoothMoveMarker(aMap);
    }

    private void initData(){
        locationListener = aMapLocation -> {
            if (firstEnter) {
                float myDistance = AMapUtils.calculateLineDistance(new LatLng(29.531876, 106.606789), new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                if (myDistance > 1300) {
                    aMap.setMyLocationEnabled(false);
                }
            }
        };
        locationClient = new AMapLocationClient(getApplicationContext());
        locationClientOption = new AMapLocationClientOption();

        locationClient.setLocationOption(locationClientOption);
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }


    private void showMap(String status) {
        if (status.equals("200")) {
            loadImage.setVisibility(View.INVISIBLE);
            if(mapView == null) {
                mapView = new MapView(this);
                layout.addView(mapView);
                mapView.onCreate(savedInstanceState);
            }

            RelativeLayout relativeLayout  = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.leftMargin = 40;
            layoutParams.topMargin = 0;
            layoutParams.rightMargin =25;
            layout.addView(relativeLayout, layoutParams);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_orgnization_logo));
            RelativeLayout.LayoutParams lpLogo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpLogo.topMargin = 45;
            relativeLayout.addView(imageView, lpLogo);

            RelativeLayout relativeLayoutDown  = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutParamsDown = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(relativeLayoutDown, layoutParamsDown);

            holeSchoolButton = new ImageButton(this);
            holeSchoolButton.setBackgroundColor(Color.TRANSPARENT);
            holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_hole_school));
            locationStatus = HOLE_SCHOOL;
            holeSchoolButton.setOnClickListener(v -> {
                switch (locationStatus) {
                    case HOLE_SCHOOL:
                        holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_me));
                        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
                        aMap.setMyLocationStyle(locationStyle);
                        locationStatus = ME;
                        break;
                    case ME:
                        holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_car_button));
                        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
                        aMap.setMyLocationStyle(locationStyle);
                        locationStatus = SCHOOL_CAR;
                        break;
                    case  SCHOOL_CAR:
                        holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_hole_school));
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(29.531876 ,106.606789), 17f);
                        aMap.animateCamera(update);
                        locationStatus = HOLE_SCHOOL;
                        break;
                }
            });

            RelativeLayout.LayoutParams lPHoleSchool = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lPHoleSchool.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lPHoleSchool.rightMargin = 25;
            lPHoleSchool.topMargin = 0;
            relativeLayoutDown.addView(holeSchoolButton, lPHoleSchool);

            initLocationType();
            initAMap();
            initData();
        } else {
            Toast.makeText(this, "校车暂时不在线哟～", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadCarLocation(long aLong) {
        RequestManager.INSTANCE.getSchoolCarLocation(new Observer<SchoolCarLocation>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(SchoolCarLocation schoolCarLocation) {
//                float carDistance = 0;
                dataList = schoolCarLocation.getData();
                if (checkBeforeEnter(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLon())) && firstEnter && aLong == 3) {
                    timer();
                }

                if (firstEnter && aLong > 5) {
                    showMap(schoolCarLocation.getStatus());
                    firstEnter = false;
                }

                if (!firstEnter && locationStatus == SCHOOL_CAR){
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLon()), 17f);
                    aMap.animateCamera(update);
                }
//
                smoothMoveList.add(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLon()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void carSmoothMove(List<LatLng> smoothMoveList) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) 150) / width;
        float scaleHeight = ((float) 75) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmapChanged = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

//        smoothMarker = new SmoothMoveMarker(aMap);
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromBitmap(bitmapChanged));
        if(smoothMoveList.size() > 1) {
            changeCarOrientation(smoothMoveList.get(smoothMoveList.size() - 4), smoothMoveList.get(smoothMoveList.size() - 3), 2);
            smoothMarker.setPoints(smoothMoveList.subList(smoothMoveList.size() - 4, smoothMoveList.size() - 3));
            smoothMarker.setTotalDuration(1);
            smoothMarker.startSmoothMove();
            drawTraceLine();
        }
    }

    private void drawTraceLine(){
        Polyline polyline =aMap.addPolyline(new PolylineOptions().
                addAll(smoothMoveList.subList(0, smoothMoveList.size() - 1)).width(8).color(Color.argb(255, 93,152,255)));
    }

    public void changeCarOrientation(LatLng latlng1, LatLng latlng2, double errorRange) {

        if (!(latlng1.latitude == latlng2.latitude && latlng1.longitude == latlng2.longitude)) {
            double nextOrientation = getNextOrientation(latlng1, latlng2);
            double currentAngle = carAngle;
            if (Math.abs(nextOrientation - currentAngle) > errorRange) {
                carAngle = nextOrientation;
                float computAngle = (float) computRotateAngle(currentAngle, nextOrientation);
                smoothMarker.getMarker().setRotateAngle(smoothMarker.getMarker().getRotateAngle() + computAngle);
            }
        }
    }

    public double getNextOrientation(LatLng latlng1, LatLng latlng2) {
        double angle;
        double a, b, c, a1, b1;

        a1 = latlng2.latitude - latlng1.latitude;
        b1 = latlng2.longitude - latlng1.longitude;

        double latitudeDif = Math.abs(a1);
        double longitudeDif = Math.abs(b1);
        a = latitudeDif;
        b = longitudeDif;
        c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        if (a == 0) {
            if ((latlng2.longitude - latlng1.longitude) < 0) {// 经度；
                angle = 180;
            } else {
                angle = 0;
            }
        } else if (b == 0) {
            if ((latlng2.latitude - latlng1.latitude) < 0) {// 纬度；
                angle = 270;
            } else {
                angle = 90;
            }
        } else {
            angle = Math.toDegrees(Math.acos(b / c));
            if (a1 < 0 && b1 > 0) {
                angle = 360 - angle;
            } else if (a1 > 0 && b1 > 0) {

            } else if (a1 > 0 && b1 < 0) {
                angle = 180 - angle;
            } else if (a1 < 0 && b1 < 0) {
                angle = 180 + angle;
            }

        }
        return angle;
    }

    private double computRotateAngle(double currentAngle, double nextAngle) {
        return nextAngle - currentAngle;

    }

    private void timer(){
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(aLong -> {
                    if (!firstEnter) {
                        carSmoothMove(smoothMoveList);
                    }
                    loadCarLocation(0);
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
                    public void onComplete() {}
                });
    }

    @OnClick(R.id.explore_schoolcar_toolbar_learnmore)
    public void learnMore(View v) {
        if (v.getId() == R.id.explore_schoolcar_toolbar_learnmore) {
            Intent intent = new Intent(this, SchoolCarLearnMoreActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.explore_schoolcar_toolbar_back)
    public void back(View v){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
            if (locationClient != null) {
                locationClient.onDestroy();
            }
        }
        if (disposable != null) {
            disposable.dispose();
        }
        if (dialog != null) {
            dialog.cancleDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
        if (dialog != null) {
            dialog.cancleDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    public void cheakActivityPermission (String permission, int processingMethod) {
        if (permission == null) {
            Toast.makeText(this, "No permissions are passed in", Toast.LENGTH_SHORT).show();
            throw new RuntimeException("The corresponding permission access failed");
        }
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, processingMethod);
        } else {
            switch (processingMethod) {
                case 1:
                    initView();
                    loadCarLocation(3);
                    break;
                case 2:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                initView();
                loadCarLocation(3);
            }
        } else {
            if (dialog == null) {
                dialog = new ExploreSchoolCarDialog();
            }
            dialog.show(this, NO_GPS);
        }
    }
}
//    public List<LatLng> setPoints(){
//        List<LatLng> schoolCarTrace = new ArrayList<>();
//
//        schoolCarTrace.add(new LatLng(29.532769,106.604673));
//        schoolCarTrace.add(new LatLng(29.532811,106.604631));
//        schoolCarTrace.add(new LatLng(29.532839,106.604636));
//        schoolCarTrace.add(new LatLng(29.532886,106.604636));
//        schoolCarTrace.add(new LatLng(29.532937,106.604641));
//        schoolCarTrace.add(new LatLng(29.534244,106.605457));
//        schoolCarTrace.add(new LatLng(29.535402,106.605462));
//        schoolCarTrace.add(new LatLng(29.535369,106.605585));
//        schoolCarTrace.add(new LatLng(29.535323,106.608112));
//        schoolCarTrace.add(new LatLng(29.534534,106.608139));
//        schoolCarTrace.add(new LatLng(29.534539,106.608922));
//        schoolCarTrace.add(new LatLng(29.534515,106.609727));
//        schoolCarTrace.add(new LatLng(29.534529,106.610076));
//        schoolCarTrace.add(new LatLng(29.535944,106.610102));
//        schoolCarTrace.add(new LatLng(29.535892,106.610633));
//        schoolCarTrace.add(new LatLng(29.535888,106.610671));
//        schoolCarTrace.add(new LatLng(29.535855,106.610714));
//        schoolCarTrace.add(new LatLng(29.535804,106.610762));
//        schoolCarTrace.add(new LatLng(29.534161,106.610751));
//        schoolCarTrace.add(new LatLng(29.534044,106.610772));
//        schoolCarTrace.add(new LatLng(29.533946,106.610896));
//        schoolCarTrace.add(new LatLng(29.533862,106.611143));
//        schoolCarTrace.add(new LatLng(29.533811,106.611314));
//        schoolCarTrace.add(new LatLng(29.533746,106.611395));
//        schoolCarTrace.add(new LatLng(29.533232,106.611384));
//        schoolCarTrace.add(new LatLng(29.533059,106.611357));
//        schoolCarTrace.add(new LatLng(29.531188,106.611373));
//        schoolCarTrace.add(new LatLng(29.531104,106.611320));
//        schoolCarTrace.add(new LatLng(29.531085,106.611234));
//        schoolCarTrace.add(new LatLng(29.531099,106.610617));
//        schoolCarTrace.add(new LatLng(29.531085,106.609769));
//        schoolCarTrace.add(new LatLng(29.531043,106.609356));
//        schoolCarTrace.add(new LatLng(29.531015,106.609206));
//        schoolCarTrace.add(new LatLng(29.530954,106.609099));
//        schoolCarTrace.add(new LatLng(29.530880,106.609034));
//        schoolCarTrace.add(new LatLng(29.530810,106.609008));
//        schoolCarTrace.add(new LatLng(29.530698,106.608965));
//        schoolCarTrace.add(new LatLng(29.530497,106.608970));
//        schoolCarTrace.add(new LatLng(29.528975,106.608943));
//        schoolCarTrace.add(new LatLng(29.528971,106.608954));
//        schoolCarTrace.add(new LatLng(29.528961,106.608101));
//        schoolCarTrace.add(new LatLng(29.529055,106.608106));
//        schoolCarTrace.add(new LatLng(29.529288,106.608149));
//        schoolCarTrace.add(new LatLng(29.529839,106.608214));
//        schoolCarTrace.add(new LatLng(29.530544,106.608230));
//        schoolCarTrace.add(new LatLng(29.530651,106.607495));
//        schoolCarTrace.add(new LatLng(29.530632,106.606953));
//        schoolCarTrace.add(new LatLng(29.529344,106.606937));
//        schoolCarTrace.add(new LatLng(29.529325,106.604512));
//        schoolCarTrace.add(new LatLng(29.529358,106.604394));
//        schoolCarTrace.add(new LatLng(29.529386,106.604314));
//        schoolCarTrace.add(new LatLng(29.529428,106.604287));
//        schoolCarTrace.add(new LatLng(29.529480,106.604276));
//        schoolCarTrace.add(new LatLng(29.530208,106.604244));
//        schoolCarTrace.add(new LatLng(29.530544,106.604239));
//        schoolCarTrace.add(new LatLng(29.531795,106.604330));
//        schoolCarTrace.add(new LatLng(29.532682,106.604641));
//        schoolCarTrace.add(new LatLng(29.532756,106.604673));
//
//        return schoolCarTrace;
//    }

