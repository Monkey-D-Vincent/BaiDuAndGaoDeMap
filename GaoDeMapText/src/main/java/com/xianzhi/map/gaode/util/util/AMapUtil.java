package com.xianzhi.map.gaode.util.util;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @Demo class AMapUtil
 * @Description TODO
 * @date 2019-11-04 16:46
 */
public class AMapUtil {
    public static LatLng convertToLatLng(LatLonPoint point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    public static List<LatLng> convertToLatLngList(List<LatLonPoint> pointList) {
        List<LatLng> resultList = new ArrayList<>();
        if (pointList == null) {
            return resultList;
        }
        for (LatLonPoint point : pointList) {
            resultList.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }
        return resultList;
    }
}
