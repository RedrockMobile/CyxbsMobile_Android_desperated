package com.mredrock.cyxbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.mredrock.cyxbs.BaseAPP;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.ui.Interface.SchoolCarInterface;

/**
 * Created by glossimar on 2018/1/29.
 */

public class SchoolCarMap {
    private static String TAG = "SchoolCarMap";

    private Context context;
    private Bundle savedInstanceState;
    private SchoolCarInterface carInterface;
    private AMap aMap;
    private MapView mapView;
    private MyLocationStyle locationStyle;
    private SmoothMoveMarker smoothMarker;



    public SchoolCarMap(Context context, Bundle savedInstanceState, SchoolCarInterface carInterface) {
        this.context = context;
        this.savedInstanceState = savedInstanceState;
        this.carInterface = carInterface;
    }

    public SchoolCarMap() {
        this.smoothMarker = new SmoothMoveMarker(getaMap());
    }

    public void initLocationType() {
        BitmapDescriptor descriptor;
        locationStyle = new MyLocationStyle();

        if (BaseAPP.getUser(context).gender.equals("女")){
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

    public void initAMap(boolean ifLocation){

        aMap = mapView.getMap();

        if (aMap == null) {
            aMap = mapView.getMap();
            Log.d(TAG, "initAMap: .........");
            if (mapView == null){
            }
        }
        if (ifLocation) {
            aMap.setMyLocationEnabled(true);
            aMap.setMyLocationStyle(locationStyle);
        }

        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(29.531876 ,106.606789), 17f);
        aMap.animateCamera(update);
        smoothMarker = new SmoothMoveMarker(aMap);
    }

    public void showMap(String status, RelativeLayout layout, ImageView loadImage) {
        if (status.equals("200")) {
            loadImage.setVisibility(View.INVISIBLE);
            if(mapView == null) {
                mapView = new MapView(context);
                layout.addView(mapView);
                mapView.onCreate(savedInstanceState);
            }

            RelativeLayout relativeLayout  = new RelativeLayout(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.leftMargin = 40;
            layoutParams.topMargin = 0;
            layoutParams.rightMargin =25;
            layout.addView(relativeLayout, layoutParams);

            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_school_car_search_orgnization_logo));
            imageView.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://eini.cqupt.edu.cn/"));
                context.startActivity(intent);
            });
            RelativeLayout.LayoutParams lpLogo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpLogo.topMargin = 45;
            relativeLayout.addView(imageView, lpLogo);

            initLocationType();
            aMap = mapView.getMap();

            if (aMap == null) {
                aMap = mapView.getMap();
                Log.d(TAG, "initAMap: .........");
                if (mapView == null){
                }
            }
            carInterface.initLocationMapButton(aMap, locationStyle);
        } else {
            Toast.makeText(context, "校车暂时不在线哟～", Toast.LENGTH_SHORT).show();
        }
    }

    public void distroyMap(AMapLocationClient locationClient) {
        if (mapView != null) {
            mapView.onDestroy();
            if (locationClient != null) {
                locationClient.onDestroy();
            }
        }
    }

    public void pauseMap() {
        if (mapView != null) {
            mapView.onPause();
        }
    }

    public void resumeMap() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    public AMap getaMap() {
        return aMap;
    }

    public MapView getMapView() {
        return mapView;
    }

    public SmoothMoveMarker getSmoothMarker() {
        return smoothMarker;
    }
}
