package com.xianzhi.baidumap.baidu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.xianzhi.baidumap.baidu.tools.BaiduCommonUtil;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.tools.NetUtil;
import com.xianzhi.baidumap.tools.SharedUtils;

/**
 * @author LiMing
 * @Demo class BaiduRouteTraceManager
 * @Description TODO
 * @date 2019-10-16 15:47
 */
public class BaiduRouteTraceManager {

    // 轨迹服务ID
    private static long serviceId;
    // 设备标识
    private static String entityName;
    // 请求标识
    private static int tag;
    //定位请求  反正我是让他的解释弄懵逼了
    private static LocRequest locRequest = null;
    private static LBSTraceClient client;
    private static BaiduMapUtil baiduMapUtil;
    private static Trace mTrace;

    /**
     * 试试定位判断
     */
    private static boolean isRealTimeRunning;
    private static RealTimeHandler realTimeHandler = new RealTimeHandler();
    private static MyRouteThread myRouteThread;

    private static Context context;
    private static SharedUtils sharedUtils;

    private static BaiduRouteTraceManager manager;
    private int index = 0;

    private BaiduRouteTraceManager() {
        sharedUtils = new SharedUtils(context);
    }

    /**
     * @param c              context
     * @param lbsTraceClient
     * @param id             serviceId
     * @param name           entityName
     * @param index          tag
     * @param util           baiduMapUtil
     * @return
     */
    public static BaiduRouteTraceManager getInstance(Context c, LBSTraceClient lbsTraceClient, long id, String name, int index, BaiduMapUtil util, Trace trace) {
        context = c;
        client = lbsTraceClient;
        serviceId = id;
        entityName = name;
        tag = index;
        baiduMapUtil = util;
        mTrace = trace;

        if (manager == null) {
            return new BaiduRouteTraceManager();
        }
        return manager;
    }

    /**
     * 开启服务
     */
    public void startService(BaiduLocationListener.OnRouteTraceClickListener listener) {
        client.startTrace(mTrace, new MyOnTraceListener(listener));
    }

    /**
     * 先结束采集
     */
    public void stopService(BaiduLocationListener.OnRouteTraceClickListener listener) {
        index = 1;
        client.stopGather(new MyOnTraceListener(listener));
        client.stopTrace(mTrace, new MyOnTraceListener(listener));
    }

    private void stopTrace(BaiduLocationListener.OnRouteTraceClickListener listener) {
        index = 2;
        client.stopTrace(mTrace, new MyOnTraceListener(listener));
    }

    /**
     * 轨迹服务监听器
     */
    private class MyOnTraceListener implements OnTraceListener {

        private BaiduLocationListener.OnRouteTraceClickListener listener;

        public MyOnTraceListener(BaiduLocationListener.OnRouteTraceClickListener listener) {
            this.listener = listener;
        }

        /**
         * 绑定服务回调接口
         *
         * @param i 状态码
         * @param s 消息
         *          <p>
         *          <pre>0：成功 </pre>
         *          <pre>1：失败</pre>
         */
        @Override
        public void onBindServiceCallback(int i, String s) {

        }

        /**
         * 开启服务回调接口
         *
         * @param i 状态码
         * @param s 消息
         *          <p>
         *          <pre>0：成功 </pre>
         *          <pre>10000：请求发送失败</pre>
         *          <pre>10001：服务开启失败</pre>
         *          <pre>10002：参数错误</pre>
         *          <pre>10003：网络连接失败</pre>
         *          <pre>10004：网络未开启</pre>
         *          <pre>10005：服务正在开启</pre>
         *          <pre>10006：服务已开启</pre>
         */
        @Override
        public void onStartTraceCallback(int i, String s) {
            Log.i("baiduaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "开始了");
            // 因为startTrace与startGather是异步执行，且startGather依赖startTrace执行开启服务成功，所以建议startGather在public void onStartTraceCallback(int errorNo, String message)回调返回错误码为0后，再进行调用执行，否则会出现服务开启失败12002的错误。
            if (StatusCodes.SUCCESS == i || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= i) {
                sharedUtils.setIsTraceStarted(true);
                // 开启采集
                client.startGather(null);
                //回调  注册
                listener.onRouteTraceClick(0);
            }
        }

        /**
         * 停止服务回调接口
         *
         * @param i 状态码
         * @param s 消息
         *          <p>
         *          <pre>0：成功</pre>
         *          <pre>11000：请求发送失败</pre>
         *          <pre>11001：服务停止失败</pre>
         *          <pre>11002：服务未开启</pre>
         *          <pre>11003：服务正在停止</pre>
         */
        @Override
        public void onStopTraceCallback(int i, String s) {
            Log.i("baiduaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "结束了");
            if (StatusCodes.SUCCESS == i || StatusCodes.CACHE_TRACK_NOT_UPLOAD == i) {
                // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                sharedUtils.setIsGatherStarted(false);
                sharedUtils.setIsTraceStarted(false);
                listener.onRouteTraceClick(1);
                //回调 接触注册
//                if (index == 1) {
//                    stopTrace(listener);
//                } else if (index == 2) {
//                    listener.onRouteTraceClick(1);
//                }
            }
        }

        /**
         * 开启采集回调接口
         *
         * @param i 状态码
         * @param s 消息
         *          <p>
         *          <pre>0：成功</pre>
         *          <pre>12000：请求发送失败</pre>
         *          <pre>12001：采集开启失败</pre>
         *          <pre>12002：服务未开启</pre>
         */
        @Override
        public void onStartGatherCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.GATHER_STARTED == i) {
                sharedUtils.setIsGatherStarted(true);
            }
        }

        /**
         * 停止采集回调接口
         *
         * @param i 状态码
         * @param s 消息
         *          <p>
         *          <pre>0：成功</pre>
         *          <pre>13000：请求发送失败</pre>
         *          <pre>13001：采集停止失败</pre>
         *          <pre>13002：服务未开启</pre>
         */
        @Override
        public void onStopGatherCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.GATHER_STOPPED == i) {
                sharedUtils.setIsGatherStarted(false);
            }
        }

        /**
         * 推送消息回调接口
         *
         * @param b     状态码
         * @param pushs 消息
         *              <p>
         *              <pre>0x01：配置下发</pre>
         *              <pre>0x02：语音消息</pre>
         *              <pre>0x03：服务端围栏报警消息</pre>
         *              <pre>0x04：本地围栏报警消息</pre>
         *              <pre>0x05~0x40：系统预留</pre>
         *              <pre>0x41~0xFF：开发者自定义</pre>
         */
        @Override
        public void onPushCallback(byte b, PushMessage pushs) {
            if (b < 0x03 || b > 0x04) {
                return;
            }
            FenceAlarmPushInfo alarmPushInfo = pushs.getFenceAlarmPushInfo();
            if (null == alarmPushInfo) {
                return;
            }
            //回调
//            StringBuffer alarmInfo = new StringBuffer();
//            alarmInfo.append("您于").append(CommonUtil.getHMS(alarmPushInfo.getCurrentPoint().getLocTime() * 1000)).append(alarmPushInfo.getMonitoredAction() == MonitoredAction.enter ? "进入" : "离开").append(messageType == 0x03 ? "云端" : "本地").append("围栏：").append(alarmPushInfo.getFenceName());
//
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//                Notification notification = new Notification.Builder(trackApp).setContentTitle(getResources().getString(R.string.alarm_push_title)).setContentText(alarmInfo.toString()).setSmallIcon(R.mipmap.icon_app).setWhen(System.currentTimeMillis()).build();
//                notificationManager.notify(notifyId++, notification);
//            }
        }

        @Override
        public void onInitBOSCallback(int i, String s) {

        }
    }

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private static OnTrackListener onTrackListener = new OnTrackListener() {
        @Override
        public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
            if (StatusCodes.SUCCESS != latestPointResponse.getStatus()) {
                return;
            }

            LatestPoint point = latestPointResponse.getLatestPoint();
            if (null == point || BaiduCommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation().getLongitude())) {
                return;
            }

            if (null != baiduMapUtil) {
                LatLng currentLatLng = baiduMapUtil.convertTrace2Map(point.getLocation());
                if (null == currentLatLng) {
                    return;
                }
                baiduMapUtil.updateStatus(currentLatLng, true);
            }
        }
    };

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private static OnEntityListener onEntityListener = new OnEntityListener() {
        @Override
        public void onReceiveLocation(TraceLocation traceLocation) {
            if (StatusCodes.SUCCESS != traceLocation.getStatus() || BaiduCommonUtil.isZeroPoint(traceLocation.getLatitude(), traceLocation.getLongitude())) {
                return;
            }
            if (null != baiduMapUtil) {
                LatLng currentLatLng = baiduMapUtil.convertTraceLocation2Map(traceLocation);
                if (null == currentLatLng) {
                    return;
                }
                baiduMapUtil.updateStatus(currentLatLng, true);
            }
        }
    };

    /**
     * 实时定位
     */
    private static class MyRouteThread implements Runnable {

        private int interval = 0;

        public MyRouteThread(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                //获取当前定位
                getCurrentLocation(onEntityListener, onTrackListener);
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    private static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 开启实时定位
     *
     * @param interval
     */
    public void startRouteTrace(int interval) {
        isRealTimeRunning = true;
        myRouteThread = new MyRouteThread(interval);
        realTimeHandler.post(myRouteThread);
    }

    /**
     * 关闭实时定位
     */
    public void stopRouteTrace() {
        isRealTimeRunning = false;
        if (null != realTimeHandler && null != myRouteThread) {
            realTimeHandler.removeCallbacks(myRouteThread);
        }
    }

    /**
     * 获取当前位置
     */
    private static void getCurrentLocation(OnEntityListener entityListener, OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetUtil.isNetworkAvailable(context) && sharedUtils.getIsGatherStarted() && sharedUtils.getIsTraceStarted()) {
            LatestPointRequest request = new LatestPointRequest(tag, serviceId, entityName);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            client.queryLatestPoint(request, trackListener);
        } else {
            client.queryRealTimeLoc(locRequest, entityListener);
        }
    }

}
