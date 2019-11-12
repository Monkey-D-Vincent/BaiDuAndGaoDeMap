package com.xianzhi.map.gaode.listener;

import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;

/**
 * @author LiMing
 * @Demo class MyOnTrackListener
 * @Description TODO
 * @date 2019-11-05 16:05
 */
public class MyOnTrackListener implements OnTrackListener {

	/**
	 * 查询terminal的详细信息的回调
	 *
	 * @param queryTerminalResponse
	 */
	@Override
	public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

	}

	/**
	 * 创建terminal的回调
	 * @param addTerminalResponse
	 */
	@Override
	public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

	}

	/**
	 * 查询里程回调
	 * @param distanceResponse
	 */
	@Override
	public void onDistanceCallback(DistanceResponse distanceResponse) {

	}

	/**
	 * 查询终端最新轨迹点的回调
	 * @param latestPointResponse
	 */
	@Override
	public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

	}

	/**
	 * 查询历史轨迹的的回调
	 * @param historyTrackResponse
	 */
	@Override
	public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

	}

	/**
	 * 查询终端轨迹信息的回调
	 * @param queryTrackResponse
	 */
	@Override
	public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

	}

	/**
	 * 增加轨迹请求的回调
	 * @param addTrackResponse
	 */
	@Override
	public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

	}

	/**
	 * 参数错误回调
	 * @param paramErrorResponse
	 */
	@Override
	public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

	}
}
