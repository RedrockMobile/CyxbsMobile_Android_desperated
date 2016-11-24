package com.mredrock.cyxbs.ui.activity.explore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.jaeger.library.StatusBarUtil;
import com.mredrock.cyxbs.R;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.subscriber.SimpleSubscriber;
import com.mredrock.cyxbs.subscriber.SubscriberListener;
import com.mredrock.cyxbs.util.MapHelper;
import com.mredrock.cyxbs.util.NetUtils;
import com.mredrock.cyxbs.util.permission.AfterPermissionGranted;
import com.mredrock.cyxbs.util.permission.EasyPermissions;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends BaseExploreActivity
        implements AMap.OnMarkerClickListener, EasyPermissions.PermissionCallbacks {

    private static final int RC_STORAGE = 123;

    @Bind(R.id.map)
    MapView mMapView;

    private AMap mAMap;
    private MapHelper mMapHelper;
    private MaterialDialog mProgressDialog;

    public static void startMapActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, MapActivity.class);
        startingActivity.startActivity(intent);
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MapHelper.LOAD_OVERLAY_SUCCESS:
                    showOverlayedMap((Bitmap) msg.obj);
                    onLoadProgressFinish();
                    break;
                case MapHelper.LOAD_OVERLAY_FAILED:
                    Toast.makeText(MapActivity.this,
                            getResources().getString(R.string.error_text), Toast.LENGTH_SHORT).show();
                    onLoadProgressFinish();
                    break;
                default:
                    throw new AssertionError("Unknown handler message received: " + msg.what);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this, 50);
        mMapView.onCreate(savedInstanceState);

        setupMap();

        mMapHelper = new MapHelper(this, mHandler);
        startLoading();

        overridePendingTransition(0, 0);
    }

    private void setupMap() {
        mAMap = mMapView.getMap();
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);

        UiSettings settings = mAMap.getUiSettings();
        settings.setCompassEnabled(true); //show the compass

        mAMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(29.532354, 106.607916), 16.5f)); //coordinate of CQUPT
        mAMap.moveCamera(CameraUpdateFactory.changeBearing(90f)); //rotate map 90 degrees
        mAMap.setOnMarkerClickListener(this);
        addMarkerToMap();
    }

    private void addMarkerToMap() {
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.534221, 106.608603)).title("风雨操场").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.535127, 106.609209)).title("三教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.53621, 106.609123)).title("四教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.536144, 106.610572)).title("五教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.534805, 106.611103)).title("八教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.532821, 106.609644)).title("太极操场").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.532821, 106.6089461)).title("红岩网校、科联、社联、校学生会").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.534613, 106.606661)).title("数字图书馆").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.531202, 106.606591)).title("老图书馆").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.535603, 106.607396)).title("逸夫楼").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.535342, 106.605417)).title("新校门").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position_red)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.533307, 106.604301)).title("老校门").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position_red)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.53263, 106.606774)).title("二教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.532644, 106.607514)).title("老操场").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.531925, 106.610974)).title("红高粱食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.531386, 106.607919)).title("中心食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.530854, 106.60756)).title("大西北食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.530312, 106.608386)).title("延生食堂、千喜鹤食堂").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.530908, 106.604834)).title("信息科技大楼").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.532093, 106.605009)).title("七教").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
        mAMap.addMarker(new MarkerOptions().position(new LatLng(29.53224, 106.603845)).title("校医院").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)));
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        mMapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        tryDownload(false);
    }

    @AfterPermissionGranted(RC_STORAGE)
    private void startLoading() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, permissions)) {
            Bitmap bitmap = mMapHelper.getCachedOverlayImage();
            if (bitmap != null) {
                showOverlayedMap(bitmap);
            } else {
                tryDownload(true);
            }
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.map_permission_explanation),
                    RC_STORAGE, permissions);
        }
    }

    /**
     * Setting only show the area of cqupt
     */
    private void showOverlayedMap(Bitmap bitmap) {
        LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(29.537246, 106.602359))
                .include(new LatLng(29.527575, 106.61341)).build();
        mAMap.addGroundOverlay(new GroundOverlayOptions().anchor(0.5f, 0.5f).transparency(0.1f)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                .positionFromBounds(bounds));
    }


    private void tryDownload(boolean shouldCache) {
        if (!NetUtils.isWiFiEnabled(this)) {
            showFlowWarningDialog(shouldCache);
        } else {
            getMapOverlayImageUrl(shouldCache);
        }
    }

    private void showFlowWarningDialog(boolean shouldCache) {
        new MaterialDialog.Builder(this)
                .title(getResources().getString(R.string.map_flow_dialog_title))
                .content(getResources().getString(R.string.map_flow_dialog_content))
                .theme(Theme.LIGHT)
                .positiveText(getResources().getString(R.string.map_flow_dialog_positive))
                .negativeText(getResources().getString(R.string.map_flow_dialog_negative))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        getMapOverlayImageUrl(shouldCache);
                    }
                }).show();
    }

    private void getMapOverlayImageUrl(boolean shouldCache) {
        RequestManager.getInstance().getMapOverlayImageUrl(
                new SimpleSubscriber<>(this, new SubscriberListener<String>() {
                    @Override
                    public void onStart() {
                        onLoadProgress();
                    }

                    @Override
                    public void onNext(String url) {
                        mMapHelper.loadOverlayImage(url, shouldCache);
                    }
                }), "overmap", "map");
    }

    private void onLoadProgress() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title(getResources().getString(R.string.map_progress_dialog_title))
                .content(getResources().getString(R.string.map_progress_dialog_content))
                .theme(Theme.LIGHT)
                .progress(true, 100)
                .cancelable(false)
                .show();
    }

    private void onLoadProgressFinish() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
