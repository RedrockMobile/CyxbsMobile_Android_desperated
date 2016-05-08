package com.mredrock.cyxbs.ui.activity.explore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.mredrock.cyxbs.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements AMap.OnMarkerClickListener {

    @Bind(R.id.map)
    MapView mMapView;

    private AMap mAmap;

    public static void startMapActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mMapView.onCreate(savedInstanceState);

        initMap();
    }

    private void initMap() {
        mAmap = mMapView.getMap();
        mAmap.setMapType(AMap.MAP_TYPE_NORMAL);

        UiSettings settings = mAmap.getUiSettings();
        settings.setCompassEnabled(true); //show the compass

        mAmap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(29.532354, 106.607916), 16.5f)); //coordinate of CQUPT
        mAmap.moveCamera(CameraUpdateFactory.changeBearing(90f)); //rotate map 90 degrees
        mAmap.setOnMarkerClickListener(this);
        addMarkerToMap();
//        Bitmap mapBitmap = null;
//        try {
//            FileInputStream fis = null;
//            fis = new FileInputStream(getExternalCacheDir() + "/map.png");
//            mapBitmap = BitmapFactory.decodeStream(fis);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (mapBitmap != null) {
//            //showMap(mapBitmap);
//        } else {
//            if (NetUtils.isWifi(this)) {
//                LogUtils.d("下载地图");
//                presenter.getMap();
//            } else {
//                new MaterialDialog.Builder(this)
//                        .title("流量警告")
//                        .titleColor(this.getResources().getColor(R.color.dialog_blue))
//                        .backgroundColor(this.getResources().getColor(R.color.white))
//                        .positiveColor(this.getResources().getColor(R.color.dialog_blue))
//                        .negativeColor(this.getResources().getColor(R.color.dialog_nav))
//                        .content("当前不在wifi下哦，是否用流量下载重邮地图？")
//                        .theme(Theme.LIGHT)
//                        .positiveText("就是任性")
//                        .negativeText("还是算了")
//                        .callback(new MaterialDialog.ButtonCallback() {
//                            @Override
//                            public void onPositive(MaterialDialog dialog) {
//                                LogUtils.d("下载地图");
//                                presenter.getMap();
//                            }
//                }).show();
//            }
//        }

    }


    private void addMarkerToMap() {
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.534221, 106.608603)).title("风雨操场").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.535127, 106.609209)).title("三教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.53621, 106.609123)).title("四教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.536144, 106.610572)).title("五教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.534805, 106.611103)).title("八教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.532821, 106.609644)).title("太极操场").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.532821, 106.6089461)).title("红岩网校、科联、社联、校学生会").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.534613, 106.606661)).title("数字图书馆").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.531202, 106.606591)).title("老图书馆").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.535603, 106.607396)).title("逸夫楼").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.535342, 106.605417)).title("新校门").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position_red)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.533307, 106.604301)).title("老校门").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position_red)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.53263, 106.606774)).title("二教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.532644, 106.607514)).title("老操场").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.531925, 106.610974)).title("红高粱食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.531386, 106.607919)).title("中心食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.530854, 106.60756)).title("大西北食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.530312, 106.608386)).title("延生食堂、千喜鹤食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.530908, 106.604834)).title("信息科技大楼").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.532093, 106.605009)).title("七教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(29.53224, 106.603845)).title("校医院").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return true;
    }

//    private void showMap(Bitmap bitmap) {
//        // 设置只显示重邮图片的显示区域
//        LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(29.537246, 106.602359))
//                .include(new LatLng(29.527575, 106.61341)).build();
//        aMap.addGroundOverlay(new GroundOverlayOptions().anchor(0.5f, 0.5f).transparency(0.1f)
//                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
//                .positionFromBounds(bounds));
//    }
}
