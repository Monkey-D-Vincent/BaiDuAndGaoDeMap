package com.xianzhi.map.listener;

import android.location.Location;
import android.view.View;

import com.amap.api.maps.model.Marker;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DriveRoutePlanResult;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.Track;
import com.xianzhi.map.gaode.bean.RouteCorrectionBean;
import com.xianzhi.map.gaode.bean.SearchPlaceBean;

import java.util.List;

/**
 * @author LiMing
 * @Demo class GaoDeOnClickListener
 * @Description TODO
 * @date 2019-10-29 11:08
 */
public interface GaoDeOnClickListener {

	/**
	 * 定位
	 */
	interface OnLocationClickListener {
		void onLocationClick(RegeocodeResult location);
	}

	/**
	 * 定位
	 */
	interface OnLocationActivityClickListener {
		void onLocationClick(RegeocodeResult location);
	}

	/**
	 * 搜索
	 */
	interface OnSearchClickListener {
		void onSearchClick(SearchPlaceBean bean);
	}

	/**
	 * 搜索
	 */
	interface OnSearchActivityClickListener {
		void onSearchClick(SearchPlaceBean bean);
	}

	/**
	 * 自定义 infoWindow
	 */
	interface OnRenderInfoWindowClickListener {
		void onRenderInfoWindowClick(Marker marker, View view);
	}

	/**
	 * 自定义 infoWindow
	 */
	interface OnRenderInfoWindowActivityClickListener {
		void onRenderInfoWindowClick(Marker marker, View view);
	}

	/**
	 * infoWindow 点击事件
	 */
	interface OnInfoWindowClickListener {
		void onInfoWindowClick(Marker marker);
	}

	/**
	 * infoWindow 点击事件
	 */
	interface OnInfoWindowActivityClickListener {
		void onInfoWindowClick(Marker marker);
	}

	/**
	 * marker 点击事件
	 */
	interface OnMarkerClickListener {
		void onMarkerClick(Marker marker);
	}

	/**
	 * marker 点击事件
	 */
	interface OnMarkerActivityClickListener {
		void onMarkerClick(Marker marker);
	}

	/**
	 * marker 拖拽事件
	 */
	interface OnMarkerDraggablegClickListener {
		/**
		 * 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
		 * 这个位置可能与拖动的之前的marker位置不一样。
		 * marker 被拖动的marker对象。
		 *
		 * @param marker
		 */
		void onMarkerDragStart(Marker marker);

		/**
		 * 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
		 * 这个位置可能与拖动的之前的marker位置不一样。
		 * marker 被拖动的marker对象。
		 *
		 * @param marker
		 */
		void onMarkerDragEnd(Marker marker);

		/**
		 * 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
		 * 这个位置可能与拖动的之前的marker位置不一样。
		 * marker 被拖动的marker对象。
		 *
		 * @param marker
		 */
		void onMarkerDrag(Marker marker);
	}

	/**
	 * marker 拖拽事件
	 */
	interface OnMarkerDraggablegActivityClickListener {
		/**
		 * 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
		 * 这个位置可能与拖动的之前的marker位置不一样。
		 * marker 被拖动的marker对象。
		 *
		 * @param marker
		 */
		void onMarkerDragStart(Marker marker);

		/**
		 * 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
		 * 这个位置可能与拖动的之前的marker位置不一样。
		 * marker 被拖动的marker对象。
		 *
		 * @param marker
		 */
		void onMarkerDragEnd(Marker marker);

		/**
		 * 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
		 * 这个位置可能与拖动的之前的marker位置不一样。
		 * marker 被拖动的marker对象。
		 *
		 * @param marker
		 */
		void onMarkerDrag(Marker marker);
	}

	/**
	 * 未来路线规划
	 */
	interface OnFutureRouteClickListener {
		void onFutureRouteClick(DriveRoutePlanResult driveRoutePlanResult);
	}

	/**
	 * 未来路线规划
	 */
	interface OnFutureRouteActivityClickListener {
		void onFutureRouteClick(DriveRoutePlanResult driveRoutePlanResult);
	}

	/**
	 * 查询某个终端某段时间内的行驶轨迹及里程
	 */
	interface OnSearchTrackHistoryClickListener {
		void onTrackHistory(List<Point> points);
	}

	/**
	 * 查询某个终端某段时间内的行驶轨迹及里程
	 */
	interface OnSearchTrackHistoryActivityClickListener {
		void onTrackHistory(List<Point> points);
	}

	/**
	 * 查询某个终端的某几条轨迹信息
	 */
	interface OnSearchTrackHistoryPointClickListener {
		void onTrackHistory(List<Track> points);
	}

	/**
	 * 查询某个终端的某几条轨迹信息
	 */
	interface OnSearchTrackHistoryPointActivityClickListener {
		void onTrackHistory(List<Track> points);
	}

	/**
	 * g轨迹纠偏
	 */
	interface OnRouteCorrectionClickListener {
		void onCorrectionClick(RouteCorrectionBean bean);
	}

	/**
	 * g轨迹纠偏
	 */
	interface OnRouteCorrectionActivityClickListener {
		void onCorrectionClick(RouteCorrectionBean bean);
	}
}
