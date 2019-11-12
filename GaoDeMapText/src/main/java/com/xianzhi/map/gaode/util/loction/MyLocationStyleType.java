package com.xianzhi.map.gaode.util.loction;

/**
 * @author LiMing
 * @Demo class MyLocationStyleType
 * @Description TODO 静态类
 * @date 2019-10-25 17:11
 */
public class MyLocationStyleType {

    /**
     * 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
     */
    public static int LOCATION_TYPE_LOCATION_ROTATE = 1;
    /**
     * 只定位一次
     */
    public static int LOCATION_TYPE_SHOW = 2;
    /**
     * 定位一次，且将视角移动到地图中心点
     */
    public static int LOCATION_TYPE_LOCATE = 3;
    /**
     * 连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
     */
    public static int LOCATION_TYPE_FOLLOW = 4;
    /**
     * 连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
     */
    public static int LOCATION_TYPE_MAP_ROTATE = 5;
    /**
     * 连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
     */
    public static int LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER = 6;
    /**
     * 连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
     */
    public static int LOCATION_TYPE_FOLLOW_NO_CENTER = 7;
    /**
     * 连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
     */
    public static int LOCATION_TYPE_MAP_ROTATE_NO_CENTER = 8;
}
