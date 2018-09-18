package com.mredrock.cyxbs.ui.activity.Interface;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.MyLocationStyle;
import com.mredrock.cyxbs.model.SchoolCarLocation;

/**
 * Created by glossimar on 2018/1/29.
 */

public interface SchoolCarInterface {
    void initLocationMapButton (AMap aMap, MyLocationStyle locationStyle);
    void processLocationInfo(SchoolCarLocation carLocationInfo, long aLong, int carID);
}