package com.xianzhi.map.gaode.util.track;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.entity.LocationMode;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.google.gson.Gson;
import com.xianzhi.map.base.BaseApplication;
import com.xianzhi.map.gaode.bean.GaoDeTrackBean;
import com.xianzhi.map.gaode.listener.MyOnTrackLifecycleListener;
import com.xianzhi.map.gaode.listener.MyOnTrackListener;
import com.xianzhi.map.gaode.view.NotificationView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LiMing
 * @Demo class GaoDeTrackUtil
 * @Description TODO 路线追踪
 * @date 2019-11-05 13:52
 */
public class GaoDeTrackUtil {

	private GaoDeTrackUtil2 util2;
	private static GaoDeTrackUtil util;
	private static Context mContext;
	private static String mKey;
	private static String mTitle;
	private static String mContent;
	private long terminalId;

	private static AMapTrackClient client;

	private GaoDeTrackUtil() {
	}

	/**
	 * @param context
	 * @return
	 */
	public static GaoDeTrackUtil getInstance(Context context) {
		client = new AMapTrackClient(context);
		mContext = context;
		if (util == null) {
			util = new GaoDeTrackUtil();
		}
		return util;
	}

	/**
	 * 请求参数 用来获取 BaseApplication.SERVICEID  必须调用
	 * key=708c88f67591084f6ebb42440fa04f7a&name=测试&desc=测试使用
	 *
	 * @param key
	 * @param title
	 * @param content
	 * @return
	 */
	public GaoDeTrackUtil2 getPostRequest(String key, String title, String content) {
		mKey = key;
		mTitle = title;
		mContent = content;
		util2 = new GaoDeTrackUtil2();
		return util2;
	}

	public class GaoDeTrackUtil2 {

		private GaoDeTrackUtil2() {
			postDataWithRequest();
		}

		/**
		 * 配置定位采集周期和上报周期 {@link AMapTrackClient#setInterval(int, int)}
		 * 猎鹰sdk默认的定位信息采集周期是2s，默认的上报周期是20s，也就是最快2s记录一次当前位置信息（若位置没有变化，这次位置信息会被忽略），20s上报一次记录下的这些信息。
		 * 可以使用AMapTrackClient的setInterval方法修改该配置，注意定位信息采集周期的范围应该是1s~60s，上报周期的范围是采集周期的5～50倍。
		 * 下面的代码将定位信息采集周期设置为5s，上报周期设置为30s：
		 *
		 * @param location 定位周期 单位：秒
		 * @param upload   上报周期 单位：秒
		 * @return
		 */
		public GaoDeTrackUtil2 setInterval(int location, int upload) {
			client.setInterval(location, upload);
			return util2;
		}

		/**
		 * 配置本地缓存大小 {@link AMapTrackClient#setCacheSize(int)}
		 * 猎鹰sdk会在无法正常上报轨迹点时将未成功上报的轨迹点缓存在本地，默认最多缓存50MB数据。
		 * 可以使用下面的代码修改缓存大小为20MB：
		 *
		 * @param cacheSize
		 * @return
		 */
		public GaoDeTrackUtil2 setCacheSize(int cacheSize) {
			client.setCacheSize(cacheSize);
			return util2;
		}

		/**
		 * 配置定位模式 {@link AMapTrackClient#setLocationMode(int)}
		 *
		 * @param mode 低功耗定位模式：在这种模式下，将只使用高德网络定位 {@link LocationMode#BATTERY_SAVING}
		 *             仅设备定位模式：在这种模式下，将只使用GPS定位 {@link LocationMode#DEVICE_SENSORS}
		 *             高精度定位模式：在这种定位模式下，将同时使用高德网络定位和GPS定位,优先返回精度高的定位 默认模式 不设置则进行此模式定位 {@link LocationMode#HIGHT_ACCURACY}
		 * @return
		 */
		public GaoDeTrackUtil2 setLocationMode(int mode) {
			client.setLocationMode(mode);
			return util2;
		}

		/**
		 * 开启服务  开启采集已经在回调接口调用
		 */
		public void startTrack() {
			// 先根据Terminal名称查询Terminal ID，如果Terminal还不存在，就尝试创建，拿到Terminal ID后，
			// 用Terminal ID开启轨迹服务
			client.queryTerminal(new QueryTerminalRequest(BaseApplication.SERVICEID, BaseApplication.DEVICENAME), new MyOnTrackListener() {
				@Override
				public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
					if (queryTerminalResponse.isSuccess()) {
						if (queryTerminalResponse.getTid() <= 0) {
							// terminal还不存在，先创建
							client.addTerminal(new AddTerminalRequest(BaseApplication.DEVICENAME, BaseApplication.SERVICEID), new MyOnTrackListener() {
								@Override
								public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
									if (addTerminalResponse.isSuccess()) {
										// 创建完成，开启猎鹰服务
										terminalId = addTerminalResponse.getTid();
										TrackParam trackParam = new TrackParam(BaseApplication.SERVICEID, terminalId);
										if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
											trackParam.setNotification(NotificationView.createNotification(mContext));
										}
										client.startTrack(trackParam, myOnTrackLifecycleListener);
									} else {
										Toast.makeText(mContext, "网络请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
									}
								}
							});
						} else {
							// terminal已经存在，直接开启猎鹰服务
							terminalId = queryTerminalResponse.getTid();
							client.startTrack(new TrackParam(BaseApplication.SERVICEID, terminalId), myOnTrackLifecycleListener);
						}
					} else {
						Toast.makeText(mContext, "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		/**
		 * 停止采集 关闭服务已经在回调接口调用
		 */
		public void stopGather() {
			client.stopGather(myOnTrackLifecycleListener);
		}

		/**
		 * 停止
		 */
		public void onDestroy() {
			client.stopTrack(new TrackParam(BaseApplication.SERVICEID, terminalId), new MyOnTrackLifecycleListener());
		}

		private MyOnTrackLifecycleListener myOnTrackLifecycleListener = new MyOnTrackLifecycleListener() {

			/**
			 * 绑定服务回调接口
			 *
			 * @param i
			 * @param s
			 */
			@Override
			public void onBindServiceCallback(int i, String s) {

			}

			/**
			 * 开启采集回调接口
			 *
			 * @param i
			 * @param s
			 */
			@Override
			public void onStartGatherCallback(int i, String s) {
				if (i == ErrorCode.TrackListen.START_GATHER_SUCEE || i == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
				} else {
					Toast.makeText(mContext, "定位采集启动异常，" + s, Toast.LENGTH_SHORT).show();
				}
			}

			/**
			 * 开启服务回调接口
			 *
			 * @param i
			 * @param s
			 */
			@Override
			public void onStartTrackCallback(int i, String s) {
				if (i == ErrorCode.TrackListen.START_TRACK_SUCEE || i == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK || i == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
					// 服务启动成功，继续开启收集上报
					client.startGather(this);
				} else {
					Toast.makeText(mContext, "轨迹上报服务服务启动异常，" + s, Toast.LENGTH_SHORT).show();
				}
			}

			/**
			 * 停止采集回调接口
			 *
			 * @param i
			 * @param s
			 */
			@Override
			public void onStopGatherCallback(int i, String s) {
				if (i == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
					client.stopTrack(new TrackParam(BaseApplication.SERVICEID, terminalId), myOnTrackLifecycleListener);
				}
			}

			/**
			 * 停止服务回调接口
			 *
			 * @param i
			 * @param s
			 */
			@Override
			public void onStopTrackCallback(int i, String s) {

			}
		};

		private void postDataWithRequest() {
			if (BaseApplication.SERVICEID != -1) {
				startTrack();
			} else {
				OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
				FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
				formBody.add("key", mKey);//传递键值对参数
				formBody.add("name", mTitle);//传递键值对参数
				formBody.add("desc", mContent);//传递键值对参数
				final Request request = new Request.Builder()//创建Request 对象。
						.url("https://tsapi.amap.com/v1/track/service/add").post(formBody.build())//传递请求体
						.build();
				client.newCall(request).enqueue(new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {

					}

					@Override
					public void onResponse(Call call, Response response) throws IOException {
						String json = response.body().string();
						GaoDeTrackBean bean = new Gson().fromJson(json, GaoDeTrackBean.class);
						if (bean != null && bean.getErrcode() == 10000) {
							BaseApplication.SERVICEID = bean.getData().getSid();
							BaseApplication.DEVICENAME = bean.getData().getName();
						}
					}
				});
			}
		}
	}

}
