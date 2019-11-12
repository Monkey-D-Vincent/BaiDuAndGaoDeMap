package com.xianzhi.map.gaode;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DriveRoutePlanResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.track.query.entity.Point;
import com.xianzhi.map.gaode.bean.FutureRouteBean;
import com.xianzhi.map.gaode.bean.GaoDeMarkerBean;
import com.xianzhi.map.gaode.bean.RouteCorrectionBean;
import com.xianzhi.map.gaode.bean.SearchPlaceBean;
import com.xianzhi.map.gaode.util.car.CarMovingUtil;
import com.xianzhi.map.gaode.util.car.RouteCorrectionUtil;
import com.xianzhi.map.gaode.util.loction.GaoDeLocationUtil;
import com.xianzhi.map.gaode.util.customize.GaoDeMarkerUtil;
import com.xianzhi.map.gaode.util.route.FutureRouteModeUtil;
import com.xianzhi.map.gaode.util.route.FutureRouteUtil;
import com.xianzhi.map.gaode.util.search.GaoDeSearchUnit;
import com.xianzhi.map.gaode.util.loction.MyLocationStyleType;
import com.xianzhi.map.gaode.util.track.GaoDeTrackUtil;
import com.xianzhi.map.gaode.util.track.TrackHistoryUtil;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author LiMing
 * @Demo class GaoDeMapManager
 * @Description TODO
 * @date 2019-10-25 13:23
 */
public class GaoDeMapManager {

	private static GaoDeMapManager manager = new GaoDeMapManager();

	private GaoDeTrackUtil.GaoDeTrackUtil2 gaoDeTrackUtil2;
	private CarMovingUtil carMovingUtil;

	private static MapView mMapView;
	private AMap mAMap;
	private static Context mContext;

	/*************************定位相关************************/

	private GaoDeMapManager() {
	}

	public static GaoDeMapManager getInstance(Context context, MapView mapView) {
		if (mapView != null) {
			mMapView = mapView;
		}
		mContext = context;
		return manager;
	}

	public void onCreate(Bundle savedInstanceState) {
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
			mAMap = mMapView.getMap();
		}
	}

	public void setLocation(final GaoDeOnClickListener.OnLocationActivityClickListener listener) {
		new GaoDeLocationUtil().setLocation(mContext, mAMap, true, true, true, true, MyLocationStyleType.LOCATION_TYPE_LOCATE, 16, new GaoDeOnClickListener.OnLocationClickListener() {
			@Override
			public void onLocationClick(RegeocodeResult regeocodeResult) {
				listener.onLocationClick(regeocodeResult);
			}
		});
	}

	/*************************查询相关************************/

	/**
	 * @param context
	 * @param keyWord
	 * @param city
	 * @param restrict 是否限制当前城市内搜索
	 * @param listener
	 */
	public void searchPlace(Context context, String keyWord, String city, final boolean restrict, final GaoDeOnClickListener.OnSearchActivityClickListener listener) {
		GaoDeSearchUnit.getInstance(context, keyWord, city).setCityLimit(restrict).setSearchListener(new GaoDeOnClickListener.OnSearchClickListener() {
			@Override
			public void onSearchClick(SearchPlaceBean bean) {
				listener.onSearchClick(bean);
			}
		}).getPlace();
	}

	/**
	 * 定位到某一个点
	 *
	 * @param latLng
	 * @param mapSize 比例尺
	 */
	public void setLocationPlace(LatLng latLng, int mapSize) {
		mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapSize));
	}

	/**
	 * 设置自定义 marker
	 *
	 * @param bean
	 * @see GaoDeMarkerBean
	 */
	public void setMarker(Context context, GaoDeMarkerBean bean) {
		GaoDeMarkerUtil.getInstance(mAMap).setPosition(bean.getLatLng()).setIcon(context, bean.getIcon()).customizeMarker();
	}

	/*************************路线规划相关************************/

	/**
	 * 未来行程规划
	 *
	 * @param context
	 * @param bean
	 * @see FutureRouteUtil
	 */
	public void setFutureRoutePlanning(Context context, FutureRouteBean bean, final GaoDeOnClickListener.OnFutureRouteActivityClickListener listener) {
		RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(bean.getStartLatLonPoint(), bean.getEndLatLonPoint());
		long date = 0;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bean.getTime()).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		FutureRouteUtil.getInstance(context).setPlaceInfo(fromAndTo).setCarType(bean.getCarType()).setMode(bean.getMode()).setCount(bean.getCount()).setInterval(bean.getInterval()).setTravel((int) date).getFutureRouteData(new GaoDeOnClickListener.OnFutureRouteClickListener() {
			@Override
			public void onFutureRouteClick(DriveRoutePlanResult driveRoutePlanResult) {
				listener.onFutureRouteClick(driveRoutePlanResult);
			}
		});
	}

	/*************************归集采集相关************************/

	/**
	 * 获取数据  serviceId {@link GaoDeTrackUtil#getPostRequest(String, String, String)}
	 *
	 * @param context
	 * @param key     用户在高德地图官网申请Web服务API类型KEY 注意：是 web 的  不是 Android 的。
	 * @param title   Service 的名字，名字在同一个 Key 下不可重复，不可为空。命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字,不能以"_"开头，最长不得超过128个字符。 必须
	 * @param content 针对此 Service 的文字描述，方便用户对 Service 进行记忆。命名规则：仅支持中文、英文大小字母、英文下划线"_"、英文横线"-"和数字, 不能以"_"开头，最长不得超过128个字符。
	 */
	public void getParmas(Context context, String key, String title, String content, int location, int upload) {
		gaoDeTrackUtil2 = GaoDeTrackUtil.getInstance(context).getPostRequest(key, title, content).setInterval(location, upload);
	}

	/**
	 * 家属采集 {@link GaoDeTrackUtil.GaoDeTrackUtil2#stopGather()}
	 */
	public void stopGather() {
		gaoDeTrackUtil2.stopGather();
	}

	/**
	 * 查询某个终端某段时间内的行驶轨迹及里程
	 *
	 * @param context
	 * @param serviceId
	 * @param deviceName
	 * @param startIime
	 * @param endTime
	 * @param correction 是否纠偏 {@link TrackHistoryUtil#setCorrection(int)}
	 * @param recoup     距离补偿 {@link TrackHistoryUtil#setRecoup(int)}
	 * @param gap        距离补偿阈值 {@link TrackHistoryUtil#setGap(int)}
	 * @param order      排序方式 {@link TrackHistoryUtil#setOrder(int)}
	 * @param pageNum
	 * @param pageSize
	 * @param listener
	 */
	public void searchTrackHistory(Context context, long serviceId, String deviceName, long startIime, long endTime, int correction, int recoup, int gap, int order, int pageNum, int pageSize, final GaoDeOnClickListener.OnSearchTrackHistoryActivityClickListener listener) {
		TrackHistoryUtil.getInstance(context, serviceId, deviceName).setDate(startIime, endTime).setCorrection(correction).setRecoup(recoup).setGap(gap).setOrder(order).setPage(pageNum, pageSize).searchHistory(new GaoDeOnClickListener.OnSearchTrackHistoryClickListener() {
			@Override
			public void onTrackHistory(List<Point> points) {
				listener.onTrackHistory(points);
			}
		});
	}

	/*************************轨迹纠偏************************/

	/**
	 * 轨迹纠偏
	 *
	 * @param context
	 * @param traceLocations 1、必填信息的缺失会导致纠偏失败，非必填信息的缺失会在一定程度影响最终纠偏结果，因此尽可能的多提供以下信息是确保绘制一条平滑轨迹的最佳方案。
	 *                       建议使用Android定位SDK中高精度，且有速度和角度返回的位置点数据。
	 *                       2、传入的经纬度点，必须是国内的坐标，轨迹纠偏功能不支持国外的坐标点的纠偏。
	 *                       <p>
	 *                       必须存在的参数:
	 *                       设置经度 {@link TraceLocation#setLongitude(double)}
	 *                       设置纬度 {@link TraceLocation#setLatitude(double)}
	 *                       设置速度 {@link TraceLocation#setSpeed(float)}
	 *                       设置方向角 {@link TraceLocation#setBearing(float)}
	 *                       设置时间 {@link TraceLocation#setTime(long)} @param listener
	 * @param type           只支持一下三种:
	 *                       高德 {@link LBSTraceClient#TYPE_AMAP}
	 *                       GPS {@link LBSTraceClient#TYPE_GPS}
	 *                       百度 {@link LBSTraceClient#TYPE_BAIDU}
	 */
	public void setRouteCorrection(Context context, List<TraceLocation> traceLocations, int type, final GaoDeOnClickListener.OnRouteCorrectionActivityClickListener listener) {
		RouteCorrectionUtil.getInstance(context, mAMap, traceLocations).queryProcessedTrace(type, new GaoDeOnClickListener.OnRouteCorrectionClickListener() {
			@Override
			public void onCorrectionClick(RouteCorrectionBean bean) {
				listener.onCorrectionClick(bean);
			}
		});
	}

	/*************************小车平滑移动************************/

	/**
	 * 小车平滑移动
	 *
	 * @param points 经纬度
	 * @param icon   小车图标
	 * @param second 设置滑动的总时间 单位：秒
	 */
	public void setCarMoving(List<LatLng> points, int icon, int second) {
		carMovingUtil = CarMovingUtil.getInstance(mAMap, points, icon).carMoving().setTotalDuration(second);
		carMovingUtil.startSmoothMove();
	}

	public void onResume() {
		if (mMapView != null) {
			mMapView.onResume();
		}
	}

	public void onPause() {
		if (mMapView != null) {
			mMapView.onPause();
		}
	}

	public void onDestroy() {
		if (mMapView != null) {
			mMapView.onDestroy();
		}
		if (gaoDeTrackUtil2 != null) {
			gaoDeTrackUtil2.onDestroy();
		}
		if (carMovingUtil != null) {
			carMovingUtil.stopMove();
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		if (mMapView != null) {
			mMapView.onSaveInstanceState(outState);
		}
	}

}
