package com.xianzhi.map.gaode.view;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.xianzhi.map.R;
import com.xianzhi.map.activity.StartActivity;

/**
 * @author LiMing
 * @Demo class NotificationView
 * @Description TODO
 * @date 2019-11-05 16:10
 */
public class NotificationView {

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static Notification createNotification(Context context) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID_SERVICE_RUNNING", "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(context, "CHANNEL_ID_SERVICE_RUNNING");
        } else {
            builder = new Notification.Builder(context);
        }
        Intent nfIntent = new Intent(context, StartActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, nfIntent, 0)).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("猎鹰sdk运行中").setContentText("猎鹰sdk运行中");
        Notification notification = builder.build();
        return notification;
    }
}
