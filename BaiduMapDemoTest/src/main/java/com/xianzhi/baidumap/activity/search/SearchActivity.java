package com.xianzhi.baidumap.activity.search;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.baidu.BaiduLocationManager;
import com.xianzhi.baidumap.base.BaseActivity;
import com.xianzhi.baidumap.listener.BaiduLocationListener;

import butterknife.BindView;

/**
 * @author LiMing
 * @Demo class SearchActivity
 * @Description TODO 搜索显示
 * @date 2019-09-25 14:03
 */
public class SearchActivity extends BaseActivity implements SensorEventListener {

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_place)
    TextView tvPlace;

    private BaiduMap mBaiduMap;

    private MyLocationConfiguration.LocationMode mCurrentMode;
    //系统方向传感器
    private SensorManager mSensorManager;
    private MyLocationData myLocationData;

    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    // 是否首次定位
    private boolean isFirstLoc = true;
    private LocationClient mLocClient;

    private PoiInfo poiInfo;

    @Override
    protected void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        mBaiduMap = mapView.getMap();
        poiInfo = getIntent().getParcelableExtra("data");
        tvPlace.setText(poiInfo != null ? poiInfo.getName() : "");

        // 获取传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);

        //定位
        mLocClient = new LocationClient(getApplicationContext());
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
                //获取位置描述信息
                String locationDescribe = location.getLocationDescribe();

                /**设置方向相关的数据*/
                myLocationData = new MyLocationData.Builder().accuracy(location.getRadius())// 设置定位数据的精度信息，单位：米
                        .direction(mCurrentDirection)// 此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(myLocationData);
                /**定位显示*/
                if (isFirstLoc) {
                    isFirstLoc = false;
                    /**定位显示*/
                    LatLng cenpt = new LatLng(mCurrentLat, mCurrentLon);
                    //定位显示
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(cenpt).zoom(18.0f);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    mapView.setVisibility(View.VISIBLE);
//                    //定义地图状态
//                    // 传入null，则为默认图标   NORMAL 普通模式   FOLLOWING 跟随模式    COMPASS 罗盘模式
                    setNormalType();
                }

                /**定位到搜索地点*/
                if(poiInfo != null && poiInfo.getLocation().latitude != 0 && poiInfo.getLocation().longitude != 0) {
                    LatLng point = new LatLng(poiInfo.getLocation().latitude, poiInfo.getLocation().longitude);
                    //显示搜索
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                    //改变地图状态
                    mBaiduMap.addOverlay(option);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(point).zoom(18.0f);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        });
        //显示定位涂层
        mBaiduMap.setMyLocationEnabled(true);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            myLocationData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)// 设置定位数据的精度信息，单位：米
                    .direction(mCurrentDirection)// 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册传感器监听
        mSensorManager.unregisterListener(this);
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }

    /**
     * 设置普通模式
     */
    public void setNormalType(){
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 传入null，则为默认图标
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
        MapStatus.Builder builder1 = new MapStatus.Builder();
        builder1.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
    }

    /**
     * 设置跟随模式
     * PS：慎用，跟随模式会回调刷新地图的接口  导致定位地点不能刷新地图   我们之所以实现不了百度地图APP那样的效果  是因为他们没有公开这部分的接口
     */
    public void setFollowType(){
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 设置罗盘模式
     */
    public void setCompassType(){
        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
    }
}
