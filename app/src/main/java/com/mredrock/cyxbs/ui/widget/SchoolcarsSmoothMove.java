package com.mredrock.cyxbs.ui.widget;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.mredrock.cyxbs.model.SchoolCarLocation;
import com.mredrock.cyxbs.network.RequestManager;
import com.mredrock.cyxbs.ui.Interface.SchoolCarInterface;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by glossimar on 2018/1/29.
 */

public class SchoolcarsSmoothMove {
    private double carAngle;

    private SchoolCarMap schoolCarMap;
    private SchoolCarInterface carInterface;
    private List<LatLng> smoothMoveList1;
    private List<LatLng> smoothMoveList2;
    private List<LatLng> smoothMoveList3;

    public SchoolcarsSmoothMove(SchoolCarMap schoolCarMap) {
        this.schoolCarMap = schoolCarMap;
        smoothMoveList1 = new ArrayList<>();
        smoothMoveList2 = new ArrayList<>();
        smoothMoveList3 = new ArrayList<>();
    }

    public void loadCarLocation(long aLong, int carID) {
        RequestManager.INSTANCE.getSchoolCarLocation(new Observer<SchoolCarLocation>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(SchoolCarLocation schoolCarLocation) {
              carInterface.processLocationInfo(schoolCarLocation, aLong, carID);
              if (carID != 0) {
                  SchoolCarLocation.Data location = schoolCarLocation.getData().get(carID - 1);
                  switch (carID) {
                      case 1:
                          smoothMoveList1.add(new LatLng(location.getLat(), location.getLon()));
                          break;
                      case 2:
                          smoothMoveList2.add(new LatLng(location.getLat(), location.getLon()));
                          break;
                      case 3:
                          smoothMoveList3.add(new LatLng(location.getLat(), location.getLon()));
                          break;
                      default:
                          break;
                  }
              }
            }

            @Override
            public void onError(Throwable e) {
//                dialog.show(ExploreSchoolCarActivity.this, LOST_SERVICES);
            }

            @Override
            public void onComplete() {
//                dialog.show(ExploreSchoolCarActivity.this, LOST_SERVICES);
            }
        });
    }


    private void drawTraceLine(AMap aMap, List<LatLng> smoothMoveList){
        Polyline polyline =aMap.addPolyline(new PolylineOptions().
                addAll(smoothMoveList.subList(0, smoothMoveList.size() - 1)).width(8).color(Color.argb(255, 93,152,255)));
    }

    private void changeCarOrientation(SmoothMoveMarker marker, LatLng latlng1, LatLng latlng2, double errorRange) {

        if (!(latlng1.latitude == latlng2.latitude && latlng1.longitude == latlng2.longitude)) {
            double nextOrientation = getNextOrientation(latlng1, latlng2);
            double currentAngle = carAngle;
            if (Math.abs(nextOrientation - currentAngle) > errorRange) {
                carAngle = nextOrientation;
                float computAngle = (float) computRotateAngle(currentAngle, nextOrientation);
                if (marker.getMarker() != null) {
                    Marker makerLocal = marker.getMarker();
                    makerLocal.setRotateAngle(marker.getMarker().getRotateAngle() + computAngle);
                }
            }
        }
    }

    public void smoothMove(List<SmoothMoveMarker> smoothMoveMarkers, Bitmap bitmapChanged ) {
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(schoolCarMap.getaMap());
        smoothMoveMarkers.add(smoothMarker);
        int carAmount = smoothMoveMarkers.size() - 1;
        smoothMoveMarkers.get(carAmount).setDescriptor(BitmapDescriptorFactory.fromBitmap(bitmapChanged));
        changeCarOrientation(smoothMoveMarkers.get(carAmount), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 3), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 2), 2);
        smoothMoveMarkers.get(carAmount).setPoints(getSmoothMoveList(carAmount).subList(getSmoothMoveList(carAmount).size() - 3, getSmoothMoveList(carAmount).size() - 1));
        smoothMoveMarkers.get(carAmount).setTotalDuration(2);
        drawTraceLine(schoolCarMap.getaMap(),getSmoothMoveList(carAmount));
        smoothMoveMarkers.get(carAmount).startSmoothMove();
    }


    private double getNextOrientation(LatLng latlng1, LatLng latlng2) {
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
            if ((latlng2.longitude - latlng1.longitude) < 0) {
                angle = 180;
            } else {
                angle = 0;
            }
        } else if (b == 0) {
            if ((latlng2.latitude - latlng1.latitude) < 0) {
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

    public void setCarMapInterface(SchoolCarInterface carMapInterface) {
        this.carInterface = carMapInterface;
    }

    public List<LatLng> getSmoothMoveList(int carID) {
        List<LatLng> smoothMoveList = null;
        switch (carID) {
            case 0:
                smoothMoveList = smoothMoveList1;
                return smoothMoveList;
            case 1:
                smoothMoveList = smoothMoveList2;
                return smoothMoveList;

            case 2:
                smoothMoveList = smoothMoveList3;
                return smoothMoveList;
        }
        return smoothMoveList;
    }
}
