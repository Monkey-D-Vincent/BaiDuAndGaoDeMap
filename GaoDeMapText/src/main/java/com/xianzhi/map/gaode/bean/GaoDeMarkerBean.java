package com.xianzhi.map.gaode.bean;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;

/**
 * @author LiMing
 * @Demo class GaoDeMarkerBean
 * @Description TODO
 * @date 2019-10-31 16:39
 */
public class GaoDeMarkerBean implements Serializable {

    /*marker 图标*/
    private int icon;
    /*经纬度*/
    private LatLng latLng;
    /*标题*/
    private String title;
    /*内容*/
    private String content;
    /*是否可以拖拽 true 是*/
    private boolean draggable;
    /*是否贴地 true 是*/
    private boolean flat;
    /*是否显示*/
    private boolean visible;
    /*横向坐标*/
    private float hor;
    /*纵向坐标*/
    private float ver;
    /*透明度*/
    private float alpha;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isFlat() {
        return flat;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getHor() {
        return hor;
    }

    public void setHor(float hor) {
        this.hor = hor;
    }

    public float getVer() {
        return ver;
    }

    public void setVer(float ver) {
        this.ver = ver;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
