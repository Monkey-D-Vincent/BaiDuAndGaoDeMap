package com.xianzhi.map.gaode.util.route;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.services.route.DriveRoutePlanResult;
import com.amap.api.services.route.RouteSearch;
import com.xianzhi.map.gaode.util.util.TimeUtils;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * @author LiMing
 * @Demo class FutureRouteUtil
 * @Description TODO 未来行程路线
 * @date 2019-11-01 17:21
 */
public class FutureRouteUtil {

    private static FutureRouteUtil util;
    private static Context mContext;

    private RouteSearch routeSearch;
    private RouteSearch.FromAndTo mFromAndTo;
    private int date;
    private int mInterval;
    private int mCount;
    private int mMode = -1;
    private int mCarType = -1;
    private String mParentPoiID;

    private FutureRouteUtil() {
        routeSearch = new RouteSearch(mContext);
    }

    public static FutureRouteUtil getInstance(Context context) {
        mContext = context;
        if (util == null) {
            util = new FutureRouteUtil();
        }
        return util;
    }

    /**
     * 路径的起点终点，必设
     *
     * @param fromAndTo 始终点的经纬度必须存在
     * @return
     * @see RouteSearch.FromAndTo
     */
    public FutureRouteUtil setPlaceInfo(RouteSearch.FromAndTo fromAndTo) {
        mFromAndTo = fromAndTo;
        return this;
    }

    /**
     * 出发时间，第一个时间戳（unix时间戳，精确到秒)，必设
     *
     * @param time 单位：秒
     *             注意：是 int 类型 不是 long 类型
     * @return
     */
    public FutureRouteUtil setTravel(int time) {
        date = time;
        return this;
    }

    /**
     * 规划的时间间隔，单位为秒，必设
     *
     * @param interval 注意：单位是秒 不是毫秒
     * @return
     */
    public FutureRouteUtil setInterval(int interval) {
        mInterval = interval;
        return this;
    }

    /**
     * 规划时间点个数，最大48个，必设
     *
     * @param count
     * @return
     */
    public FutureRouteUtil setCount(int count) {
        mCount = count;
        return this;
    }

    /**
     * 设置终点的父POIID，无父POI的情况留空即可
     *
     * @param parentPoiID
     * @return
     */
    public FutureRouteUtil setParentPoiID(String parentPoiID) {
        mParentPoiID = parentPoiID;
        return this;
    }

    /**
     * 设置规划策略模式，可选，默认为速度优先
     *
     * @param mode 返回的结果考虑路况，尽量躲避拥堵而规划路径，与高德地图的“躲避拥堵”策略一致. {@link FutureRouteModeUtil#AVOIDINGCONGESTION}
     *             返回的结果不走高速，与高德地图“不走高速”策略一致. {@link FutureRouteModeUtil#NOTHIGHWAY}
     *             返回的结果尽可能规划收费较低甚至免费的路径，与高德地图“避免收费”策略一致. {@link FutureRouteModeUtil#AVOIDPAYMONEY}
     *             返回的结果考虑路况，尽量躲避拥堵而规划路径，并且不走高速，与高德地图的“躲避拥堵&不走高速”策略一致. {@link FutureRouteModeUtil#AVOIDINGCONGESTIONANDNOTHIGHWAY}
     *             返回的结果尽量不走高速，并且尽量规划收费较低甚至免费的路径结果，与高德地图的“避免收费&不走高速”策略一致. {@link FutureRouteModeUtil#AVOIDPAYMONEYANDNOTHIGHWAY}
     *             返回路径规划结果会尽量的躲避拥堵，并且规划收费较低甚至免费的路径结果，与高德地图的“躲避拥堵&避免收费”策略一致. {@link FutureRouteModeUtil#AVOIDINGCONGESTIONANDAVOIDPAYMONEY}
     *             返回的结果尽量躲避拥堵，规划收费较低甚至免费的路径结果，并且尽量不走高速路，与高德地图的“避免拥堵&避免收费&不走高速”策略一致. {@link FutureRouteModeUtil#AVOIDINGCONGESTIONANDNOTHIGHWAYANDAVOIDPAYMONEY}
     *             返回的结果会优先选择高速路，与高德地图的“高速优先”策略一致. {@link FutureRouteModeUtil#PRIORITYHIGHWAY}
     *             返回的结果会优先考虑高速路，并且会考虑路况躲避拥堵，与高德地图的“躲避拥堵&高速优先”策略一致. {@link FutureRouteModeUtil#AVOIDINGCONGESTIONANDPRIORITYHIGHWAY}
     *             不考虑路况，返回速度最优、耗时最短的路线，但是此路线不一定距离最短. {@link FutureRouteModeUtil#ROUTESHORYEST}
     *             避让拥堵&速度优先&避免收费. {@link FutureRouteModeUtil#AVOIDINGCONGESTIONANDROUTESHORYESTANDAVOIDPAYMONEY}
     * @return
     * @see FutureRouteModeUtil
     */
    public FutureRouteUtil setMode(int mode) {
        mMode = mode;
        return this;
    }

    /**
     * 设置车辆类型，默认为普通汽车
     *
     * @param carType 0：普通汽车，1：纯电动车，2：插电混动车
     * @return
     */
    public FutureRouteUtil setCarType(int carType) {
        mCarType = carType;
        return this;
    }

    /**
     * 设置规划策略模式，可选，默认为速度优先
     * 设置路径的起点终点，必设 {@link FutureRouteUtil#setPlaceInfo(RouteSearch.FromAndTo)}
     * 设置出发时间，第一个时间戳（unix时间戳，精确到秒)，必设 {@link FutureRouteUtil#setTravel(int)}
     * 设置规划的时间间隔，单位为秒，必设 {@link FutureRouteUtil#setInterval(int)}
     * 设置规划时间点个数，最大48个，必设 {@link FutureRouteUtil#setCount(int)}
     * 设置终点的父POIID，无父POI的情况留空即可 {@link FutureRouteUtil#setParentPoiID(String)}
     * 设置车辆类型，默认为普通汽车 {@link FutureRouteUtil#setCarType(int)}
     */
    public void getFutureRouteData(final GaoDeOnClickListener.OnFutureRouteClickListener listener) {
        routeSearch.setOnRoutePlanSearchListener(new RouteSearch.OnRoutePlanSearchListener() {
            @Override
            public void onDriveRoutePlanSearched(DriveRoutePlanResult driveRoutePlanResult, int i) {
//                返回的信息中包括：路线的距离、规划时间、路况情况等。
//                - 可以在回调中解析 result，获取驾车的路径；
//                - result.getPaths()可以获取到 DrivePath 列表，驾车路径的详细信息可参考 DrivePlanPath 类；
//                - 返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）。
                if (i == 1000) {
                    listener.onFutureRouteClick(driveRoutePlanResult);
                }
            }
        });
        RouteSearch.DrivePlanQuery query = new RouteSearch.DrivePlanQuery(mFromAndTo, date, mInterval, mCount);
        if (!TextUtils.isEmpty(mParentPoiID)) {
            query.setDestParentPoiID(mParentPoiID);
        }
        if (mMode != -1) {
            query.setMode(mMode);
        }
        if (mCarType != -1) {
            query.setCarType(mCarType);
        }
        routeSearch.calculateDrivePlanAsyn(query);
    }

}
