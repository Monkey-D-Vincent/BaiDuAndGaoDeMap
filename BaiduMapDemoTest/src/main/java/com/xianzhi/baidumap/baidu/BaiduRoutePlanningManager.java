package com.xianzhi.baidumap.baidu;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xianzhi.baidumap.bean.RoutePlanningSearchBean;
import com.xianzhi.baidumap.listener.BaiduLocationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @Demo class BaiduRoutePlanningManager
 * @Description TODO 路线规划
 * @date 2019-09-29 17:34
 */
public class BaiduRoutePlanningManager {

    private static List<PoiInfo> places = new ArrayList<>();
    private static List<DrivingRouteLine> lines = new ArrayList<>();
    private static int index = 0;

    public static void routePlanning(Context context, DrivingRoutePlanOption option, RoutePlanSearch mSearch, RoutePlanningSearchBean bean, int type, BaiduLocationListener.OnRoutePlanningSearchClickListener listener) {
        //开启路况
        option.trafficPolicy(DrivingRoutePlanOption.DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC);
        PlanNode startNode = PlanNode.withCityNameAndPlaceName(bean.getStartCity(), bean.getStartName());
        // 终点参数
        PlanNode endNode = PlanNode.withCityNameAndPlaceName(bean.getEndCity(), bean.getEndName());
        switch (type) {
            case 0:
                // 时间优先策略，  默认时间优先
                option.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST);
                break;
            case 1:
                // 躲避拥堵策略
                option.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM);
                break;
            case 2:
                // 最短距离策略
                option.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST);
                break;
            case 3:
                // 费用较少策略
                option.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_FEE_FIRST);
                break;
            default:
                break;
        }
        setRoutePlanning(context, option, mSearch, startNode, endNode, listener);

    }

    private static void setRoutePlanning(final Context context, final DrivingRoutePlanOption option, final RoutePlanSearch mSearch, final PlanNode startNode, PlanNode endNode, final BaiduLocationListener.OnRoutePlanningSearchClickListener listener) {
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            /**
             * 步行
             * @param walkingRouteResult
             */
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            }

            /**
             * 乘车
             * @param transitRouteResult
             */
            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
            }

            /**
             * 自驾
             * @param drivingRouteResult
             */
            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                if (drivingRouteResult != null && drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    places.addAll(drivingRouteResult.getSuggestAddrInfo().getSuggestEndNode());
                    for (int i = 0; i < places.size(); i++) {
                        PlanNode endNode1 = PlanNode.withCityNameAndPlaceName(places.get(i).getCity(), places.get(i).getName());
                        setRoutePlanning(context, option, mSearch, startNode, endNode1, listener);
                    }
                    return;
                }
                if (drivingRouteResult == null || drivingRouteResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**多条线路*/
                if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    if(places.size() > 0) {
                        index ++;
                    }
                    lines.addAll(drivingRouteResult.getRouteLines());
                    if (drivingRouteResult.getRouteLines().size() >= 1 && index == places.size()) {
                        listener.onSearchClick(lines);
                    }
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
            }

            /**
             * 骑行
             * @param bikingRouteResult
             */
            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            }
        });
        /**一定要卸载注册监听后边*/
        mSearch.drivingSearch(option.from(startNode).to(endNode));
    }

}
