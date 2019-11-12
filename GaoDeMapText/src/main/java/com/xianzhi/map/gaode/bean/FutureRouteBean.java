package com.xianzhi.map.gaode.bean;

import com.amap.api.services.core.LatLonPoint;
import com.xianzhi.map.gaode.util.route.FutureRouteModeUtil;
import com.xianzhi.map.gaode.util.route.FutureRouteUtil;

import java.io.Serializable;

/**
 * @author LiMing
 * @Demo class FutureRouteBean
 * @Description TODO
 * @date 2019-11-04 13:42
 */
public class FutureRouteBean implements Serializable {

    private LatLonPoint startLatLonPoint;
    private LatLonPoint endLatLonPoint;
    /**
     * 汽车类型 {@link FutureRouteUtil#setCarType(int)}
     */
    private int carType;
    /**
     * 规划策略模式 {@link FutureRouteModeUtil}
     */
    private int mode;
    /**
     * 规划时间点个数，最大48个 {@link FutureRouteUtil#setCount(int)}
     */
    private int count;
    /**
     * 规划的时间间隔，单位为秒 {@link FutureRouteUtil#setInterval(int)}
     */
    private int interval;
    /**
     * 出发时间，第一个时间戳（unix时间戳，精确到秒)，{@link FutureRouteUtil#setTravel(int)} (int)}
     */
    private String time;

    public LatLonPoint getStartLatLonPoint() {
        return startLatLonPoint;
    }

    public void setStartLatLonPoint(LatLonPoint startLatLonPoint) {
        this.startLatLonPoint = startLatLonPoint;
    }

    public LatLonPoint getEndLatLonPoint() {
        return endLatLonPoint;
    }

    public void setEndLatLonPoint(LatLonPoint endLatLonPoint) {
        this.endLatLonPoint = endLatLonPoint;
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
