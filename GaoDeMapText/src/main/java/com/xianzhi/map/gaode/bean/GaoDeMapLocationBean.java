package com.xianzhi.map.gaode.bean;

import java.io.Serializable;

/**
 * @author LiMing
 * @Demo class GaoDeMapLocationBean
 * @Description TODO
 * @date 2019-10-25 16:42
 */
public class GaoDeMapLocationBean implements Serializable {

    /*锚点水平范围的比例，建议传入0 到1 之间的数值*/
    private float horizontal = 0.0f;
    /*锚点垂直范围的比例，建议传入0 到1 之间的数值*/
    private float vertical = 0.0f;
    /*边框颜色 越小越淡  0 为透明*/
    private int strokeColor = 1;
    /*填充颜色  越小越淡  0 为透明*/
    private int radiusFillColor = 1;
    /*边框宽度 0 不显示边框*/
    private float width = 0.0f;
    /*单位：毫秒，默认值：1000毫秒，如果传小于1000的任何值将按照1000计算。该方法只会作用在会执行连续定位的工作模式上*/
    private long interval = 0;
    /*图片 ID*/
    private int icon;

    public float getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(float horizontal) {
        this.horizontal = horizontal;
    }

    public float getVertical() {
        return vertical;
    }

    public void setVertical(float vertical) {
        this.vertical = vertical;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getRadiusFillColor() {
        return radiusFillColor;
    }

    public void setRadiusFillColor(int radiusFillColor) {
        this.radiusFillColor = radiusFillColor;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
