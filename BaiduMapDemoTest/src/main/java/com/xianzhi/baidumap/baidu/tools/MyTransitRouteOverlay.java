package com.xianzhi.baidumap.baidu.tools;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.xianzhi.baidumap.bean.RoutePlanningSearchBean;

/**
 * @author LiMing
 * @Demo class MyTransitRouteOverlay
 * @Description TODO 自定义起终点坐标图片
 * @date 2019-10-10 10:33
 */
public class MyTransitRouteOverlay extends DrivingRouteOverlay {

    private RoutePlanningSearchBean bean;

    public MyTransitRouteOverlay(BaiduMap baiduMap, RoutePlanningSearchBean bean) {
        super(baiduMap);
        this.bean = bean;
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        if (bean.isUseDefaultIcon()) {
            return BitmapDescriptorFactory.fromResource(bean.getStartIcon());
        }
        return null;
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        if (bean.isUseDefaultIcon()) {
            return BitmapDescriptorFactory.fromResource(bean.getEndIcon());
        }
        return null;
    }
}
