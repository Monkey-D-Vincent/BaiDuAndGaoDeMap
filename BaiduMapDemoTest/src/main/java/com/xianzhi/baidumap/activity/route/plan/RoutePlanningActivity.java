package com.xianzhi.baidumap.activity.route.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.baidu.BaiduLocationManager;
import com.xianzhi.baidumap.baidu.BaiduRoutePlanningManager;
import com.xianzhi.baidumap.baidu.tools.DrivingRouteOverlay;
import com.xianzhi.baidumap.baidu.tools.MyTransitRouteOverlay;
import com.xianzhi.baidumap.base.BaseActivity;
import com.xianzhi.baidumap.bean.RoutePlanningSearchBean;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.view.RoutePlanningListDialog;
import com.xianzhi.baidumap.view.RouteTypePopup;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class RoutePlanningActivity
 * @Description TODO 路线规划 - 显示列表
 * @date 2019-09-29 16:21
 */
public class RoutePlanningActivity extends BaseActivity implements BaiduLocationListener.OnLocationClickListener, RouteTypePopup.OnRouteTypeClickListener, BaiduLocationListener.OnRoutePlanningSearchClickListener {

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.mMapView)
    MapView mMapView;

    private RoutePlanningSearchBean bean = new RoutePlanningSearchBean();
    private RouteTypePopup popup;

    /**
     * 驾车路线规划参数
     */
    private DrivingRoutePlanOption mDrivingRoutePlanOption;
    /**
     * 搜索模块，也可去掉地图模块独立使用
     */
    private RoutePlanSearch mSearch;
    private int type = 0;
    //是否定位成功
    private boolean isLocation = false;
    private BaiduMap baiduMap;

    private boolean isFirstLoc = true;

    @Override
    protected void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_planning);
    }

    @Override
    protected void initView() {
        baiduMap = mMapView.getMap();
        popup = new RouteTypePopup(context, this, 0);
        tvStart.setText("定位当前位置...");

        //定位获取所在城市，进行检索
        LocationClient client = new LocationClient(getApplicationContext());
        BaiduLocationManager.initLocationOption(client, this);
        // 创建路线规划Option   // 设置参数前创建
        mDrivingRoutePlanOption = new DrivingRoutePlanOption();
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();

    }

    @OnClick({R.id.tv_start, R.id.tv_end, R.id.tv_tag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                if (isLocation) {
                    startActivityForResult(new Intent(context, RoutePlanningSearchActivity.class), 100);
                }
                break;
            case R.id.tv_end:
                if (isLocation) {
                    startActivityForResult(new Intent(context, RoutePlanningSearchActivity.class), 200);
                }
                break;
            case R.id.tv_tag:
                popup.showAsDropDown(tvTag);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null && data.getParcelableExtra("data") != null) {
            PoiInfo poiInfo = data.getParcelableExtra("data");
            if (requestCode == 100 && resultCode == 100) {
                tvStart.setText(poiInfo.getName());
                bean.setStartName(poiInfo.getName());
                bean.setStartCity(poiInfo.getCity());
            } else if (requestCode == 200 && resultCode == 100) {
                tvEnd.setText(poiInfo.getName());
                bean.setEndName(poiInfo.getName());
                bean.setEndCity(poiInfo.getCity());
            }
            if (!TextUtils.isEmpty(bean.getStartName()) && !TextUtils.isEmpty(bean.getEndName()) && !tvEnd.getText().toString().equals("你要去哪儿")) {
                BaiduRoutePlanningManager.routePlanning(getApplicationContext(), mDrivingRoutePlanOption, mSearch, bean, type, this);
            }
        }
    }

    /**
     * 定位当前位置
     *
     * @param location
     */
    @Override
    public void onLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null) {
            return;
        }
        //获取纬度信息
        double mCurrentLat = location.getLatitude();
        //获取经度信息
        double mCurrentLon = location.getLongitude();
        /**定位显示*/
        if (isFirstLoc) {
            isFirstLoc = false;
            /**定位显示*/
            LatLng cenpt = new LatLng(mCurrentLat, mCurrentLon);
            //定位显示
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(cenpt).zoom(18.0f);
            baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        mMapView.setVisibility(View.VISIBLE);
        //获取位置描述信息
        tvStart.setText(location.getLocationDescribe());
        bean.setStartCity(location.getCity());
        bean.setStartName(location.getLocationDescribe());
        isLocation = true;
    }

    @Override
    public void onRouteTypeClick(int index, String name) {
        popup.dismiss();
        type = index;
        tvTag.setText(name);
        if (!TextUtils.isEmpty(bean.getStartName()) && !TextUtils.isEmpty(bean.getEndName()) && !tvEnd.getText().toString().equals("你要去哪儿")) {
            BaiduRoutePlanningManager.routePlanning(getApplicationContext(), mDrivingRoutePlanOption, mSearch, bean, type, this);
        }
    }

    /**
     * 显示路线
     * @param bean
     */
    private void displayRoute(DrivingRouteLine bean) {
        //显示路线规划
        baiduMap.clear();
        RoutePlanningSearchBean searchBean = new RoutePlanningSearchBean();
        searchBean.setStartIcon(R.drawable.icon_start);
        searchBean.setEndIcon(R.drawable.icon_end);
        searchBean.setUseDefaultIcon(true);
        DrivingRouteOverlay overlay = new MyTransitRouteOverlay(baiduMap, searchBean);
        baiduMap.setOnMarkerClickListener(overlay);
        overlay.setData(bean);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    @Override
    public void onSearchClick(List<DrivingRouteLine> routePlans) {
        //多条线路
        if(routePlans.size() > 1) {
            RoutePlanningListDialog dialog = new RoutePlanningListDialog(context, R.style.myDialog, routePlans, new RoutePlanningListDialog.OnRouteClickListener() {
                @Override
                public void onRouteClick(DrivingRouteLine bean) {
                    displayRoute(bean);
                }
            });
            dialog.show();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = display.getWidth(); //设置宽度
            lp.gravity = Gravity.BOTTOM;
            dialog.getWindow().setAttributes(lp);
        } else if(routePlans.size() == 1) {
            //只有一条线路
            displayRoute(routePlans.get(0));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearch != null) {
            mSearch.destroy();
        }
        baiduMap.clear();
        mMapView.onDestroy();
    }
}
