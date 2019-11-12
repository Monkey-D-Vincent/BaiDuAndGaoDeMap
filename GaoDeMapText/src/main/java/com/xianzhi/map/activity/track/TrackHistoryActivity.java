package com.xianzhi.map.activity.track;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.track.query.entity.CorrectMode;
import com.amap.api.track.query.entity.OrderMode;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.RecoupMode;
import com.xianzhi.map.R;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.base.BaseApplication;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class TrackHistoryActivity
 * @Description TODO
 * @date 2019-11-06 11:16
 */
public class TrackHistoryActivity extends BaseActivity {

	@BindView(R.id.mapView)
	MapView mapView;
	@BindView(R.id.tracks)
	RecyclerView tracks;

	private GaoDeMapManager manager;

	@Override
	protected void onCreateView() {
		setContentView(R.layout.activity_track_history);
	}

	@Override
	protected void initView(@Nullable Bundle savedInstanceState) {

		manager = GaoDeMapManager.getInstance(context, mapView);
		manager.onCreate(savedInstanceState);
		manager.setLocation(new GaoDeOnClickListener.OnLocationActivityClickListener() {
			@Override
			public void onLocationClick(RegeocodeResult regeocodeResult) {
				manager.searchTrackHistory(context, BaseApplication.SERVICEID, BaseApplication.DEVICENAME, System.currentTimeMillis() - 12 * 60 * 60 * 1000, System.currentTimeMillis(), CorrectMode.DRIVING, RecoupMode.STRAIGHT_LINE, 5000, OrderMode.NEW_FIRST, 1, 100, new GaoDeOnClickListener.OnSearchTrackHistoryActivityClickListener() {
					@Override
					public void onTrackHistory(List<Point> points) {
						Log.i("Aaaaaaaaaaaaaaaaaaaaaa", points.toString());
					}
				});
			}
		});
	}

//	private void drawTrackOnMap(List<Point> points) {
//		LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//		PolylineOptions polylineOptions = new PolylineOptions();
//		polylineOptions.color(Color.BLUE).width(20);
//		if (points.size() > 0) {
//			// 起点
//			Point p = points.get(0);
//			LatLng latLng = new LatLng(p.getLat(), p.getLng());
//			MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//			endMarkers.add(textureMapView.getMap().addMarker(markerOptions));
//		}
//		if (points.size() > 1) {
//			// 终点
//			Point p = points.get(points.size() - 1);
//			LatLng latLng = new LatLng(p.getLat(), p.getLng());
//			MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//			endMarkers.add(textureMapView.getMap().addMarker(markerOptions));
//		}
//		for (Point p : points) {
//			LatLng latLng = new LatLng(p.getLat(), p.getLng());
//			polylineOptions.add(latLng);
//			boundsBuilder.include(latLng);
//		}
//		Polyline polyline = textureMapView.getMap().addPolyline(polylineOptions);
//		polylines.add(polyline);
//		textureMapView.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
//	}
//
//	private void clearTracksOnMap() {
//		for (Polyline polyline : polylines) {
//			polyline.remove();
//		}
//		for (Marker marker : endMarkers) {
//			marker.remove();
//		}
//		endMarkers.clear();
//		polylines.clear();
//	}

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
