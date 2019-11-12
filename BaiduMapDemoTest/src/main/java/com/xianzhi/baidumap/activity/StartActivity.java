package com.xianzhi.baidumap.activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.activity.navigation.NavigationSearchActivity;
import com.xianzhi.baidumap.activity.route.plan.RoutePlanningActivity;
import com.xianzhi.baidumap.activity.route.trace.RouteTraceActivity;
import com.xianzhi.baidumap.activity.search.SearchPlaceActivity;
import com.xianzhi.baidumap.baidu.BaiduNavigationManager;
import com.xianzhi.baidumap.base.BaseActivity;
import com.xianzhi.baidumap.base.BaseApplication;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.service.ForegroundService;
import com.xianzhi.baidumap.service.VideoService;
import com.xianzhi.baidumap.tools.FileUtils;

import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class StartActivity
 * @Description TODO
 * @date 2019-09-25 13:51
 */
public class StartActivity extends BaseActivity {

    private boolean hasInitSuccess = false;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";

    @Override
    protected void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void initView() {
        // 开启前台服务防止应用进入后台gps挂掉
        startService(new Intent(this, ForegroundService.class));
        //初始化百度地图导航和百度地图语音
        BaiduNavigationManager.getInstance(getApplicationContext(), APP_FOLDER_NAME, FileUtils.initDirs(APP_FOLDER_NAME), new BaiduLocationListener.OnNavigationInitClickListener() {
            @Override
            public void onNavigationClick() {
                hasInitSuccess = true;
            }
        });
    }

    @OnClick({R.id.tv_search, R.id.tv_route, R.id.tv_navigation, R.id.tv_routeTrace})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(context, SearchPlaceActivity.class));
                break;
            case R.id.tv_route:
                startActivity(new Intent(context, RoutePlanningActivity.class));
                break;
            case R.id.tv_navigation:
                if (hasInitSuccess) {
                    startActivity(new Intent(context, NavigationSearchActivity.class));
                }
                break;
            case R.id.tv_routeTrace:
                startActivity(new Intent(context, RouteTraceActivity.class));
                break;
        }
    }

}
