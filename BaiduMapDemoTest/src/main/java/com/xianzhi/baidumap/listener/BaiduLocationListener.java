package com.xianzhi.baidumap.listener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;

import java.util.List;

/**
 * @author LiMing
 * @Demo class BaiduLocationListener
 * @Description TODO
 * @date 2019-09-20 15:04
 */
public interface BaiduLocationListener {

    /**
     * 定位监听
     */
    interface OnLocationClickListener{
        void onLocation(BDLocation location);
    }

    /**
     * 地点和地里边吗搜索
     */
    interface OnSearchAddressOrNumClickListener {
        /**
         *
         * @param type 0 经纬度   1 位置
         * @param geoCodeResult 经纬度
         * @param reverseGeoCodeResult 位置
         */
        void onSearchClick(int type, GeoCodeResult geoCodeResult, ReverseGeoCodeResult reverseGeoCodeResult);
    }

    /**
     * 地点搜索
     */
    interface OnSearchAddressClickListener {
        void onSearchClick(List<PoiInfo> searchData);
    }

    /**
     * 线路规划
     */
    interface OnRoutePlanningSearchClickListener {
        void onSearchClick(List<DrivingRouteLine> routePlans);
    }

    /**
     * 导航初始化
     */
    interface OnNavigationInitClickListener {
        void onNavigationClick();
    }

    /**
     * 导航
     */
    interface OnNavigationClickListener {
        void onNavigationClick(int info);
    }

    /**
     * 路线追踪
     */
    interface OnRouteTraceClickListener {
        void onRouteTraceClick(int type);
    }

}
