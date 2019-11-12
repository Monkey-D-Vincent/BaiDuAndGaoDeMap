package com.xianzhi.baidumap.baidu;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNOuterSettingParams;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.bean.BaiduNavigationBean;
import com.xianzhi.baidumap.bean.RoutePlanningSearchBean;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.tools.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @Demo class BaiduNavigationManager
 * @Description TODO
 * @date 2019-10-12 15:45
 */
public class BaiduNavigationManager {

    private static BaiduNavigationManager manager;
    private static Context context;
    private static String appFolderName;
    private static String path;
    private static BaiduLocationListener.OnNavigationInitClickListener onNavigationInitClickListener;

    private BaiduNavigationManager() {
        /**
         * 初始化百度导航.
         *
         * @param context          建议是应用的context
         * @param sdcardRootPath   系统SD卡根目录路径
         * @param appFolderName    应用在SD卡中的目录名
         * @param naviInitListener 百度导航初始化监听器
         */
        BaiduNaviManagerFactory.getBaiduNaviManager().init(context, path, appFolderName, new IBaiduNaviManager.INaviInitListener() {

            @Override
            public void onAuthResult(int status, String msg) {
                if (0 != status) {
                    Toast.makeText(context, "key校验失败, " + msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void initStart() {
            }

            @Override
            public void initSuccess() {
                onNavigationInitClickListener.onNavigationClick();
                // 初始化tts
                initTTS();
            }

            @Override
            public void initFailed(int errCode) {
                Toast.makeText(context, "百度导航引擎初始化失败 " + errCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static BaiduNavigationManager getInstance(Context c, String folderName, String filePath, BaiduLocationListener.OnNavigationInitClickListener listener) {
        context = c;
        appFolderName = folderName;
        path = filePath;
        onNavigationInitClickListener = listener;
        if (manager == null) {
            return new BaiduNavigationManager();
        }
        return manager;
    }

    /**
     * 开启导航
     *
     * @param bean
     */
    public static void setNavigation(BaiduNavigationBean bean, final BaiduLocationListener.OnNavigationClickListener listener) {
        BNRoutePlanNode sNode = new BNRoutePlanNode.Builder().latitude(bean.getStartLat()).longitude(bean.getStartLng()).name(bean.getStartPlace()).description(bean.getStartPlaceDesc()).coordinateType(BNRoutePlanNode.CoordinateType.WGS84).build();
        BNRoutePlanNode eNode = new BNRoutePlanNode.Builder().latitude(bean.getEndLat()).longitude(bean.getEndLng()).name(bean.getEndPlace()).description(bean.getEndPlaceDesc()).coordinateType(BNRoutePlanNode.CoordinateType.WGS84).build();
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        //添加车牌信息
//        BaiduNaviManagerFactory.getCommonSettingManager().setCarNum(context, "粤B88888");

        /**
         * 设置算路偏好
         *
         * @param mode 算路偏好类型:
         *             {@linkplain NeRoutePlanPreference#ROUTE_PLAN_PREFERENCE_DEFAULT} 默认，智能推荐
         *             {@linkplain NeRoutePlanPreference#ROUTE_PLAN_PREFERENCE_TIME_FIRST} 时间最短，时间优先
         *             {@linkplain NeRoutePlanPreference#ROUTE_PLAN_PREFERENCE_NOTOLL} 少收费
         *             {@linkplain NeRoutePlanPreference#ROUTE_PLAN_PREFERENCE_AVOID_TRAFFIC_JAM} 躲避拥堵
         *             {@linkplain NeRoutePlanPreference#ROUTE_PLAN_PREFERENCE_NOHIGHWAY} 不走高速
         *             {@linkplain NeRoutePlanPreference#ROUTE_PLAN_PREFERENCE_ROAD_FIRST} 大路优先，高速优先
         *             {@linkplain NeRoutePlanPreference#ROUTE_PLAN_PREFERENCE_DISTANCE_FIRST} 距离最短，距离优先
         * @return
         */
        if (bean.getType() == 0) {
            BaiduNaviManagerFactory.getCommonSettingManager().setRouteSortMode(IBNOuterSettingParams.NeRoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT);
        } else if (bean.getType() == 1) {
            BaiduNaviManagerFactory.getCommonSettingManager().setRouteSortMode(IBNOuterSettingParams.NeRoutePlanPreference.ROUTE_PLAN_PREFERENCE_TIME_FIRST);
        } else if (bean.getType() == 2) {
            BaiduNaviManagerFactory.getCommonSettingManager().setRouteSortMode(IBNOuterSettingParams.NeRoutePlanPreference.ROUTE_PLAN_PREFERENCE_NOTOLL);
        } else if (bean.getType() == 3) {
            BaiduNaviManagerFactory.getCommonSettingManager().setRouteSortMode(IBNOuterSettingParams.NeRoutePlanPreference.ROUTE_PLAN_PREFERENCE_AVOID_TRAFFIC_JAM);
        } else if (bean.getType() == 4) {
            BaiduNaviManagerFactory.getCommonSettingManager().setRouteSortMode(IBNOuterSettingParams.NeRoutePlanPreference.ROUTE_PLAN_PREFERENCE_NOHIGHWAY);
        } else if (bean.getType() == 5) {
            BaiduNaviManagerFactory.getCommonSettingManager().setRouteSortMode(IBNOuterSettingParams.NeRoutePlanPreference.ROUTE_PLAN_PREFERENCE_ROAD_FIRST);
        }else if (bean.getType() == 6) {
            BaiduNaviManagerFactory.getCommonSettingManager().setRouteSortMode(IBNOuterSettingParams.NeRoutePlanPreference.ROUTE_PLAN_PREFERENCE_DISTANCE_FIRST);
        }

        BaiduNaviManagerFactory.getRoutePlanManager().routeplan(list, IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT, null, new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                        //算路开始
                        break;
                    case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                        //算路成功
                        listener.onNavigationClick(10);
                        break;
                    case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                        //算路失败
                        listener.onNavigationClick(01);
                        break;
                    case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                        //算路成功准备进入导航
//                        Intent intent = new Intent(DemoMainActivity.this, DemoGuideActivity.class);
//                        startActivity(intent);
                        listener.onNavigationClick(11);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 使用内置TTS
     */
    private void initTTS() {
        BaiduNaviManagerFactory.getTTSManager().initTTS(context, FileUtils.getSdcardDir(), appFolderName, getTTSAppID());
    }

    public static String getTTSAppID() {
        //需要去 https://ai.baidu.com/tech/speech 创建应用
        return "17519079";
    }

}
