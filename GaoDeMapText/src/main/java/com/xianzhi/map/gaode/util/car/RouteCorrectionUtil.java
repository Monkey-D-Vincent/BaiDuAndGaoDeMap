package com.xianzhi.map.gaode.util.car;

import android.content.Context;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.xianzhi.map.gaode.bean.RouteCorrectionBean;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author liMing
 * @Demo class RouteCorrectionUtil
 * @Description TODO 轨迹纠偏
 * @date 2019-11-08 14:41
 */
public class RouteCorrectionUtil {

	private static RouteCorrectionUtil util;
	private static Context mContext;
	private static AMap mAMap;
	private static List<TraceLocation> mTraceLocations;

	private LBSTraceClient mTraceClient;
	private int mSequenceLineID = 1;
	private ConcurrentMap<Integer, TraceOverlay> mOverlayList = new ConcurrentHashMap<>();

	private RouteCorrectionUtil() {
		mTraceClient = new LBSTraceClient(mContext);
	}

	/**
	 * 初始化
	 *
	 * @param context
	 * @param aMap
	 * @param traceLocations 1、必填信息的缺失会导致纠偏失败，非必填信息的缺失会在一定程度影响最终纠偏结果，因此尽可能的多提供以下信息是确保绘制一条平滑轨迹的最佳方案。
	 *                       建议使用Android定位SDK中高精度，且有速度和角度返回的位置点数据。
	 *                       2、传入的经纬度点，必须是国内的坐标，轨迹纠偏功能不支持国外的坐标点的纠偏。
	 *                       <p>
	 *                       必须存在的参数:
	 *                       设置经度 {@link TraceLocation#setLongitude(double)}
	 *                       设置纬度 {@link TraceLocation#setLatitude(double)}
	 *                       设置速度 {@link TraceLocation#setSpeed(float)}
	 *                       设置方向角 {@link TraceLocation#setBearing(float)}
	 *                       设置时间 {@link TraceLocation#setTime(long)}
	 * @return
	 */
	public static RouteCorrectionUtil getInstance(Context context, AMap aMap, List<TraceLocation> traceLocations) {
		mContext = context;
		mTraceLocations = traceLocations;
		mAMap = aMap;

		if (util == null) {
			util = new RouteCorrectionUtil();
		}
		return util;
	}

	/**
	 * {@link LBSTraceClient#queryProcessedTrace(int, List, int, TraceListener)}说明：
	 * lineID： 用于标示一条轨迹，支持多轨迹纠偏，如果多条轨迹调起纠偏接口，则lineID需不同。
	 * locations ： 一条轨迹的点集合。建议为一条行车GPS高精度定位轨迹。
	 * type： 	 :  轨迹坐标系。
	 * listener  ： 轨迹纠偏回调。
	 * <p>
	 * 当出现网络不连通和原始轨迹数据只有1个点的时候会导致纠偏失败。
	 *
	 * @param type     只支持以下三种:
	 *                 高德 {@link LBSTraceClient#TYPE_AMAP}
	 *                 GPS {@link LBSTraceClient#TYPE_GPS}
	 *                 百度 {@link LBSTraceClient#TYPE_BAIDU}
	 * @param listener
	 */
	public void queryProcessedTrace(int type, final GaoDeOnClickListener.OnRouteCorrectionClickListener listener) {
		if (mOverlayList.containsKey(mSequenceLineID)) {
			TraceOverlay overlay = mOverlayList.get(mSequenceLineID);
			overlay.zoopToSpan();
			int status = overlay.getTraceStatus();
			String tipString = "";
			if (status == TraceOverlay.TRACE_STATUS_PROCESSING) {
				tipString = "该线路轨迹纠偏进行中...";
				setDistanceWaitInfo(overlay, listener);
			} else if (status == TraceOverlay.TRACE_STATUS_FINISH) {
				setDistanceWaitInfo(overlay, listener);
				tipString = "该线路轨迹已完成";
			} else if (status == TraceOverlay.TRACE_STATUS_FAILURE) {
				tipString = "该线路轨迹失败";
			} else if (status == TraceOverlay.TRACE_STATUS_PREPARE) {
				tipString = "该线路轨迹纠偏已经开始";
			}
			Toast.makeText(mContext, tipString, Toast.LENGTH_SHORT).show();
			return;
		}
		TraceOverlay mTraceOverlay = new TraceOverlay(mAMap);
		mOverlayList.put(mSequenceLineID, mTraceOverlay);
		List<LatLng> mapList = traceLocationToMap(mTraceLocations);
		mTraceOverlay.setProperCamera(mapList);
		mTraceClient.queryProcessedTrace(mSequenceLineID, mTraceLocations, type, new TraceListener() {
			/**
			 * 轨迹纠偏失败回调
			 * @param lineID 用于标示一条轨迹，支持多轨迹纠偏，如果多条轨迹调起纠偏接口，则lineID需不同
			 * @param errorInfo 轨迹纠偏失败原因
			 */
			@Override
			public void onRequestFailed(int lineID, String errorInfo) {
				Toast.makeText(mContext, errorInfo, Toast.LENGTH_SHORT).show();
				if (mOverlayList.containsKey(lineID)) {
					TraceOverlay overlay = mOverlayList.get(lineID);
					overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_FAILURE);
					setDistanceWaitInfo(overlay, listener);
				}
			}

			/**
			 * 轨迹纠偏过程回调
			 * @param lineID 用于标示一条轨迹，支持多轨迹纠偏，如果多条轨迹调起纠偏接口，则lineID需不同
			 * @param index 一条轨迹分割为多个段,标示当前轨迹段索引
			 * @param segments 一条轨迹分割为多个段，segments标示当前轨迹段经过纠偏后经纬度点集合
			 */
			@Override
			public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {
				if (segments == null) {
					return;
				}
				if (mOverlayList.containsKey(lineID)) {
					TraceOverlay overlay = mOverlayList.get(lineID);
					overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_PROCESSING);
					overlay.add(segments);
				}
			}

			/**
			 * 轨迹纠偏结束回调
			 * @param lineID 用于标示一条轨迹，支持多轨迹纠偏，如果多条轨迹调起纠偏接口，则lineID需不同
			 * @param linepoints 整条轨迹经过纠偏后点的经纬度集合
			 * @param distance 轨迹经过纠偏后总距离，单位米
			 * @param watingtime 该轨迹中间停止时间，以GPS速度为参考，单位秒
			 */
			@Override
			public void onFinished(int lineID, List<LatLng> linepoints, int distance, int watingtime) {
				if (mOverlayList.containsKey(lineID)) {
					TraceOverlay overlay = mOverlayList.get(lineID);
					overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_FINISH);
					overlay.setDistance(distance);
					overlay.setWaitTime(watingtime);
					setDistanceWaitInfo(overlay, listener);
				}
			}
		});
	}

	/**
	 * 轨迹纠偏点转换为地图LatLng
	 *
	 * @param traceLocationList
	 * @return
	 */
	private List<LatLng> traceLocationToMap(List<TraceLocation> traceLocationList) {
		List<LatLng> mapList = new ArrayList<>();
		for (TraceLocation location : traceLocationList) {
			LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
			mapList.add(latlng);
		}
		return mapList;
	}

	/**
	 * 设置显示总里程和等待时间
	 *
	 * @param overlay
	 */
	private void setDistanceWaitInfo(TraceOverlay overlay, GaoDeOnClickListener.OnRouteCorrectionClickListener listener) {
		int distance = -1;
		int waittime = -1;
		if (overlay != null) {
			distance = overlay.getDistance();
			waittime = overlay.getWaitTime();
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		RouteCorrectionBean bean = new RouteCorrectionBean();
		bean.setDis(distance);
		bean.setTime(waittime);
		bean.setTimeContent("等   待：" + decimalFormat.format(waittime / 60d) + "分钟");
		bean.setDisContent("总距离：" + decimalFormat.format(distance / 1000d) + "KM");
		listener.onCorrectionClick(bean);
	}

}
