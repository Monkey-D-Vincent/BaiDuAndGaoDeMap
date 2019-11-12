package com.xianzhi.map.activity.track;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.MapView;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xianzhi.map.R;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import butterknife.BindView;

/**
 * @author LiMing
 * @Demo class TrackActivity
 * @Description TODO 轨迹追踪
 * @date 2019-11-05 17:32
 */
public class TrackRunningActivity extends BaseActivity {

    @BindView(R.id.mMapView)
    MapView mMapView;

    private GaoDeMapManager manager;

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_track_running);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        manager = GaoDeMapManager.getInstance(context, mMapView);
        manager.onCreate(savedInstanceState);
        //处理唯一标识符  10.0 以前可以使用 IMEI   10.0 以后使用 UUID   逻辑自己处理吧  我就不写了
        manager.getParmas(context, "708c88f67591084f6ebb42440fa04f7a", "测试", "测试使用", 3 * 60, 5 * 60);
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
        manager.stopGather();
        manager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }

}
