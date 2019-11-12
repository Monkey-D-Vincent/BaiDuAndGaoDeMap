package com.xianzhi.map.gaode.util.search;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.SubPoiItem;
import com.xianzhi.map.gaode.bean.SearchPlaceBean;
import com.xianzhi.map.listener.GaoDeOnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @Demo class GaoDeSearchUnit
 * @Description TODO POI 搜索
 * @date 2019-10-30 15:22
 */
public class GaoDeSearchUnit {

	private static GaoDeOnClickListener.OnSearchClickListener mListener;

	private static Context mContext;
	private static String mKeyWord;
	private static String mCity;

	private SearchPlaceBean bean;

	private InputtipsQuery query;
	private Inputtips inputTips;

	private GaoDeSearchUnit() {
		query = new InputtipsQuery(mKeyWord, mCity);
		inputTips = new Inputtips(mContext, query);
	}

	/**
	 * 限制在当前城市
	 *
	 * @param restrict true 限制
	 */
	public GaoDeSearchUnit setCityLimit(boolean restrict) {
		query.setCityLimit(restrict);
		return this;
	}

	/**
	 * 初始化
	 *
	 * @param context
	 * @param keyWord 关键字
	 * @param city    城市名
	 * @return
	 */
	public static GaoDeSearchUnit getInstance(Context context, String keyWord, String city) {
		mContext = context;
		mKeyWord = keyWord;
		mCity = city;
		return new GaoDeSearchUnit();
	}

	public GaoDeSearchUnit setSearchListener(GaoDeOnClickListener.OnSearchClickListener listener) {
		mListener = listener;
		return this;
	}

	/**
	 * 返回列表
	 *
	 * @see SearchPlaceBean
	 */
	public void searchPlaceList() {
		inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
			@Override
			public void onGetInputtips(List<Tip> list, int i) {
				if (i == AMapException.CODE_AMAP_SUCCESS) {
					bean = new SearchPlaceBean();
					bean.getAddress().addAll(list);
					mListener.onSearchClick(bean);
				}
			}
		});
		//发起请求
		inputTips.requestInputtipsAsyn();
	}

	/**
	 * POI 父子检索
	 */
	public void getPlace() {
		PoiSearch.Query query = new PoiSearch.Query(mKeyWord, "", mCity);
		query.setPageSize(10);
		query.setPageNum(0);
		//设置是否按距离排序
		query.setDistanceSort(false);
		//设置是否返回父子关系。
		query.requireSubPois(true);
		//keyWord表示搜索字符串，
		//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
		//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
		PoiSearch poiSearch = new PoiSearch(mContext, query);
		poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
			@Override
			public void onPoiSearched(PoiResult poiResult, int i) {
				if (i == AMapException.CODE_AMAP_SUCCESS) {
					if (poiResult != null) {
						bean = new SearchPlaceBean();
						List<PoiItem> poiItems = poiResult.getPois();
						for (int x = 0; x < poiItems.size(); x++) {
							List<SubPoiItem> subPoiItems = new ArrayList<>();
							for (int y = 0; y < poiItems.get(x).getSubPois().size(); y++) {
								if(y < 6) {
									SubPoiItem subPoiItem = poiItems.get(x).getSubPois().get(y);
									subPoiItems.add(subPoiItem);
								}
							}
							poiItems.get(x).setSubPois(subPoiItems);
						}
						bean.getPlaces().addAll(poiItems);
						mListener.onSearchClick(bean);
					}
				}
			}

			@Override
			public void onPoiItemSearched(PoiItem poiItem, int i) {
				Log.i("aaaaaaaa", poiItem.toString());
			}
		});
		//发送请求
		poiSearch.searchPOIAsyn();
	}
}
