package com.xianzhi.map.activity.route.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DriveRoutePlanResult;
import com.xianzhi.map.R;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.base.BaseApplication;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.gaode.overlay.DrivingRoutePlanOverlay;
import com.xianzhi.map.gaode.util.util.TimeUtils;
import com.xianzhi.map.gaode.view.TravelView;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import butterknife.BindView;

/**
 * @author LiMing
 * @Demo class RoutePlanningActivity
 * @Description TODO
 * @date 2019-11-04 16:33
 */
public class RoutePlanningActivity extends BaseActivity {

	@BindView(R.id.mMapView)
	MapView mMapView;
	@BindView(R.id.travel_view)
	TravelView travelView;

	private AMap aMap;

	private GaoDeMapManager manager;

	@Override
	protected void onCreateView() {
		setContentView(R.layout.activiity_route_planning);
	}

	@Override
	protected void initView(@Nullable Bundle savedInstanceState) {
		//定位
		manager = GaoDeMapManager.getInstance(context, mMapView);
		manager.onCreate(savedInstanceState);
		manager.setLocation(new GaoDeOnClickListener.OnLocationActivityClickListener() {
			@Override
			public void onLocationClick(RegeocodeResult regeocodeResult) {

			}
		});
		//路线规划
		aMap = mMapView.getMap();
		final DriveRoutePlanResult result = BaseApplication.result;
		if (result != null && result.getPaths().size() > 0) {
			String time = getIntent().getStringExtra("time");
			travelView.setVisibility(View.VISIBLE);
			travelView.init(result, new TravelView.IndexListener() {
				@Override
				public void onClicked(int index) {
					if (result.getPaths().size() > 0) {
						DrivingRoutePlanOverlay drivingRouteOverlay = new DrivingRoutePlanOverlay(context, aMap, result, index);
						drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
						drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
						drivingRouteOverlay.removeFromMap();
						drivingRouteOverlay.addToMap();
						drivingRouteOverlay.zoomToSpan();

					} else if (result.getPaths() == null) {
						Toast.makeText(context, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
					}
				}
			}, TimeUtils.date2TimeStampTime(time, "yyyy-MM-dd HH:mm:ss"));
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
