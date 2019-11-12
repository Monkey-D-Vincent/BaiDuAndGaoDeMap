package com.xianzhi.baidumap.baidu;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xianzhi.baidumap.listener.BaiduLocationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @Demo class BaiduSearchManager
 * @Description TODO 搜索地点
 * @date 2019-09-23 13:56
 */
public class BaiduSearchManager {

    private SuggestionSearch suggestionSearch;
    private PoiSearch poiSearch;
    private static BaiduSearchManager baiduSearchManager;
    private int index = 0;
    private List<String> cities = new ArrayList<>();

    private BaiduSearchManager() {
        suggestionSearch = SuggestionSearch.newInstance();
        poiSearch = PoiSearch.newInstance();
    }

    public static BaiduSearchManager getInstance() {
        if (baiduSearchManager == null) {
            baiduSearchManager = new BaiduSearchManager();
        }
        return baiduSearchManager;
    }

    /**
     * SUG 检索
     *
     * @param keyWork
     * @param city
     * @param listener
     */
    public void searchSuggestionPlace(final String keyWork, final String city, final BaiduLocationListener.OnSearchAddressClickListener listener) {
        cities.add(city);
        cities.add("中国");
        for (int i = 0; i < cities.size(); i++) {
            final List<PoiInfo> searchInfos = new ArrayList<>();
            poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
                @Override
                public void onGetPoiResult(PoiResult poiResult) {
                    if (poiResult.getAllPoi() != null && !poiResult.getAllPoi().isEmpty()) {
                        searchInfos.addAll(poiResult.getAllPoi());
                    }
                    if (index == cities.size() - 1) {
                        listener.onSearchClick(searchInfos);
                        return;
                    }
                    index++;
                }

                @Override
                public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                }

                @Override
                public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
                }

                @Override
                public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                }
            });
            // uid的集合，最多可以传入10个uid，多个uid之间用英文逗号分隔。
            poiSearch.searchInCity((new PoiCitySearchOption()).city("中国").keyword(keyWork).cityLimit(false).scope(2).pageCapacity(100));
        }
    }

    /**
     * 释放资源
     */
    public void setDestroy() {
        suggestionSearch.destroy();
        poiSearch.destroy();
    }


    /**
     * 搜索地址 地里边吗
     *
     * @param city
     * @param address  搜索地址
     * @param mSearch  GeoCoder.newInstance()
     * @param listener
     */
    public static void searchAddresOrNum(String city, String address, GeoCoder mSearch, final BaiduLocationListener.OnSearchAddressOrNumClickListener listener) {
        // 地理位置搜索
        //city(editCity.getText().toString())可以不写，GeoCodeOption共有两个方法，一个是查询城市，一个是查询地址；
        //但是address（）方法必须写
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            /**
             * 地理位置查询回调方法
             * @param geoCodeResult 返回的经纬度
             */
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                listener.onSearchClick(0, geoCodeResult, null);
            }

            /**
             * 反地理位置查询回调方法
             * @param reverseGeoCodeResult  返回的地理位置
             */
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                listener.onSearchClick(1, null, reverseGeoCodeResult);
            }
        });
        mSearch.geocode(new GeoCodeOption().city(city).address(address));
    }

}
