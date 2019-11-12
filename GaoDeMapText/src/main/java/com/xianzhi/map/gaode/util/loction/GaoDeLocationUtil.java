package com.xianzhi.map.gaode.util.loction;

import android.content.Context;
import android.location.Location;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xianzhi.map.gaode.bean.GaoDeMapLocationBean;
import com.xianzhi.map.listener.GaoDeOnClickListener;

/**
 * @author LiMing
 * @Demo class GaoDeLocationUtil
 * @Description TODO 定位
 * @date 2019-10-25 13:44
 */
public class GaoDeLocationUtil {

    private static GaoDeMapLocationBean bean = new GaoDeMapLocationBean();

    /**
     * 定位
     * 如果需要设置定位图标的锚点请先调用{@link GaoDeLocationUtil#anchor(float, float)} ()}
     * 如果需要设置蓝点样式请先调用{@link GaoDeLocationUtil#strokeColor(int, int, float)}} ()}
     * 如果需要设置定位频次方法请先调用{@link GaoDeLocationUtil#interval(long)} ()}
     * 如果需要设置定位蓝点的 icon 图标请先调用{@link GaoDeLocationUtil#myLocationIcon(int)} ()}
     *
     * @param context      Context
     * @param aMap         AMap
     * @param locationIcon 设置是否显示定位小蓝点 true 显示
     * @param locationBtn  设置默认定位按钮是否显示 true 显示
     * @param location     设置为 true 表示启动显示定位蓝点，false 表示隐藏定位蓝点并不进行定位，默认是 false
     * @param traffic      是否显示交通路线 true 显示
     * @param type         类型
     *                     只定位一次。{@link MyLocationStyleType#LOCATION_TYPE_SHOW}
     *                     定位一次，且将视角移动到地图中心点 {@link MyLocationStyleType#LOCATION_TYPE_LOCATE}
     *                     连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位） {@link MyLocationStyleType#LOCATION_TYPE_FOLLOW}
     *                     连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位） {@link MyLocationStyleType#LOCATION_TYPE_MAP_ROTATE}
     *                     连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。 {@link MyLocationStyleType#LOCATION_TYPE_LOCATION_ROTATE}
     *                     <p>以下三种模式从5.1.0版本开始提供</p>
     *                     连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。 {@link MyLocationStyleType#LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER}
     *                     连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。 {@link MyLocationStyleType#LOCATION_TYPE_FOLLOW_NO_CENTER}
     *                     连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。 {@link MyLocationStyleType#LOCATION_TYPE_MAP_ROTATE_NO_CENTER}
     * @param mapSize      地图缩放比例
     * @see MyLocationStyleType
     * @see GaoDeMapLocationBean
     */
    public void setLocation(final Context context, final AMap aMap, boolean locationIcon, boolean locationBtn, boolean location, boolean traffic, int type, final int mapSize, final GaoDeOnClickListener.OnLocationClickListener listener) {
        //初始化定位蓝点样式类
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        if (type == MyLocationStyleType.LOCATION_TYPE_LOCATION_ROTATE) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        } else if (type == MyLocationStyleType.LOCATION_TYPE_SHOW) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        } else if (type == MyLocationStyleType.LOCATION_TYPE_LOCATE) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        } else if (type == MyLocationStyleType.LOCATION_TYPE_FOLLOW) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        } else if (type == MyLocationStyleType.LOCATION_TYPE_MAP_ROTATE) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
        } else if (type == MyLocationStyleType.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        } else if (type == MyLocationStyleType.LOCATION_TYPE_FOLLOW_NO_CENTER) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        } else if (type == MyLocationStyleType.LOCATION_TYPE_MAP_ROTATE_NO_CENTER) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);
        }
        //设置定位蓝点的 icon 图标
        if (bean.getIcon() != 0 || BitmapDescriptorFactory.fromResource(bean.getIcon()) != null) {
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(bean.getIcon()));
        }
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息
        myLocationStyle.showMyLocation(locationIcon);
        //设置定位蓝点精度圈的边框宽度的方法。
        myLocationStyle.strokeWidth(bean.getWidth());
//        //设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.strokeColor(bean.getStrokeColor());
//        //设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.radiusFillColor(bean.getRadiusFillColor());
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒
        myLocationStyle.interval(bean.getInterval());
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        //设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(locationBtn);
        //设置为 true 表示启动显示定位蓝点，false 表示隐藏定位蓝点并不进行定位，默认是 false
        aMap.setMyLocationEnabled(location);
        //是否显示交通
        aMap.setTrafficEnabled(traffic);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), mapSize));
                //逆地理编码获取详细信息
                GeocodeSearch geocodeSearch = new GeocodeSearch(context);
                geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        listener.onLocationClick(regeocodeResult);
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    }
                });
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
            }
        });
    }

    /**
     * 设置定位图标的锚点。 锚点是定位图标接触地图平面的点。图标的左顶点为（0,0）点，右底点为（1,1）点
     *
     * @param horizontal 锚点水平范围的比例，建议传入0 到1 之间的数值
     * @param vertical   锚点垂直范围的比例，建议传入0 到1 之间的数值
     * @return
     */
    public GaoDeLocationUtil anchor(float horizontal, float vertical) {
        bean.setHorizontal(horizontal);
        bean.setVertical(vertical);
        return this;
    }

    /**
     * 设置蓝点样式
     *
     * @param strokeColor     边框颜色 越小越淡  0 为透明
     * @param radiusFillColor 填充颜色 越小越淡  0 为透明
     * @param strokeWidth     边框宽度 0 不显示边框
     * @return
     */
    public GaoDeLocationUtil strokeColor(int strokeColor, int radiusFillColor, float strokeWidth) {
        bean.setStrokeColor(strokeColor);
        bean.setRadiusFillColor(radiusFillColor);
        bean.setWidth(strokeWidth);
        return this;
    }

    /**
     * 设置定位频次方法，
     *
     * @param interval 单位：毫秒，默认值：1000毫秒，如果传小于1000的任何值将按照1000计算。该方法只会作用在会执行连续定位的工作模式上
     */
    public GaoDeLocationUtil interval(long interval) {
        bean.setInterval(interval);
        return this;
    }

    /**
     * 设置定位蓝点的 icon 图标
     *
     * @param icon 图片 ID
     * @return
     */
    public GaoDeLocationUtil myLocationIcon(int icon) {
        bean.setIcon(icon);
        return this;
    }

}
