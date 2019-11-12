package com.xianzhi.baidumap.activity.route.trace;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.model.StatusCodes;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.baidu.BaiduLocationManager;
import com.xianzhi.baidumap.baidu.BaiduMapUtil;
import com.xianzhi.baidumap.baidu.BaiduRouteTraceManager;
import com.xianzhi.baidumap.baidu.tools.BitmapUtil;
import com.xianzhi.baidumap.base.BaseActivity;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.recever.TrackReceiver;
import com.xianzhi.baidumap.tools.SharedUtils;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;

/**
 * @author LiMing
 * @Demo class RouteTraceActivity
 * @Description TODO 路线追踪
 * @date 2019-10-16 14:28
 */
public class RouteTraceActivity extends BaseActivity {

    @BindView(R.id.mapView)
    MapView mapView;

    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    private TrackReceiver trackReceiver = null;
    //百度接口会一直会带哦  防止度偶次注册广播
    private boolean isRegisterReceiver = false;
    private BaiduMapUtil baiduMapUtil;

    //轨迹客户端
    public LBSTraceClient mClient;
    //轨迹服务
    public Trace mTrace;
    // 轨迹服务ID
    private static long serviceId = 216830;
    // 设备标识
    private static String entityName = "myTrace";
    private Notification notification;
    private SharedUtils sharedUtils;
    private BaiduRouteTraceManager manager;

    @Override
    protected void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_trace);
    }

    @Override
    protected void initView() {
        sharedUtils = new SharedUtils(context);
        baiduMapUtil = BaiduMapUtil.getInstance();
        BitmapUtil.init();
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        baiduMapUtil.initView(getApplicationContext(), mapView, false);
        baiduMapUtil.setLocation(1, false);
        mClient = new LBSTraceClient(getApplicationContext());
        mTrace = new Trace(serviceId, entityName);
        mTrace.setNotification(notification);
        if (sharedUtils.getIsTraceStarted()) {
            sharedUtils.setIsTraceStarted(false);
        }
        //开启精准定位
        manager = BaiduRouteTraceManager.getInstance(context, mClient, serviceId, entityName, getTag(), baiduMapUtil, mTrace);
        manager.startRouteTrace(10);
        initNotification();
    }

    @Override
    public void onResume() {
        super.onResume();
        baiduMapUtil.onResume();
        manager.startService(new BaiduLocationListener.OnRouteTraceClickListener() {
            @Override
            public void onRouteTraceClick(int type) {
                if (type == 0) {
                    registerReceiver();
                }
            }
        });
        if (null != mapView) {
            mapView.onResume();
        }
        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 注册广播（电源锁、GPS状态） 动态注册  防止锁屏状态下不能进行图像采集
     */
    @SuppressLint("InvalidWakeLockTag")
    private void registerReceiver() {
        if (isRegisterReceiver) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        registerReceiver(trackReceiver, filter);
        isRegisterReceiver = true;
    }

    /**
     * 解除注册
     */
    private void unregisterPowerReceiver() {
        if (!isRegisterReceiver) {
            return;
        }
        if (null != trackReceiver) {
            unregisterReceiver(trackReceiver);
        }
        isRegisterReceiver = false;
    }

    private void initNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        Intent notificationIntent = new Intent(this, RouteTraceActivity.class);

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_flash_on_white);

        // 设置PendingIntent
        builder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0)).setLargeIcon(icon)  // 设置下拉列表中的图标(大图标)
                .setContentTitle("百度鹰眼") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_flash_on_white) // 设置状态栏内的小图标
                .setContentText("服务正在运行...") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baiduMapUtil.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.stopRouteTrace();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "退出服务中，请稍后...", Toast.LENGTH_SHORT).show();
            unregisterPowerReceiver();
            manager.stopRouteTrace();
            manager.stopService(new BaiduLocationListener.OnRouteTraceClickListener() {
                @Override
                public void onRouteTraceClick(int type) {
                    if (type == 1) {
                        finish();
                    }
                }
            });
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMapUtil.clear();
    }

}
