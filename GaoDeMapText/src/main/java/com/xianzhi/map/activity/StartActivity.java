package com.xianzhi.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xianzhi.map.R;
import com.xianzhi.map.activity.car.CarMovingActivity;
import com.xianzhi.map.activity.location.PlaceActivity;
import com.xianzhi.map.activity.navigation.NavigationActivity;
import com.xianzhi.map.activity.route.base.RouteActivity;
import com.xianzhi.map.activity.search.SearchActivity;
import com.xianzhi.map.activity.track.TrackActivity;
import com.xianzhi.map.activity.track.TrackRunningActivity;
import com.xianzhi.map.base.BaseActivity;

import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class StartActivity
 * @Description TODO
 * @date 2019-09-25 13:51
 */
public class StartActivity extends BaseActivity {

	@Override
	protected void onCreateView() {
		setContentView(R.layout.activity_start);
	}

	@Override
	protected void initView(@Nullable Bundle savedInstanceState) {
	}

	@OnClick({R.id.tv_location, R.id.tv_search, R.id.tv_route, R.id.tv_navigation, R.id.tv_routeTrace, R.id.tv_car})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.tv_location:
				startActivity(new Intent(context, PlaceActivity.class));
				break;
			case R.id.tv_search:
				startActivity(new Intent(context, SearchActivity.class));
				break;
			case R.id.tv_route:
				startActivity(new Intent(context, RouteActivity.class));
				break;
			case R.id.tv_navigation:
				startActivity(new Intent(context, NavigationActivity.class));
				break;
			case R.id.tv_routeTrace:
				startActivity(new Intent(context, TrackActivity.class));
				break;
			case R.id.tv_car:
				startActivity(new Intent(context, CarMovingActivity.class));
				break;
		}
	}

}
