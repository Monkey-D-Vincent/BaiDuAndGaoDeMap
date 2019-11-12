package com.xianzhi.map.gaode.util.track;

import android.content.Context;
import android.widget.Toast;

import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.AccuracyMode;
import com.amap.api.track.query.entity.CorrectMode;
import com.amap.api.track.query.entity.DenoiseMode;
import com.amap.api.track.query.entity.DriveMode;
import com.amap.api.track.query.entity.HistoryTrack;
import com.amap.api.track.query.entity.OrderMode;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.RecoupMode;
import com.amap.api.track.query.entity.ThresholdMode;
import com.amap.api.track.query.entity.Track;
import com.amap.api.track.query.model.HistoryTrackRequest;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;


import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.xianzhi.map.gaode.listener.MyOnTrackListener;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import java.util.List;

/**
 * @author LiMing
 * @Demo class TrackHistoryUtil
 * @Description TODO
 * @date 2019-11-06 13:07
 */
public class TrackHistoryUtil {

	private static TrackHistoryUtil util;

	private static AMapTrackClient client;
	private static Context mContext;
	private static long mServiceId;
	private static String mDeviceName;

	private long mStartTime;
	private long mEndTime;
	private int mCorrectMode;
	private int mRecoupMode;
	private int mGap;
	private int mOrderMode;
	private int mDenoiseMode;
	private int mThresholdMode;
	private int mDriveMode;
	private int mIspoints;
	private int mTrackId = -1;
	private int mPageNum;
	private int mPageSize = 100;
	private String mAccuracyMode = "";

	private TrackHistoryUtil() {
	}

	public static TrackHistoryUtil getInstance(Context context, long serviceId, String deviceName) {
		client = new AMapTrackClient(context);
		mContext = context;
		mServiceId = serviceId;
		mDeviceName = deviceName;
		if (util == null) {
			util = new TrackHistoryUtil();
		}
		return util;
	}

	/**
	 * 设置时间
	 *
	 * @param startTime 开始时间，unix时间戳，单位为毫秒
	 * @param endTime   结束时间，unix时间戳，单位为毫秒，注意，结束时间不能大于当前时间，且距离开始时间不能超过24小时
	 * @return
	 */
	public TrackHistoryUtil setDate(long startTime, long endTime) {
		mStartTime = startTime;
		mEndTime = endTime;
		return util;
	}

	/**
	 * 设置纠偏模式
	 *
	 * @param correction 采用驾车模式纠偏 {@link CorrectMode#DRIVING}
	 *                   不纠偏 {@link CorrectMode#NONE}
	 * @return
	 */
	public TrackHistoryUtil setCorrection(int correction) {
		mCorrectMode = correction;
		return util;
	}

	/**
	 * 设置进行补偿
	 *
	 * @param recoup 代表用驾车策略进行补点计算 {@link RecoupMode#DRIVING}
	 *               代表用直线距离进行补点计算 {@link RecoupMode#STRAIGHT_LINE}
	 * @return
	 */
	public TrackHistoryUtil setRecoup(int recoup) {
		mRecoupMode = recoup;
		return util;
	}

	/**
	 * 设置距离补偿生效的点间距
	 *
	 * @param gap 单位为米，范围必须在50m~10km，当两点间距离超过该值时，将启用距离补偿计算两点 间距离
	 */
	public TrackHistoryUtil setGap(int gap) {
		mGap = gap;
		return util;
	}

	/**
	 * 设置排序方式
	 *
	 * @param order 按照定位时间，由旧>新排序 {@link OrderMode#OLD_FIRST}
	 *              按照定位时间，由新>旧排序 {@link OrderMode#NEW_FIRST}
	 * @return
	 */
	public TrackHistoryUtil setOrder(int order) {
		mOrderMode = order;
		return util;
	}

	/**
	 * 查看数量 分页使用
	 *
	 * @param pageNum  需要第几页数据，若仅需要返回起点、终点的经纬度，请指定此参数为1
	 * @param pageSize 在每页返回的个数，注意page = 1时的起点、终点个数不计算在内，该值必须小于1000
	 * @return
	 */
	public TrackHistoryUtil setPage(int pageNum, int pageSize) {
		mPageNum = pageNum;
		mPageSize = pageSize;
		return util;
	}

	/**
	 * 根据轨迹点精度进行筛选
	 *
	 * @param accuracy 筛选条件 {@link AccuracyMode#createAccurationMode(int, int)} #}
	 *                 创建可作为accuracy参数传入的字符串
	 *                 格式：A-B、A-、-B
	 *                 例如：0-233 筛选accuracy在[0,233] 的最后一个点
	 *                 例如：3- 筛选accuracy≥3的最后一个点
	 *                 例如：-55 筛选accuracy≤55的最后一个点
	 *                 若不填写，则代表不进行筛选
	 *                 参数:
	 *                 startPos - 精度下限
	 *                 endPos - 精度上限
	 *                 返回:
	 *                 可作为accuracy参数传入的字符串
	 * @return
	 */
	public TrackHistoryUtil setAccuracy(String accuracy) {
		mAccuracyMode = accuracy;
		return util;
	}

	/**
	 * 设置轨迹 ID
	 *
	 * @param trackId -1 表示不指定，查询所有轨迹，注意分页仅在查询特定轨迹id时生效，查询所有轨迹时无法对轨迹点进行分页
	 * @return
	 */
	public TrackHistoryUtil setTrackId(int trackId) {
		mTrackId = trackId;
		return util;
	}

	/**
	 * 去噪模式
	 *
	 * @param denoise 去噪 {@link DenoiseMode#DENOSE}
	 *                不去噪 {@link DenoiseMode#NON_DENOSE}
	 * @return
	 */
	public TrackHistoryUtil setDenoise(int denoise) {
		mDenoiseMode = denoise;
		return util;
	}

	/**
	 * 是否返回轨迹包含的轨迹点内容
	 *
	 * @param ispoints 1表示返回，0表示不返回。分页当前仅对查询单条轨迹生效
	 * @return
	 */
	public TrackHistoryUtil setIspoints(int ispoints) {
		mIspoints = ispoints;
		return util;
	}

	/**
	 * 定位精度过滤 当取值=0时，则不过滤； 当取值大于0的整数时，则过滤掉radius大于设定值的轨迹点。
	 *
	 * @param threshold 若只需保留 GPS 定位点,则建议设为：20 {@link ThresholdMode#GPS_THRESHOLD}
	 *                  若需保留 GPS 和 Wi-Fi 定位点，去除基站定位点，则建议设为：100 {@link ThresholdMode#GPS_WIFI_THRESHOLD}
	 *                  可以为其他值
	 * @return
	 */
	public TrackHistoryUtil setThreshold(int threshold) {
		mThresholdMode = threshold;
		return util;
	}

	/**
	 * 交通方式，该参数决定纠偏策略，目前仅支持驾车模式
	 *
	 * @param driveMode 驾车模式 {@link DriveMode#DRIVING}
	 *                  骑行模式 {@link DriveMode#RIDING}
	 *                  步行模式 {@link DriveMode#WALKING}
	 * @return
	 */
	public TrackHistoryUtil setDriveMode(int driveMode) {
		mDriveMode = driveMode;
		return util;
	}

	/**
	 * 查询某个终端某段时间内的行驶轨迹及里程
	 * <p>
	 * 需要先调用以下方法：
	 * 始终时间 {@link TrackHistoryUtil#setDate(long, long)}
	 * 是否纠偏 {@link TrackHistoryUtil#setCorrection(int)}
	 * 距离补偿  {@link TrackHistoryUtil#setRecoup(int)}
	 * 距离补偿阈值 {@link TrackHistoryUtil#setGap(int)}
	 * 排序方式 {@link TrackHistoryUtil#setOrder(int)}
	 * 可以调用筛选方法 也可以不调用 {@link TrackHistoryUtil#setAccuracy(String)}
	 * 页数 每页数量 {@link TrackHistoryUtil#setPage(int, int)}
	 *
	 * @param listener
	 */
	public void searchHistory(final GaoDeOnClickListener.OnSearchTrackHistoryClickListener listener) {
		if (mStartTime >= mEndTime || mStartTime * 1000 * 60 * 60 * 24 == mEndTime) {
			Toast.makeText(mContext, "结束时间不能大于当前时间，且距离开始时间不能超过24小时", Toast.LENGTH_SHORT).show();
		} else {
			// 先查询terminal id，然后用terminal id查询轨迹
			// 查询符合条件的所有轨迹，并分别绘制
			client.queryTerminal(new QueryTerminalRequest(mServiceId, mDeviceName), new MyOnTrackListener() {
				@Override
				public void onQueryTerminalCallback(final QueryTerminalResponse queryTerminalResponse) {
					if (queryTerminalResponse.isSuccess()) {
						if (queryTerminalResponse.isTerminalExist()) {
							long tid = queryTerminalResponse.getTid();
							// 搜索最近12小时以内上报的属于某个轨迹的轨迹点信息，散点上报不会包含在该查询结果中
							HistoryTrackRequest queryTrackRequest = new HistoryTrackRequest(mServiceId, tid, mStartTime, mEndTime, //时间
									mCorrectMode, //纠偏模式
									mRecoupMode,      // 是否进行距离补偿
									mGap,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
									mOrderMode,  // 排序方式
									mPageNum,  // 返回第1页数据
									mPageSize,    // 一页不超过100条
									mAccuracyMode // 筛选
							);
							client.queryHistoryTrack(queryTrackRequest, new MyOnTrackListener() {
								@Override
								public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
									if (historyTrackResponse.isSuccess()) {
										HistoryTrack historyTrack = historyTrackResponse.getHistoryTrack();
										if (historyTrack == null || historyTrack.getCount() == 0) {
											Toast.makeText(mContext, "未获取到轨迹点", Toast.LENGTH_SHORT).show();
											return;
										}
										listener.onTrackHistory(historyTrack.getPoints());
									} else {
										Toast.makeText(mContext, "查询历史轨迹点失败，" + historyTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
									}
								}
							});
						} else {
							Toast.makeText(mContext, "Terminal不存在", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(mContext, "网络请求失败，错误原因: " + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}

	/**
	 * 查询某个终端的某几条轨迹信息
	 * 能够查询某个终端的某条轨迹，提供多种自定义参数查询方式。
	 * 1、 id 查询轨迹信息:以轨迹 id 查询轨迹信息，最多支持查询 1 条轨迹;
	 * 2、 时间段查询轨迹信息:传递设备 id，查询在一定时间内的(最大时间检索跨度为 24h)开始记录的所有设备轨迹;
	 * 3、 分段查询轨迹信息:可以分段查询指定的一条轨迹(通过 trid 指定轨迹)，设置查询的时间间隔(通过 starttime 和endtime) 用于轨迹的分段。在分段查询中，trid、 starttime、endtime 均为必填信息。
	 * <p>
	 * 需要先调用以下方法：
	 * 轨迹 ID {@link TrackHistoryUtil#setTrackId(int)}
	 * 始终时间 {@link TrackHistoryUtil#setDate(long, long)}
	 * 是否去噪 {@link TrackHistoryUtil#setDenoise(int)}
	 * 是否纠偏 {@link TrackHistoryUtil#setCorrection(int)}
	 * 是否进行精度过滤 {@link TrackHistoryUtil#setThreshold(int)}
	 * 当前仅支持驾车模式  {@link TrackHistoryUtil#setDriveMode(int)}
	 * 距离补偿  {@link TrackHistoryUtil#setRecoup(int)}
	 * 距离补偿阈值 {@link TrackHistoryUtil#setGap(int)}
	 * 结果应该包含轨迹点信息 {@link TrackHistoryUtil#setIspoints(int)}
	 * 页数 每页数量 {@link TrackHistoryUtil#setPage(int, int)}
	 */
	public void setSearchHistoryPoint(final GaoDeOnClickListener.OnSearchTrackHistoryPointClickListener listener) {
		// 先查询terminal id，然后用terminal id查询轨迹
		// 查询符合条件的所有轨迹，并分别绘制
		client.queryTerminal(new QueryTerminalRequest(mServiceId, mDeviceName), new MyOnTrackListener() {
			@Override
			public void onQueryTerminalCallback(final QueryTerminalResponse queryTerminalResponse) {
				if (queryTerminalResponse.isSuccess()) {
					if (queryTerminalResponse.isTerminalExist()) {
						long tid = queryTerminalResponse.getTid();
						// 搜索最近12小时以内上报的属于某个轨迹的轨迹点信息，散点上报不会包含在该查询结果中
						QueryTrackRequest queryTrackRequest = new QueryTrackRequest(mServiceId, tid, mTrackId,     // 轨迹 ID
								mStartTime, mEndTime, // 时间
								mDenoiseMode,      // 是否去噪
								mCorrectMode,   // 是否纠偏
								mThresholdMode,      // 是否进行精度过滤
								mDriveMode,  // 当前仅支持驾车模式
								mRecoupMode,     // 距离补偿
								mGap,   // 距离补偿，只有超过5km的点才启用距离补偿
								mIspoints,  // 结果应该包含轨迹点信息
								mPageNum,  // 返回第1页数据，但由于未指定轨迹，分页将失效
								mPageSize    // 一页不超过100条
						);
						client.queryTerminalTrack(queryTrackRequest, new MyOnTrackListener() {
							@Override
							public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
								if (queryTrackResponse.isSuccess()) {
									List<Track> tracks = queryTrackResponse.getTracks();
									if (tracks != null && !tracks.isEmpty()) {
										boolean allEmpty = true;
										for (Track track : tracks) {
											List<Point> points = track.getPoints();
											if (points != null && points.size() > 0) {
												allEmpty = false;
											}
										}
										if (allEmpty) {
											Toast.makeText(mContext, "所有轨迹都无轨迹点，请尝试放宽过滤限制，如：关闭绑路模式", Toast.LENGTH_SHORT).show();
										} else {
//											StringBuilder sb = new StringBuilder();
//											sb.append("共查询到").append(tracks.size()).append("条轨迹，每条轨迹行驶距离分别为：");
//											for (Track track : tracks) {
//												sb.append(track.getDistance()).append("m,");
//											}
//											sb.deleteCharAt(sb.length() - 1);
//											Toast.makeText(mContext, sb.toString(), Toast.LENGTH_SHORT).show();
											listener.onTrackHistory(tracks);
										}
									} else {
										Toast.makeText(mContext, "未获取到轨迹", Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(mContext, "查询历史轨迹失败，" + queryTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
								}
							}
						});
					} else {
						Toast.makeText(mContext, "Terminal不存在", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(mContext, "网络请求失败，错误原因: " + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


}
