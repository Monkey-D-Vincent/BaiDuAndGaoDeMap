package com.xianzhi.baidumap.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import java.io.File;

/**
 * @author LiMing
 * @Demo class BaseApplication
 * @Description TODO
 * @date 2019-09-25 15:38
 */
public class BaseApplication extends Application {

    public static String path = getSDPath() + "/百度地图测试/视频";
    public static String videoPath;
    public static boolean isVideoPlay = false;

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        // 默认本地个性化地图初始化方法
        SDKInitializer.initialize(this);

        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }

    }

    /**
     * 取SD卡路径
     **/
    public static String getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            //获取根目录
            sdDir = Environment.getExternalStorageDirectory();
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "/mnt/sdcard";
        }
    }
}
