package com.xianzhi.map.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xianzhi.map.R;
import com.xianzhi.map.adapter.SearchAdapter;
import com.xianzhi.map.base.BaseActivity;
import com.xianzhi.map.gaode.GaoDeMapManager;
import com.xianzhi.map.gaode.bean.SearchPlaceBean;
import com.xianzhi.map.listener.GaoDeOnClickListener;
import com.xianzhi.map.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class SearchActivity
 * @Description TODO 我只写 PoiItem 的 adapter，如果需要 Tip 的 adapter，请自己处理
 * @date 2019-10-29 11:00
 */
public class SearchActivity extends BaseActivity implements GaoDeOnClickListener.OnSearchActivityClickListener, SearchAdapter.OnSearchClickListener {

    @BindView(R.id.et_search)
    AutoCompleteTextView etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;
    @BindView(R.id.mMapView)
    MapView mMapView;

    private SearchAdapter adapter;
    private String city;
    private String keyword = "";

    private GaoDeMapManager manager;

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        rvAddress.setLayoutManager(new LinearLayoutManager(context));
        rvAddress.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 3, getResources().getColor(R.color.white)));
        adapter = new SearchAdapter(context, this);
        rvAddress.setAdapter(adapter);

        manager = GaoDeMapManager.getInstance(context, mMapView);
        manager.onCreate(savedInstanceState);
        manager.setLocation(new GaoDeOnClickListener.OnLocationActivityClickListener() {
            @Override
            public void onLocationClick(RegeocodeResult regeocodeResult) {
                city = regeocodeResult.getRegeocodeAddress().getCity();
            }
        });
        setEditTextListener();
    }

    private void setEditTextListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(tvMsg.getVisibility() == View.VISIBLE) {
                    tvMsg.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(s)) {
                    rvAddress.setVisibility(View.VISIBLE);
                    manager.searchPlace(getApplicationContext(), s.toString(), city, false, SearchActivity.this);
                } else {
                    rvAddress.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        if(!TextUtils.isEmpty(etSearch.getText().toString())) {
            if(!keyword.equals(etSearch.getText().toString())) {
                keyword = etSearch.getText().toString();
                rvAddress.setVisibility(View.VISIBLE);
                manager.searchPlace(getApplicationContext(), etSearch.getText().toString(), city, false, this);
            }
        } else {
            rvAddress.setVisibility(View.GONE);
        }
    }

    /**
     * 查询结果
     * @param bean
     */
    @Override
    public void onSearchClick(SearchPlaceBean bean) {
        adapter.addData(bean.getPlaces());
    }

    /**
     * item 点击事件
     * @param bean
     */
    @Override
    public void onSearchClick(PoiItem bean) {
        startActivity(new Intent(context, LocationPlaceActivity.class).putExtra("data", bean));
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        manager.onSaveInstanceState(outState);
    }

}
