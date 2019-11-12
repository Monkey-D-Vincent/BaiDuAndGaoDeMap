package com.xianzhi.map.gaode.util.car;

import android.util.Pair;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.xianzhi.map.R;

import java.util.List;

/**
 * @author LiMing
 * @Demo class CarMovingUtil
 * @Description TODO 小车平滑移动
 * @date 2019-11-07 11:40
 */
public class CarMovingUtil {

	private static CarMovingUtil util;
	private static List<LatLng> mPoints;
	private static AMap mAMap;
	private static int mIcon;
	private int mSecond;

	private static MovingPointOverlay smoothMarker;
	private Polyline mPolyline;

	private CarMovingUtil() {
	}

	/**
	 * @param aMap
	 * @param points 平滑移动的经纬度数组
	 * @param icon   移动Marker的图标
	 * @return
	 */
	public static CarMovingUtil getInstance(AMap aMap, List<LatLng> points, int icon) {
		mPoints = points;
		mAMap = aMap;
		mIcon = icon;
		if (util == null) {
			util = new CarMovingUtil();
		}
		return util;
	}


	/**
	 * 添加轨迹线
	 * @return
	 */
	public CarMovingUtil carMoving() {
		mPolyline = mAMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture))
				//setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
				.addAll(mPoints).useGradient(true).width(18));
		//线路是否可见
		mPolyline.setVisible(false);
		// 构建 轨迹的显示区域
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(mPoints.get(0));
		builder.include(mPoints.get(mPoints.size() - 2));
		mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
		return util;
	}

	/**
	 * 设置滑动的总时间
	 *
	 * @param second 单位：秒
	 * @return
	 */
	public CarMovingUtil setTotalDuration(int second) {
		mSecond = second;
		return util;
	}

	/**
	 * 开始平滑移动
	 */
	public void startSmoothMove() {
		if (mPolyline == null) {
			//请先设置路线
			return;
		}
		// 实例 MovingPointOverlay 对象
		if (smoothMarker == null) {
			// 设置 平滑移动的 图标
			Marker marker = mAMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(mIcon)).anchor(0.5f, 0.5f));
			smoothMarker = new MovingPointOverlay(mAMap, marker);
		}
		// 取轨迹点的第一个点 作为 平滑移动的启动
		LatLng drivePoint = mPoints.get(0);
		Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(mPoints, drivePoint);
		mPoints.set(pair.first, drivePoint);
		List<LatLng> subList = mPoints.subList(pair.first, mPoints.size());
		// 设置轨迹点
		smoothMarker.setPoints(subList);
		// 设置平滑移动的总时间  单位  秒
		smoothMarker.setTotalDuration(mSecond);
		// 设置移动的监听事件  返回 距终点的距离  单位 米
//		smoothMarker.setMoveListener(new MovingPointOverlay.MoveListener() {
//			@Override
//			public void move(final double distance) {
//				try {
//					"距离终点还有： " + (int) distance + "米"
//				} catch (Throwable e) {
//					e.printStackTrace();
//				}
//			}
//		});
		// 开始移动
		smoothMarker.startSmoothMove();
	}

	/**
	 * 停止平滑移动
	 */
	public void stopMove() {
		smoothMarker.stopMove();
		if (smoothMarker != null) {
			smoothMarker.setMoveListener(null);
			smoothMarker.destroy();
		}
	}

}
