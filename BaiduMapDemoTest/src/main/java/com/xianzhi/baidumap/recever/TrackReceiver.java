package com.xianzhi.baidumap.recever;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.baidu.trace.model.StatusCodes;

/**
 * @author liMing
 * @Demo class TrackReceiver
 * @Description TODO
 * @date 2019-10-16 16:10
 */
public class TrackReceiver extends BroadcastReceiver {

    private PowerManager.WakeLock wakeLock;
    private final String TAG = "TrackReceiver";

    public TrackReceiver(PowerManager.WakeLock wakeLock) {
        super();
        this.wakeLock = wakeLock;
    }

    @SuppressLint("Wakelock")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            if (null != wakeLock && !(wakeLock.isHeld())) {
                wakeLock.acquire();
            }
        } else if (Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_USER_PRESENT.equals(action)) {
            if (null != wakeLock && wakeLock.isHeld()) {
                wakeLock.release();
            }
        } else if (StatusCodes.GPS_STATUS_ACTION.equals(action)) {
            int statusCode = intent.getIntExtra("statusCode", 0);
            String statusMessage = intent.getStringExtra("statusMessage");
            Log.i(TAG, String.format("GPS status, statusCode:%d, statusMessage:%s", statusCode, statusMessage));
        }
    }
}
