package com.xianzhi.baidumap.activity.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.struct.BNLocationData;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.activity.route.plan.RoutePlanningSearchActivity;
import com.xianzhi.baidumap.baidu.BaiduLocationManager;
import com.xianzhi.baidumap.baidu.BaiduNavigationManager;
import com.xianzhi.baidumap.baidu.tools.LocationController;
import com.xianzhi.baidumap.base.BaseActivity;
import com.xianzhi.baidumap.bean.BaiduNavigationBean;
import com.xianzhi.baidumap.fragment.NavigationSearchFragment;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.view.RouteTypePopup;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class NavigationSearchActivity
 * @Description TODO 导航
 * @date 2019-10-12 14:26
 */
public class NavigationSearchActivity extends BaseActivity implements BaiduLocationListener.OnLocationClickListener, RouteTypePopup.OnRouteTypeClickListener {

    @BindView(R.id.frag_controller)
    FrameLayout fragController;
    @BindView(R.id.ll_retry)
    LinearLayout llRetry;
    @BindView(R.id.fl_filed)
    FrameLayout flFiled;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_tag)
    TextView tvTag;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private BaiduNavigationBean bean = new BaiduNavigationBean();
    private RouteTypePopup popup;

    private boolean isLocation = false;
    private boolean isFirstLoc = true;

    @Override
    protected void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_navigation_controller);
    }

    @Override
    protected void initView() {
        popup = new RouteTypePopup(context, this, 1);

        //定位获取所在城市，进行检索
        LocationClient client = new LocationClient(context);
        BaiduLocationManager.initLocationOption(client, this);

        addBaiDuMap();
    }

    /**
     * 添加百度 map
     */
    private void addBaiDuMap() {
        if(BaiduNaviManagerFactory.getMapManager() != null) {
            View map = BaiduNaviManagerFactory.getMapManager().getMapView();
            if (map != null && map.getParent() != null) {
                ((ViewGroup) map.getParent()).removeView(map);
            }
            fragController.addView(map);
//            initExtGpsData();
        }
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null && data.getParcelableExtra("data") != null) {
            PoiInfo poiInfo = data.getParcelableExtra("data");
            if (requestCode == 100 && resultCode == 100) {
                tvStart.setText(poiInfo.getName());
                bean.setStartPlace(poiInfo.getName());
                bean.setStartPlaceDesc(!TextUtils.isEmpty(poiInfo.getDirection()) ? poiInfo.getDirection() : bean.getStartPlace());
                bean.setStartLat(poiInfo.getLocation().latitude);
                bean.setStartLng(poiInfo.getLocation().longitude);
            } else if (requestCode == 200 && resultCode == 100) {
                tvEnd.setText(poiInfo.getName());
                bean.setEndPlace(poiInfo.getProvince());
                bean.setEndPlaceDesc(!TextUtils.isEmpty(poiInfo.getName()) ? poiInfo.getName() : bean.getStartPlace());
                bean.setEndLat(poiInfo.getLocation().latitude);
                bean.setEndLng(poiInfo.getLocation().longitude);
            }
            if (!TextUtils.isEmpty(bean.getStartPlace()) && !TextUtils.isEmpty(bean.getEndPlace()) && !tvEnd.getText().toString().equals("你要去哪儿")) {
                BaiduNavigationManager.setNavigation(bean, new BaiduLocationListener.OnNavigationClickListener() {
                    @Override
                    public void onNavigationClick(int info) {
                        if(info == 0) {
                            flFiled.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "算路失败", Toast.LENGTH_SHORT).show();
                        } else if(info == 10) {
                            flFiled.setVisibility(View.GONE);
                            manager = getSupportFragmentManager();
                            transaction = manager.beginTransaction();
                            NavigationSearchFragment fragment = new NavigationSearchFragment();
                            transaction.add(R.id.frag_content, fragment, "RouteResult");
                            transaction.commit();
                        }
                    }
                });
            }
        }
    }

    /**
     * 搜索地址
     * @param index
     * @param name
     */
    @Override
    public void onRouteTypeClick(int index, String name) {
        popup.dismiss();
        tvTag.setText(name);
        bean.setType(index);
        if (!TextUtils.isEmpty(bean.getStartPlace()) && !TextUtils.isEmpty(bean.getEndPlace()) && !tvEnd.getText().toString().equals("你要去哪儿")) {
            BaiduNavigationManager.setNavigation(bean, new BaiduLocationListener.OnNavigationClickListener() {
                @Override
                public void onNavigationClick(int info) {
                    if(info == 0) {
                        Toast.makeText(getApplicationContext(), "算路失败", Toast.LENGTH_SHORT).show();
                    } else if(info == 10) {
                        manager = getSupportFragmentManager();
                        transaction = manager.beginTransaction();
                        NavigationSearchFragment fragment = new NavigationSearchFragment();
                        transaction.add(R.id.frag_content, fragment, "RouteResult");
                        transaction.commit();
                    }
                }
            });
        }
    }

    /**
     * 定位
     *
     * @param location
     */
    @Override
    public void onLocation(BDLocation location) {
        if (location == null) {
            return;
        }
        isLocation = true;
        if(isFirstLoc) {
            //获取位置描述信息
            tvStart.setText(location.getLocationDescribe());
            bean.setStartPlace(location.getAddress().city);
            bean.setStartPlaceDesc(!TextUtils.isEmpty(location.getLocationDescribe()) ? location.getLocationDescribe() : bean.getStartPlace());
            bean.setStartLat(location.getLatitude());
            bean.setStartLng(location.getLongitude());
            isFirstLoc = false;
            initExtGpsData(location);
        }

    }

    /**
     * 定位
     */
    private void initExtGpsData(BDLocation location) {
        // 开启使用外部GPS数据
        BaiduNaviManagerFactory.getBaiduNaviManager().externalLocation(true);
//        // 传入外部GPS数据,请尽量按以下方式传入数据参数，缺失数据会影响GPS的准确性，造成定位不准。
//        // 必须使用WGS84坐标系
//        LocationController.getInstance().startLocation(getApplication());
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        BNLocationData mLocData = new BNLocationData.Builder()
                .latitude(lat)
                .longitude(lng)
                .accuracy(18)
                .speed(location.getSpeed())
                .direction(location.getDirection())
                .altitude((int) location.getAltitude())
                .build();
        BaiduNaviManagerFactory.getMapManager().setMyLocationData(mLocData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaiduNaviManagerFactory.getMapManager().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaiduNaviManagerFactory.getMapManager().onPause();
    }

}
