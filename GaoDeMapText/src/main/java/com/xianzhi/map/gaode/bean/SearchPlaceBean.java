package com.xianzhi.map.gaode.bean;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @Demo class SearchPlaceBean
 * @Description TODO
 * @date 2019-10-30 16:42
 */
public class SearchPlaceBean implements Serializable {

    /*添加二次查询的结果*/
    private List<PoiItem> places = new ArrayList<>();
    /*只查询一次*/
    private List<Tip> address = new ArrayList<>();

    public List<PoiItem> getPlaces() {
        return places;
    }

    public void setPlaces(List<PoiItem> places) {
        this.places = places;
    }

    public List<Tip> getAddress() {
        return address;
    }

    public void setAddress(List<Tip> address) {
        this.address = address;
    }
}
