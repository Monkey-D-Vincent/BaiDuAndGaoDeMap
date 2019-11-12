package com.xianzhi.map.activity.car;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.google.gson.Gson;
import com.xianzhi.map.R;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.bean.CarMovingBean;
import com.xianzhi.map.bean.RouteCorrectionListBean;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.gaode.bean.RouteCorrectionBean;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LiMing
 * @Demo class CarMovingActivity
 * @Description TODO 小车平滑移动 和 轨迹纠偏
 * @date 2019-11-07 11:32
 */
public class CarMovingActivity extends BaseActivity {

	@BindView(R.id.mMapView)
	MapView mMapView;
	@BindView(R.id.tv_dis)
	TextView tvDis;
	@BindView(R.id.tv_time)
	TextView tvTime;

	private GaoDeMapManager manager;
	private List<TraceLocation> traceLocations = new ArrayList<>();
	private List<LatLng> cars = new ArrayList<>();

	@Override
	protected void onCreateView() {
		setContentView(R.layout.activity_car_moving);
	}

	@Override
	protected void initView(@Nullable Bundle savedInstanceState) {

		manager = GaoDeMapManager.getInstance(context, mMapView);
		manager.onCreate(savedInstanceState);
		manager.setLocation(new GaoDeOnClickListener.OnLocationActivityClickListener() {
			@Override
			public void onLocationClick(RegeocodeResult regeocodeResult) {
				carMoving();
			}
		});
	}

	private void carMoving() {
		CarMovingBean carMovingBean = new Gson().fromJson(getAssetsData("json"), CarMovingBean.class);
		for (int i = 0; i < carMovingBean.getData().size(); i++) {
			cars.add(new LatLng(carMovingBean.getData().get(i).getLatitude(), carMovingBean.getData().get(i).getLongitude()));
		}
		RouteCorrectionListBean routeCorrection = new Gson().fromJson(getAssetsData("RouteCorrection"), RouteCorrectionListBean.class);
		for (int i = 0; i < routeCorrection.getData().size(); i++) {
			TraceLocation traceLocation = new TraceLocation();
			traceLocation.setTime(routeCorrection.getData().get(i).getTime());
			traceLocation.setBearing(routeCorrection.getData().get(i).getRearing());
			traceLocation.setSpeed(routeCorrection.getData().get(i).getSpeed());
			traceLocation.setLongitude(routeCorrection.getData().get(i).getLongitude());
			traceLocation.setLatitude(routeCorrection.getData().get(i).getLatitude());
			traceLocations.add(traceLocation);
		}
		manager.setRouteCorrection(context, traceLocations, LBSTraceClient.TYPE_AMAP, new GaoDeOnClickListener.OnRouteCorrectionActivityClickListener() {
			@Override
			public void onCorrectionClick(RouteCorrectionBean bean) {
				manager.setCarMoving(cars, R.drawable.icon_car, bean.getTime());
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

	private String getAssetsData(String fileName) {
		String result = "";
		try {
			//获取输入流
			InputStream mAssets = getAssets().open(fileName);
			//获取文件的字节数
			int lenght = mAssets.available();
			//创建byte数组
			byte[] buffer = new byte[lenght];
			//将文件中的数据写入到字节数组中
			mAssets.read(buffer);
			mAssets.close();
			result = new String(buffer);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("fuck", "error");
			return result;
		}
	}

}
