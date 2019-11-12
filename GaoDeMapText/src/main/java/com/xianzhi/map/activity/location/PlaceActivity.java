package com.xianzhi.map.activity.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.maps.MapView;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xianzhi.map.R;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import butterknife.BindView;

/**
 * @author LiMing
 * @Demo class PlaceActivity
 * @Description TODO 定位
 * @date 2019-10-25 13:19
 */
public class PlaceActivity extends BaseActivity {

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
        manager.setLocation(new GaoDeOnClickListener.OnLocationActivityClickListener() {
            @Override
            public void onLocationClick(RegeocodeResult regeocodeResult) {

            }
        });
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
