package com.xianzhi.baidumap.baidu;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.TraceLocation;
import com.xianzhi.baidumap.baidu.tools.BaiduCommonUtil;
import com.xianzhi.baidumap.baidu.tools.BitmapUtil;
import com.xianzhi.baidumap.baidu.tools.CurrentLocation;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.tools.Screens;

/**
 * @author liMing
 * @Demo class BaiduMapUtil
 * @Description TODO
 * @date 2019-10-23 15:15
 */
public class BaiduMapUtil implements SensorEventListener {

    public MapView mapView;
    private BaiduMap baiduMap;
    private Context context;

    /**
     * 轨迹追踪
     */
    private static BaiduMapUtil baiduMapUtil = new BaiduMapUtil();
    public LatLng lastPoint;
    private Marker mMoveMarker;
    /*路线覆盖物*/
    public Overlay polylineOverlay;
    private MapStatus mapStatus;

    /**
     * 定位祥光
     */
    private LocationClient mLocClient;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    // 是否首次定位
    private boolean isFirstLoc = true;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    //系统方向传感器
    private SensorManager mSensorManager;
    private MyLocationData myLocationData;

    private BaiduMapUtil() {
    }

    public static BaiduMapUtil getInstance() {
        return baiduMapUtil;
    }

    /**
     * 初始化
     *
     * @param context
     * @param mapView
     * @param isShowZoomControls 是否显示缩放控件  false 不显示 否则显示
     */
    public void initView(Context context, MapView mapView, boolean isShowZoomControls) {
        this.context = context;
        this.mapView = mapView;
        baiduMap = mapView.getMap();
        //设置是否显示缩放控件
        mapView.showZoomControls(isShowZoomControls);
    }

    /**
     * 定位
     *
     * @param type 带传感器  0 设置普通模式  1 设置跟随模式  2 设置罗盘模式
     */
    public void setLocation(final int type, boolean isLocation) {
        // 获取传感器管理服务
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        //定位
        mLocClient = new LocationClient(context);
        BaiduLocationManager.initLocationOption(mLocClient, new BaiduLocationListener.OnLocationClickListener() {
            @Override
            public void onLocation(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null || mapView == null) {
                    return;
                }
                //获取纬度信息
                mCurrentLat = location.getLatitude();
                //获取经度信息
                mCurrentLon = location.getLongitude();
                //获取定位精度，默认值为0.0f
                mCurrentAccracy = location.getRadius();

                /**设置方向相关的数据*/
                myLocationData = new MyLocationData.Builder().accuracy(location.getRadius())// 设置定位数据的精度信息，单位：米
                        .direction(mCurrentDirection)// 此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                baiduMap.setMyLocationData(myLocationData);
                /**定位显示*/
                if (isFirstLoc) {
                    isFirstLoc = false;
                    /**定位显示*/
                    LatLng latLng = new LatLng(mCurrentLat, mCurrentLon);
                    //定位显示
                    updateStatus(latLng, false);
//                    //定义地图状态
//                    // 传入null，则为默认图标   NORMAL 普通模式   FOLLOWING 跟随模式    COMPASS 罗盘模式
                    if (type == 0) {
                        setNormalType();
                    } else if (type == 1) {
                        setFollowType();
                    } else if (type == 2) {
                        setCompassType();
                    }
                }
            }
        });
        //显示定位涂层
        if(isLocation) {
            baiduMap.setMyLocationEnabled(true);
        }
    }

    /**
     * 设置普通模式
     */
    public void setNormalType() {
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 传入null，则为默认图标
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
        MapStatus.Builder builder1 = new MapStatus.Builder();
        builder1.overlook(0);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
    }

    /**
     * 设置跟随模式
     * PS：慎用，跟随模式会回调刷新地图的接口  导致定位地点不能刷新地图   我们之所以实现不了百度地图APP那样的效果  是因为他们没有公开这部分的接口
     */
    public void setFollowType() {
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 设置罗盘模式
     */
    public void setCompassType() {
        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            myLocationData = new MyLocationData.Builder().accuracy(mCurrentAccracy)// 设置定位数据的精度信息，单位：米
                    .direction(mCurrentDirection)// 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(mCurrentLat).longitude(mCurrentLon).build();
            baiduMap.setMyLocationData(myLocationData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /****************************************************线路追踪************************************************************/

    /**
     * 将轨迹实时定位点转换为地图坐标
     *
     * @param location
     * @return
     */
    public static LatLng convertTraceLocation2Map(TraceLocation location) {
        if (null == location) {
            return null;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
            return null;
        }
        LatLng currentLatLng = new LatLng(latitude, longitude);
        if (CoordType.wgs84 == location.getCoordType()) {
            LatLng sourceLatLng = currentLatLng;
            CoordinateConverter converter = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
            converter.coord(sourceLatLng);
            currentLatLng = converter.convert();
        }
        return currentLatLng;
    }

    /**
     * 刷新中心店
     *
     * @param currentPoint
     * @param showMarker   显示的覆盖物
     */
    public void updateStatus(LatLng currentPoint, boolean showMarker) {
        if (null == baiduMap || null == currentPoint) {
            return;
        }

        if (null != baiduMap.getProjection()) {
            Point screenPoint = baiduMap.getProjection().toScreenLocation(currentPoint);
            // 点在屏幕上的坐标超过限制范围，则重新聚焦底图
            if (screenPoint.y < 200 || screenPoint.y > Screens.getScreenHeight(context) - 500 || screenPoint.x < 200 || screenPoint.x > Screens.getScreenWidth(context) - 200 || null == mapStatus) {
                animateMapStatus(currentPoint, 15.0f);
            }
        } else if (null == mapStatus) {
            // 第一次定位时，聚焦底图
            setMapStatus(currentPoint, 15.0f);
        }

        if (showMarker) {
            addMarker(currentPoint);
        }
    }

    /**
     * 地图标点
     *
     * @param point
     * @param zoom
     */
    public void animateMapStatus(LatLng point, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        mapStatus = builder.target(point).zoom(zoom).build();
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    public void setMapStatus(LatLng point, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        mapStatus = builder.target(point).zoom(zoom).build();
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    /**
     * 添加地图覆盖物
     */
    public void addMarker(LatLng currentPoint) {
        if (null == mMoveMarker) {
            mMoveMarker = addOverlay(currentPoint, BitmapUtil.bmArrowPoint, null);
            return;
        }

        if (null != lastPoint) {
            moveLooper(currentPoint);
        } else {
            lastPoint = currentPoint;
            mMoveMarker.setPosition(currentPoint);
        }
    }

    /**
     * 移动逻辑
     */
    public void moveLooper(LatLng endPoint) {
        mMoveMarker.setPosition(lastPoint);
        mMoveMarker.setRotate((float) BaiduCommonUtil.getAngle(lastPoint, endPoint));

        double slope = BaiduCommonUtil.getSlope(lastPoint, endPoint);
        // 是不是正向的标示（向上设为正向）
        boolean isReverse = (lastPoint.latitude > endPoint.latitude);
        double intercept = BaiduCommonUtil.getInterception(slope, lastPoint);
        double xMoveDistance = isReverse ? BaiduCommonUtil.getXMoveDistance(slope) : -1 * BaiduCommonUtil.getXMoveDistance(slope);

        for (double latitude = lastPoint.latitude; latitude > endPoint.latitude == isReverse; latitude = latitude - xMoveDistance) {
            LatLng latLng;
            if (slope != Double.MAX_VALUE) {
                latLng = new LatLng(latitude, (latitude - intercept) / slope);
            } else {
                latLng = new LatLng(latitude, lastPoint.longitude);
            }
            mMoveMarker.setPosition(latLng);
        }
    }

    /**
     * 地图覆盖物
     *
     * @param currentPoint
     * @param icon
     * @param bundle
     * @return
     */
    public Marker addOverlay(LatLng currentPoint, BitmapDescriptor icon, Bundle bundle) {
        OverlayOptions overlayOptions = new MarkerOptions().position(currentPoint).icon(icon).zIndex(9).draggable(true);
        Marker marker = (Marker) baiduMap.addOverlay(overlayOptions);
        if (null != bundle) {
            marker.setExtraInfo(bundle);
        }
        return marker;
    }

    /**
     * 将地图坐标转换轨迹坐标
     *
     * @param latLng
     * @return
     */
    public static com.baidu.trace.model.LatLng convertMap2Trace(LatLng latLng) {
        return new com.baidu.trace.model.LatLng(latLng.latitude, latLng.longitude);
    }

    /**
     * 将轨迹坐标对象转换为地图坐标对象
     *
     * @param traceLatLng
     * @return
     */
    public static LatLng convertTrace2Map(com.baidu.trace.model.LatLng traceLatLng) {
        return new LatLng(traceLatLng.latitude, traceLatLng.longitude);
    }


    /****************************************************生命周期************************************************************/
    public void onPause() {
        if (null != mapView) {
            mapView.onPause();
        }
    }

    public void onResume() {
        if (null != mapView) {
            mapView.onResume();
        }
    }

    public void clear() {
        // 取消注册传感器监听
        if(mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        // 退出时销毁定位
        if(mLocClient != null) {
            mLocClient.stop();
        }
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        lastPoint = null;
        if (null != mMoveMarker) {
            mMoveMarker.remove();
            mMoveMarker = null;
        }
        if (null != polylineOverlay) {
            polylineOverlay.remove();
            polylineOverlay = null;
        }
        if (null != baiduMap) {
            baiduMap.clear();
            baiduMap = null;
        }
        mapStatus = null;
        if (null != mapView) {
            mapView.onDestroy();
            mapView = null;
        }
    }

}
