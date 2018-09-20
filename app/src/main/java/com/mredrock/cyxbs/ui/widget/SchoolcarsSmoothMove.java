package com.mredrock.cyxbs.ui.widget;

import android.app.Activity;
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
import com.mredrock.cyxbs.ui.activity.Interface.SchoolCarInterface;
import com.mredrock.cyxbs.util.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.mredrock.cyxbs.ui.activity.explore.ExploreSchoolCarActivity.TIME_OUT;

/**
 * Created by glossimar on 2018/1/29.
 */

public class SchoolcarsSmoothMove {
    private double carAngle;

    private SchoolCarMap schoolCarMap;
    private SchoolCarInterface carInterface;
    private List<String> timeList;
    private List<LatLng> smoothMoveList1;
    private List<LatLng> smoothMoveList2;
    private List<LatLng> smoothMoveList3;

    public SchoolcarsSmoothMove(SchoolCarMap schoolCarMap) {
        this.schoolCarMap = schoolCarMap;
        timeList = new ArrayList<>();
        smoothMoveList1 = new ArrayList<>();
        smoothMoveList2 = new ArrayList<>();
        smoothMoveList3 = new ArrayList<>();
    }

    public void loadCarLocation(long aLong, int carID) {
        RequestManager.INSTANCE.getSchoolCarLocation(new Observer<SchoolCarLocation>() {
                                                         @Override
                                                         public void onSubscribe(Disposable d) {
                                                         }

                                                         @Override
                                                         public void onNext(SchoolCarLocation schoolCarLocation) {
                                                             carInterface.processLocationInfo(schoolCarLocation, aLong, carID);
                                                             if (carID != 0) {
                                                                 SchoolCarLocation.Data location = schoolCarLocation.getData().get(carID - 1);
                                                                 if (timeList != null) {
                                                                     timeList.add(schoolCarLocation.getTime());
                                                                 }
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
                                                     }, "Redrock", Utils.md5Hex(String.valueOf(System.currentTimeMillis()).substring(0, 10) + "." + "Redrock"),
                String.valueOf(System.currentTimeMillis()).substring(0, 10),
                Utils.md5Hex(String.valueOf(System.currentTimeMillis() - 1).substring(0, 10)));
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
        if (smoothMoveList1.size() > 0 || smoothMoveList2.size() > 0) {
            SmoothMoveMarker smoothMarker = new SmoothMoveMarker(schoolCarMap.getaMap());
            smoothMoveMarkers.add(smoothMarker);
            int carAmount = smoothMoveMarkers.size() - 1;
            smoothMoveMarkers.get(carAmount).setDescriptor(BitmapDescriptorFactory.fromBitmap(bitmapChanged));
            if (smoothMoveList2.size() > 3 || smoothMoveList1.size() > 3) {
                changeCarOrientation(smoothMoveMarkers.get(carAmount), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 3), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 2), 2);
                smoothMoveMarkers.get(carAmount).setPoints(getSmoothMoveList(carAmount).subList(getSmoothMoveList(carAmount).size() - 3, getSmoothMoveList(carAmount).size() - 1));
            } else {
                changeCarOrientation(smoothMoveMarkers.get(carAmount), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 1), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 1), 2);
                smoothMoveMarkers.get(carAmount).setPoints(getSmoothMoveList(carAmount).subList(getSmoothMoveList(carAmount).size() - 1, getSmoothMoveList(carAmount).size() - 1));
            }
            smoothMoveMarkers.get(carAmount).setTotalDuration(2);
            drawTraceLine(schoolCarMap.getaMap(), getSmoothMoveList(carAmount));
            smoothMoveMarkers.get(carAmount).startSmoothMove();
        }

//        if (smoothMoveList2.size() > 0 || smoothMoveList1.size() > 0) {
//            SmoothMoveMarker smoothMarker = new SmoothMoveMarker(schoolCarMap.getaMap());
//            smoothMoveMarkers.add(smoothMarker);
//            int carAmount = smoothMoveMarkers.size() - 1;
//            smoothMoveMarkers.get(carAmount).setDescriptor(BitmapDescriptorFactory.fromBitmap(bitmapChanged));
//            if (smoothMoveList1.size() < 4 || smoothMoveList2.size() < 4) {
//                changeCarOrientation(smoothMoveMarkers.get(carAmount), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 3), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 2), 2);
//                smoothMoveMarkers.get(carAmount).setPoints(getSmoothMoveList(carAmount).subList(getSmoothMoveList(carAmount).size() - 3, getSmoothMoveList(carAmount).size() - 1));
//                smoothMoveMarkers.get(carAmount).setTotalDuration(2);
//                drawTraceLine(schoolCarMap.getaMap(), getSmoothMoveList(carAmount));
//            } else {
//                changeCarOrientation(smoothMoveMarkers.get(carAmount), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 1), getSmoothMoveList(carAmount).get(getSmoothMoveList(carAmount).size() - 1), 2);
//                smoothMoveMarkers.get(carAmount).setPoints(getSmoothMoveList(carAmount).subList(getSmoothMoveList(carAmount).size() - 1, getSmoothMoveList(carAmount).size() - 1));
//                smoothMoveMarkers.get(carAmount).setTotalDuration(2);
//                drawTraceLine(schoolCarMap.getaMap(), getSmoothMoveList(carAmount));
//            }
//            smoothMoveMarkers.get(carAmount).startSmoothMove();
//        }
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

    public boolean checkBeforeEnter(Activity activity, ExploreSchoolCarDialog dialog) {
        List<String> list = timeList;
         if (timeList == null || (timeList.size() > 1 && timeList.get(timeList.size() - 1).equals(timeList.get(0))) ) {
            timeList = null;
            dialog.show(activity, TIME_OUT);
            return false;
        }
        timeList.clear();
        return true;
    }

    public void clearAllList() {
        smoothMoveList3.clear();
        smoothMoveList2.clear();
        smoothMoveList1.clear();
    }
}

