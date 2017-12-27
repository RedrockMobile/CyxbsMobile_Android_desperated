package com.mredrock.cyxbs.ui.activity.explore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.fence.GeoFenceClient;
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
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.model.SchoolCarLocation;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.widget.ExploreSchoolCarDialog;

import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ExploreSchoolCar extends AppCompatActivity {

    private static final String TAG = "ExploreSchoolCar";
    public static final int TIME_OUT = 1;
    public static final int LOST_SERVICES = 2;
    public static final int NO_GPS = 3;

    @BindView(R.id.explore_schoolcar_load)
    ImageView loadImage;
    @BindView(R.id.explore_schoolcar_toolbar)
    Toolbar toolbar;
    @BindView(R.id.explore_schoolcar_linearLayout)
    RelativeLayout layout;
    @BindView(R.id.explore_schoolcar_toolbar_back)
    ImageView back;
    @BindView(R.id.explore_schoolcar_toolbar_learnmore)
    ImageButton learnMore;

    boolean firstEnter = true;
    int locationStatus;
    Timer timer;
    Task task;
    List<SchoolCarLocation.Data> dataList;
    List<LatLng> smoothMoveList = new ArrayList<>();

    AMap aMap;
    MapView mapView;
    ImageButton  holeSchoolButton;
    SmoothMoveMarker smoothMarker;
    MyLocationStyle locationStyle;
    AMapLocationClient locationClient;
    GeoFenceClient geoFenceClient;
    AMapLocationClientOption locationClientOption;
    AMapLocationListener locationListener;


    public static void startSchoolCarActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, ExploreSchoolCar.class);
        startingActivity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_school_car);
        ButterKnife.bind(this);
//        buildFence();
        initView();
        Log.d(TAG, "onCreate:" + smoothMoveList.size());
        loadCarLocation(savedInstanceState);
    }

    private boolean checkBeforeEnter(LatLng carLocation){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int AM_PM = calendar.get(Calendar.AM_PM);
        Log.d(TAG, "checkBeforeEnter: " +  hour + AM_PM);
        if(((AM_PM == Calendar.AM && hour < 11 )||(AM_PM == Calendar.PM && ((hour >1 && hour < 5) || (hour > 9))))) {
            ExploreSchoolCarDialog.show(this, this, TIME_OUT);
            loadImage.setVisibility(View.GONE);
            return false;
        } else {
            float carDistance = AMapUtils.calculateLineDistance(new LatLng(29.531876 ,106.606789), carLocation);
            if (carDistance > 1300) {
                ExploreSchoolCarDialog.show(this, this, LOST_SERVICES);
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        Glide.with(this ).load( R.drawable.ic_school_car_search_load).asGif().into(loadImage) ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }

    }

    private void initLocationType() {
        locationStyle = new MyLocationStyle();
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_school_car_search_girl);

        locationStyle.interval(2000);
        locationStyle.strokeWidth(0);
        locationStyle.radiusFillColor(Color.alpha(0));
        locationStyle.myLocationIcon(descriptor);
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        }

    private void initAMap(){
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(29.531876 ,106.606789), 17f);

        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(locationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showMap(String status, Bundle savedInstanceState) {
        if (status.equals("200")) {
            if(mapView == null) {
                mapView = new MapView(this);
                layout.addView(mapView);
                mapView.onCreate(savedInstanceState);
            }
            RelativeLayout relativeLayout  = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_RIGHT);

            layout.addView(relativeLayout);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_orgnization_logo));
            imageView.layout(0, 20, 10, 0);
            relativeLayout.addView(imageView);

            holeSchoolButton = new ImageButton(this);
            holeSchoolButton.setBackgroundColor(Color.TRANSPARENT);
            holeSchoolButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_school_car_search_hole_school));
            relativeLayout.addView(holeSchoolButton);
            initLocationType();
            initAMap();
            initData();
        } else {
            Toast.makeText(this, "校车暂时不在线哟～", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadCarLocation(Bundle savedInstanceState) {
        RequestManager.INSTANCE.getSchoolCarLocation(new Observer<SchoolCarLocation>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onNext(SchoolCarLocation schoolCarLocation) {
                dataList = schoolCarLocation.getData();
                if (firstEnter) {
                    showMap(schoolCarLocation.getStatus(), savedInstanceState);
                    if (checkBeforeEnter(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLon()))) {
                        timer = new Timer();
                        task = new Task(savedInstanceState);
                        timer.schedule(task,0,1000);
                    }
                    smoothMoveList.add(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLon()));
                    firstEnter = false;
                }
                smoothMoveList.add(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLon()));
                carSmoothMove(smoothMoveList);
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
        smoothMarker.setPoints(smoothMoveList.subList(smoothMoveList.size() - 2, smoothMoveList.size() - 1));
        smoothMarker.setTotalDuration(1);
        smoothMarker.startSmoothMove();
        drawTraceLine();
    }

    private void drawTraceLine(){
        Polyline polyline =aMap.addPolyline(new PolylineOptions().
                addAll(smoothMoveList.subList(0, smoothMoveList.size() - 1)).width(8).color(Color.argb(255, 93,152,255)));
    }

    @OnClick(R.id.explore_schoolcar_toolbar_learnmore)
    public void learnMore(View v) {
        if (v.getId() == R.id.explore_schoolcar_toolbar_learnmore) {
            Intent intent = new Intent(this, SchoolCarLearnMore.class);
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

        if (timer != null && task != null) {
            timer.cancel();
            task.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
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

    private class Task extends TimerTask{
        Bundle saveInstanceState;

        public Task(Bundle saveInstanceState) {
            this.saveInstanceState = saveInstanceState;
        }

        @Override
        public void run() {
            Log.d(TAG, "run: ..................................." );
            loadCarLocation(saveInstanceState);
        }
    }

}
