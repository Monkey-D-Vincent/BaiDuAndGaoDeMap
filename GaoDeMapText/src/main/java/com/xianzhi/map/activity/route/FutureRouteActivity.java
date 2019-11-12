package com.xianzhi.map.activity.route;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DriveRoutePlanResult;
import com.xianzhi.map.R;
import com.xianzhi.map.activity.navigation.NavigationActivity;
import com.xianzhi.map.activity.route.base.RoutePlanningActivity;
import com.xianzhi.map.adapter.SearchAdapter;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.base.BaseApplication;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.gaode.bean.FutureRouteBean;
import com.xianzhi.map.gaode.bean.SearchPlaceBean;
import com.xianzhi.map.gaode.util.route.FutureRouteModeUtil;
import com.xianzhi.map.gaode.util.util.TimeUtils;
import com.xianzhi.map.listener.GaoDeOnClickListener;
import com.xianzhi.map.view.RecycleViewDivider;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class FutureRouteActivity
 * @Description TODO 未来路线规划
 * @date 2019-11-01 17:16
 */
public class FutureRouteActivity extends BaseActivity implements GaoDeOnClickListener.OnSearchActivityClickListener, SearchAdapter.OnSearchClickListener {

	@BindView(R.id.et_start)
	EditText etStart;
	@BindView(R.id.et_end)
	EditText etEnd;
	@BindView(R.id.mMapView)
	MapView mMapView;
	@BindView(R.id.tv_msg)
	TextView tvMsg;
	@BindView(R.id.rv_address)
	RecyclerView rvAddress;

	private SearchAdapter adapter;
	private String city;
	private int position = 0;

	private GaoDeMapManager manager;
	private PoiItem startBean;
	private PoiItem endBean;
	private String time;

	@Override
	protected void onCreateView() {
		setContentView(R.layout.activity_navigation_controller);
	}

	@Override
	protected void initView(@Nullable Bundle savedInstanceState) {
		rvAddress.setLayoutManager(new LinearLayoutManager(context));
		rvAddress.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 3, getResources().getColor(R.color.white)));
		adapter = new SearchAdapter(context, this);
		rvAddress.setAdapter(adapter);

		manager = GaoDeMapManager.getInstance(context, mMapView);
		manager.onCreate(savedInstanceState);
		manager.setLocation(new GaoDeOnClickListener.OnLocationActivityClickListener() {
			@Override
			public void onLocationClick(RegeocodeResult regeocodeResult) {
				city = regeocodeResult.getRegeocodeAddress().getCity();
				if (regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getPois() != null && !regeocodeResult.getRegeocodeAddress().getPois().isEmpty()) {
					position = 1;
					etStart.setText(regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle());
					startBean = new PoiItem(regeocodeResult.getRegeocodeAddress().getPois().get(0).getPoiId(), regeocodeResult.getRegeocodeAddress().getPois().get(0).getLatLonPoint(), regeocodeResult.getRegeocodeAddress().getPois().get(0).getTitle(), regeocodeResult.getRegeocodeAddress().getPois().get(0).getSnippet());
				}
			}
		});
		setEditTextListener();
	}

	/**
	 * 开始导航  不要怀疑 就是这么一点代码  其他的都是内部处理的
	 */
	private void startNavigation() {
		time = TimeUtils.timeStamp2Date(new Date().getTime() + 1000 * 60 * 60 * 3, "");
		FutureRouteBean bean = new FutureRouteBean();
		bean.setStartLatLonPoint(startBean.getLatLonPoint());
		bean.setEndLatLonPoint(endBean.getLatLonPoint());
		bean.setCarType(0);
		bean.setCount(48);
		bean.setInterval(60 * 3);
		bean.setMode(FutureRouteModeUtil.AVOIDINGCONGESTIONANDROUTESHORYESTANDAVOIDPAYMONEY);
		bean.setTime(time);
		manager.setFutureRoutePlanning(context, bean, new GaoDeOnClickListener.OnFutureRouteActivityClickListener() {
			@Override
			public void onFutureRouteClick(DriveRoutePlanResult driveRoutePlanResult) {
				Intent intent = new Intent(context, RoutePlanningActivity.class);
				intent.putExtra("time", time);
				BaseApplication.result = driveRoutePlanResult;
				startActivity(intent);
			}
		});
	}

	private void setEditTextListener() {
		etStart.setFocusable(false);
		etStart.setFocusableInTouchMode(false);
		etEnd.setFocusable(true);
		etEnd.setFocusableInTouchMode(true);
		etStart.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (tvMsg.getVisibility() == View.VISIBLE) {
					tvMsg.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(s) && position != 1) {
					manager.searchPlace(getApplicationContext(), s.toString(), city, false, FutureRouteActivity.this);
				} else {
					rvAddress.setVisibility(View.GONE);
				}
				position = 2;
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		etEnd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (tvMsg.getVisibility() == View.VISIBLE) {
					tvMsg.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(s)) {
					manager.searchPlace(getApplicationContext(), s.toString(), city, false, FutureRouteActivity.this);
				} else {
					rvAddress.setVisibility(View.GONE);
				}
				position = 3;
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@OnClick({R.id.et_start, R.id.et_end, R.id.btn_start})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.et_start:
				etStart.setFocusable(true);
				etStart.setFocusableInTouchMode(true);
				break;
			case R.id.et_end:
				break;
			case R.id.btn_start:
				if (startBean == null) {
					Toast.makeText(context, "请输入出发点", Toast.LENGTH_SHORT).show();
				} else if (endBean == null) {
					Toast.makeText(context, "请输入目的地", Toast.LENGTH_SHORT).show();
				} else {
					startNavigation();
				}
				break;
		}
	}

	/**
	 * 查询结果
	 *
	 * @param bean
	 */
	@Override
	public void onSearchClick(SearchPlaceBean bean) {
		adapter.addData(bean.getPlaces());
		rvAddress.setVisibility(View.VISIBLE);
	}

	/**
	 * item 点击事件
	 *
	 * @param bean
	 */
	@Override
	public void onSearchClick(PoiItem bean) {
		if (bean != null) {
			if (position < 3 && position > 0) {
				startBean = bean;
				etStart.setText(bean.getTitle());
			} else if (position == 3) {
				etEnd.setText(bean.getTitle());
				endBean = bean;
			}
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
