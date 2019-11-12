package com.xianzhi.baidumap.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author LiMing
 * @Demo class SharedUtils
 * @Description TODO
 * @date 2019-10-16 17:19
 */
public class SharedUtils {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedUtils(Context context) {
        sp = context.getSharedPreferences("UserDate", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 是否开启服务
     * @param isTraceStarted
     */
    public void setIsTraceStarted(boolean isTraceStarted) {
        editor.putBoolean("isTraceStarted", isTraceStarted);
        editor.commit();
    }

    public boolean getIsTraceStarted() {
        return sp.getBoolean("isTraceStarted", false);
    }

    /**
     * 是否开启采集
     * @param isGatherStarted
     */
    public void setIsGatherStarted(boolean isGatherStarted) {
        editor.putBoolean("isGatherStarted", isGatherStarted);
        editor.commit();
    }

    public boolean getIsGatherStarted() {
        return sp.getBoolean("isGatherStarted", false);
    }

    /**
     * 当前定位点
     * @param lastLocation
     */
    public void setLastLocation(String lastLocation) {
        editor.putString("lastLocation", lastLocation);
        editor.commit();
    }

    public String getLastLocation() {
        return sp.getString("lastLocation", "");
    }
}
