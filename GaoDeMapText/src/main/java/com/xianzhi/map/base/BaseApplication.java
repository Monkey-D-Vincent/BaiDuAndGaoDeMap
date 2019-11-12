package com.xianzhi.map.base;

import android.app.Application;

import com.amap.api.services.route.DriveRoutePlanResult;

/**
 * @author LiMing
 * @Demo class BaseApplication
 * @Description TODO
 * @date 2019-09-25 15:38
 */
public class BaseApplication extends Application {

    public static String DEVICENAME = "测试";
    public static long SERVICEID = 83278;

    public static DriveRoutePlanResult result;

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
