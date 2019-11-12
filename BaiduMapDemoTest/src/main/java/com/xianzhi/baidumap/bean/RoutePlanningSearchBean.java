package com.xianzhi.baidumap.bean;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * @author LiMing
 * @Demo class RoutePlanningSearchBean
 * @Description TODO
 * @date 2019-09-29 16:41
 */
public class RoutePlanningSearchBean implements Serializable {

    private String startName;
    private String endName;
    private String startCity;
    private String endCity;
    private int type;

    private int startIcon;
    private int endIcon;
    /**
     * 是否使用系统默认的起终点图片
     */
    private boolean isUseDefaultIcon;

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public int getStartIcon() {
        return startIcon;
    }

    public void setStartIcon(int startIcon) {
        this.startIcon = startIcon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEndIcon() {
        return endIcon;
    }

    public void setEndIcon(int endIcon) {
        this.endIcon = endIcon;
    }

    public boolean isUseDefaultIcon() {
        return isUseDefaultIcon;
    }

    public void setUseDefaultIcon(boolean useDefaultIcon) {
        isUseDefaultIcon = useDefaultIcon;
    }
}
