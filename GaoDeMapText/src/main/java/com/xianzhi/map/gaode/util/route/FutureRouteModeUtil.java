package com.xianzhi.map.gaode.util.route;

/**
 * @author LiMing
 * @Demo class FutureRouteModeUtil
 * @Description TODO
 * @date 2019-11-01 17:52
 */
public class FutureRouteModeUtil {

    /*返回的结果考虑路况，尽量躲避拥堵而规划路径，与高德地图的“躲避拥堵”策略一致*/
    public static final int AVOIDINGCONGESTION = 1;
    /*返回的结果不走高速，与高德地图“不走高速”策略一致*/
    public static final int NOTHIGHWAY = 2;
    /*返回的结果尽可能规划收费较低甚至免费的路径，与高德地图“避免收费”策略一致*/
    public static final int AVOIDPAYMONEY = 3;
    /*返回的结果考虑路况，尽量躲避拥堵而规划路径，并且不走高速，与高德地图的“躲避拥堵&不走高速”策略一致*/
    public static final int AVOIDINGCONGESTIONANDNOTHIGHWAY = 4;
    /*返回的结果尽量不走高速，并且尽量规划收费较低甚至免费的路径结果，与高德地图的“避免收费&不走高速”策略一致*/
    public static final int AVOIDPAYMONEYANDNOTHIGHWAY = 5;
    /*返回路径规划结果会尽量的躲避拥堵，并且规划收费较低甚至免费的路径结果，与高德地图的“躲避拥堵&避免收费”策略一致*/
    public static final int AVOIDINGCONGESTIONANDAVOIDPAYMONEY = 6;
    /*返回的结果尽量躲避拥堵，规划收费较低甚至免费的路径结果，并且尽量不走高速路，与高德地图的“避免拥堵&避免收费&不走高速”策略一致*/
    public static final int AVOIDINGCONGESTIONANDNOTHIGHWAYANDAVOIDPAYMONEY = 7;
    /*返回的结果会优先选择高速路，与高德地图的“高速优先”策略一致*/
    public static final int PRIORITYHIGHWAY = 8;
    /*返回的结果会优先考虑高速路，并且会考虑路况躲避拥堵，与高德地图的“躲避拥堵&高速优先”策略一致*/
    public static final int AVOIDINGCONGESTIONANDPRIORITYHIGHWAY = 9;
    /*不考虑路况，返回速度最优、耗时最短的路线，但是此路线不一定距离最短*/
    public static final int ROUTESHORYEST = 10;
    /*避让拥堵&速度优先&避免收费*/
    public static final int AVOIDINGCONGESTIONANDROUTESHORYESTANDAVOIDPAYMONEY = 11;

}
