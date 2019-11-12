package com.xianzhi.baidumap.bean;

import java.io.Serializable;

/**
 * @author LiMing
 * @Demo class BaiduNavigationBean
 * @Description TODO
 * @date 2019-10-14 15:45
 */
public class BaiduNavigationBean implements Serializable {

    private String startPlace;
    private String startPlaceDesc;
    private double startLat;
    private double startLng;
    private String endPlace;
    private String endPlaceDesc;
    private double endLat;
    private double endLng;
    private int type;

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getStartPlaceDesc() {
        return startPlaceDesc;
    }

    public void setStartPlaceDesc(String startPlaceDesc) {
        this.startPlaceDesc = startPlaceDesc;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getEndPlaceDesc() {
        return endPlaceDesc;
    }

    public void setEndPlaceDesc(String endPlaceDesc) {
        this.endPlaceDesc = endPlaceDesc;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
