package com.xianzhi.baidumap.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.search.core.PoiInfo;
import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.adpater.SearchAdapter;
import com.xianzhi.baidumap.baidu.BaiduLocationManager;
import com.xianzhi.baidumap.baidu.BaiduSearchManager;
import com.xianzhi.baidumap.base.BaseActivity;
import com.xianzhi.baidumap.listener.BaiduLocationListener;
import com.xianzhi.baidumap.view.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class SearchPlaceActivity
 * @Description TODO 搜索 - 列表显示
 * @date 2019-09-26 15:09
 */
public class SearchPlaceActivity extends BaseActivity implements SearchAdapter.OnSearchClickListener, BaiduLocationListener.OnLocationClickListener, TextWatcher, BaiduLocationListener.OnSearchAddressClickListener {

    @BindView(R.id.et_search)
    AutoCompleteTextView etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;

    private String city = "";
    private SearchAdapter adapter;

    @Override
    protected void onCreateView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_dialog);
    }

    @Override
    protected void initView() {
        rvAddress.setLayoutManager(new LinearLayoutManager(context));
        rvAddress.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 3, getResources().getColor(R.color.white)));
        adapter = new SearchAdapter(context, this);
        rvAddress.setAdapter(adapter);

        //定位获取所在城市，进行检索
        LocationClient client = new LocationClient(context);
        BaiduLocationManager.initLocationOption(client, this);

        //监听 editText 输入内容
        etSearch.addTextChangedListener(this);
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        if(!TextUtils.isEmpty(etSearch.getText().toString())) {
            BaiduSearchManager.getInstance().searchSuggestionPlace(etSearch.getText().toString(), city, this);
        } else {
            Toast.makeText(getApplicationContext(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() <= 0) {
            return;
        }
        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        BaiduSearchManager.getInstance().searchSuggestionPlace(s.toString(), city, this);
    }

    /**
     * 搜索到的地址
     *
     * @param bean
     */
    @Override
    public void onSearchClick(PoiInfo bean) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("data", bean);
        startActivity(intent);
    }


    /**
     * 定位当前城市
     *
     * @param location
     */
    @Override
    public void onLocation(BDLocation location) {
        if (location == null) {
            return;
        }
        city = !TextUtils.isEmpty(location.getCity()) ? location.getCity() : "";
    }

    @Override
    public void onSearchClick(List<PoiInfo> data) {
        adapter.addData(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        BaiduSearchManager.getInstance().setDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
