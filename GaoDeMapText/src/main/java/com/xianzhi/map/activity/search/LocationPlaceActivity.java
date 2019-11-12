package com.xianzhi.map.activity.search;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.xianzhi.map.R;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.gaode.bean.GaoDeMarkerBean;

import butterknife.BindView;

/**
 * @author LiMing
 * @Demo class LocationPlaceActivity
 * @Description TODO 搜索结果定位
 * @date 2019-10-31 10:36
 */
public class LocationPlaceActivity extends BaseActivity {

    @BindView(R.id.mapView)
    MapView mapView;

    private GaoDeMapManager manager;

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_place);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        manager = GaoDeMapManager.getInstance(context, mapView);
        manager.onCreate(savedInstanceState);
        if(getIntent().getParcelableExtra("data") != null) {
            PoiItem bean = getIntent().getParcelableExtra("data");
            LatLng latLng = new LatLng(bean.getLatLonPoint().getLatitude(), bean.getLatLonPoint().getLongitude());
            manager.setLocationPlace(latLng, 16);
            GaoDeMarkerBean markerBean = new GaoDeMarkerBean();
            markerBean.setIcon(R.drawable.icon_gcoding);
            markerBean.setLatLng(latLng);
            manager.setMarker(context, markerBean);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }
}
